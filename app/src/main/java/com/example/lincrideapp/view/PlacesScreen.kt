package com.example.lincrideapp.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lincrideapp.R
import com.example.lincrideapp.adapter.PlacesAutoCompleteAdapter
import com.example.lincrideapp.data.room.Ride
import com.example.lincrideapp.databinding.FragmentPlacesScreenBinding
import com.example.lincrideapp.databinding.OfferRequestDialogBinding
import com.example.lincrideapp.util.getCurrentDateTime
import com.example.lincrideapp.util.getFormattedAmt
import com.example.lincrideapp.viewmodel.RideViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round


@AndroidEntryPoint
class PlacesScreen : Fragment(R.layout.fragment_places_screen) {
    private lateinit var binding : FragmentPlacesScreenBinding
    private val viewModel: RideViewModel by viewModels()

    private var dropOffLatLng = LatLng(0.0, 0.0)
    private var dropOffAddress = ""
    private var pickUpLatLng = LatLng(0.0,0.0)
    private var pickupAddress = ""



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlacesScreenBinding.bind(view)


        binding.apply {
            toolbarBookRide.setNavigationIcon(R.drawable.arrow_back)
            /** to navigate back **/
            toolbarBookRide.setNavigationOnClickListener {
                findNavController().navigateUp()
            }


            val apiKey = getString(R.string.maps_api_key)
            Places.initialize(requireContext(), apiKey)

            setupRecyclerView()
            setAutoCompleteTextView()
            setPickUpAutoCompleteTextView()

           btnAcceptRideNew.setOnClickListener {
               validateFields()
            }

             btnRideHistory.setOnClickListener {
                findNavController().navigate(R.id.rideHistoryScreen)
            }
        }

    }


    // set up the the recyclerview for each places user to select
    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.rvSearchAddress
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val adapter = PlacesAutoCompleteAdapter(requireContext())
        recyclerView.adapter = adapter
    }


    // set the auto Complete for drop off address
    private fun setAutoCompleteTextView() {
        val placesAutoCompleteAdapter =
            PlacesAutoCompleteAdapter(
                requireContext()
            )

        placesAutoCompleteAdapter.setListener(object : PlacesAutoCompleteAdapter.PlacesDetail {
            override fun getDetail(place: Place) {
                // Handle place detail here
                binding.edtPlaceAddress.setText(place.address)
                binding.edtPlaceAddress.clearFocus()
                dropOffLatLng = place.latLng!!
                dropOffAddress = place.address!!
                Log.e("PlaceAutoComplte", "getDetail:  PlaceComplete listner : " + place)
                binding.rvSearchAddress.visibility = View.GONE
            }
        })

        binding.edtPlaceAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                if (searchText.length >= 3) {
                    // Perform search based on searchText
                    placesAutoCompleteAdapter.filter.filter(searchText)
                    binding.rvSearchAddress.visibility = View.VISIBLE
                } else {
                    // Clear search results or show previous results if any
                    placesAutoCompleteAdapter.clear()
                    placesAutoCompleteAdapter.itemClicked = false
                }
            }
        })
        binding.rvSearchAddress.adapter = placesAutoCompleteAdapter

    }

    // set the auto Complete for pick up address
    private fun setPickUpAutoCompleteTextView() {
        Log.e("MQ", "getDetail:  PickUpPlaceComplete listner : ")

        val placesAutoCompleteAdapter =
            PlacesAutoCompleteAdapter(
                requireContext()
            )

        placesAutoCompleteAdapter.setListener(object : PlacesAutoCompleteAdapter.PlacesDetail {
            override fun getDetail(place: Place) {
                // Handle place detail here
                binding.edtPickUpAddress.setText(place.address)
                binding.edtPickUpAddress.clearFocus()
                pickUpLatLng = place.latLng!!
                pickupAddress = place.address!!
                binding.rvPickupSearchAddress.visibility = View.GONE
            }
        })

        binding.edtPickUpAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                if (searchText.length >= 3) {
                    // Perform search based on searchText
                    placesAutoCompleteAdapter.filter.filter(searchText)
                    binding.rvPickupSearchAddress.visibility = View.VISIBLE
                } else {
                    // Clear search results or show previous results if any
                    placesAutoCompleteAdapter.clear()
                    placesAutoCompleteAdapter.itemClicked = false
                }
            }
        })
        binding.rvPickupSearchAddress.adapter = placesAutoCompleteAdapter
    }

    //  fare estimate dialog
    @SuppressLint("ResourceType")
    private fun estimateDialog() {
        var tfare = 0.00

        val dialog = Dialog(requireContext())
        val binding = OfferRequestDialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)

        viewModel.fareEstimate.observe(viewLifecycleOwner){
            tfare = round(it.total_fare)
            binding.tvFare.text = getFormattedAmt( tfare, "₦")
            binding.tvDistance.text = round(it.distance_fare).toString()
            binding.tvPickupLocation.text = pickupAddress
            binding.tvDropoffLocation.text = dropOffAddress

        }

        val window = dialog.window
        val params = window?.attributes
        params?.width = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._265sdp)
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        binding.btnAcceptRideNew.setOnClickListener {
            val ride  = Ride(pickupLocation = pickupAddress, destinationLocation = dropOffAddress, fare = tfare, timestamp = getCurrentDateTime() )
            viewModel.requestRide(ride)
            dialog.dismiss()
        }
        binding.btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    // validate the pickup and drop off fields
    @RequiresApi(Build.VERSION_CODES.O)
    private fun validateFields(){
        if (pickupAddress == ""){
            Toast.makeText(requireContext(), "Please add Pickup Address", Toast.LENGTH_SHORT).show()
        }else if(dropOffAddress == ""){
            Toast.makeText(requireContext(), "Please add Dropoff Address", Toast.LENGTH_SHORT).show()
        }else{
            viewModel.getFareEstimate(pickUpLatLng, dropOffLatLng)
            estimateDialog()
        }
    }


}