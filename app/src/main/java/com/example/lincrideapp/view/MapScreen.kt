package com.example.lincrideapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lincrideapp.R
import com.example.lincrideapp.databinding.FragmentMapScreenBinding
import com.example.lincrideapp.util.getGreeting
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class MapScreen : Fragment(R.layout.fragment_map_screen), OnMapReadyCallback, View.OnClickListener {
    private lateinit var binding : FragmentMapScreenBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private var pickupLatLng = LatLng(0.0, 0.0)
    private val REQUEST_CODE = 101



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapScreenBinding.bind(view)



        binding.apply {

            llWhereTo.setOnClickListener {findNavController().navigate(R.id.placesScreen)}
            tvGoodName.text  = buildString {
                append(getGreeting())
                append("Guest")
            }
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        }else{
            Log.e("MQ", "Map fragment is null")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        } else {
            getCurrentLocation()

        }

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }





    // to get user current location
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                pickupLatLng = LatLng(location.latitude, location.longitude)

                val markerIcon =
                    BitmapFactory.decodeResource(resources, R.drawable.ic_current_marker)
                val scaledMarkerIcon = Bitmap.createScaledBitmap(markerIcon, 100, 100, false)

                map.clear()
                map.addMarker(
                    MarkerOptions()
                        .position(pickupLatLng)
                        .title("My Location")
                        .icon(BitmapDescriptorFactory.fromBitmap(scaledMarkerIcon))
                )

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(pickupLatLng, 15.0f))

                // Run geocoding in a background thread
                Thread {
                    try {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val addresses: MutableList<Address>? = geocoder.getFromLocation(
                            pickupLatLng.latitude,
                            pickupLatLng.longitude,
                            1
                        )

                        // Ensure that addresses is not empty and handle the result
                        if (addresses != null && addresses.isNotEmpty()) {
                            val address = addresses[0].getAddressLine(0)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }.start()
            } else {
                isLocationEnabled() // Handle case where location is null
            }
        }
    }


    private fun isLocationEnabled() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            getCurrentLocation()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(requireActivity(), REQUEST_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocationEnabled()
                }
            }
        }
    }

    fun areLocationGranted(): Boolean {
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )

        return coarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
                fineLocationPermission == PackageManager.PERMISSION_GRANTED
    }





}