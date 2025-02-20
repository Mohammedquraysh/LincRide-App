package com.example.lincrideapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lincrideapp.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class PlacesAutoCompleteAdapter(private val context: Context) :
    RecyclerView.Adapter<PlacesAutoCompleteAdapter.ViewHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val places = mutableListOf<PlaceAutocomplete>()
    private val styleBold: CharacterStyle = StyleSpan(Typeface.BOLD)
    private val styleNormal: CharacterStyle = StyleSpan(Typeface.NORMAL)
    private val placesClient: PlacesClient =
        com.google.android.libraries.places.api.Places.createClient(context)
    private var listener: PlacesDetail? = null
    var itemClicked: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.listrow_other_places, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.tvAddressTitle.text = place.area
        holder.tvAddressDesc.text = place.address

        holder.itemView.setOnClickListener {
            getPlaceDetail(place.placeId)
            itemClicked = true
        }

        if (position == places.size - 1) {
            holder.viewDivider.visibility = View.GONE
        } else {
            holder.viewDivider.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }

    fun setListener(listener: PlacesDetail) {
        this.listener = listener
    }

    interface PlacesDetail {
        fun getDetail(place: Place)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llMain: LinearLayout = itemView.findViewById(R.id.llMain)
        val tvAddressTitle: TextView = itemView.findViewById(R.id.tvAddress)
        val viewDivider: View = itemView.findViewById(R.id.ViewDivider)
        val tvAddressDesc: TextView = itemView.findViewById(R.id.tvAddressDesc)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (!constraint.isNullOrBlank() && !itemClicked) { // Check if itemClicked is false
                    val filteredPlaces = getPlacePredictions(constraint.toString())
                    results.values = filteredPlaces
                    results.count = filteredPlaces.size
                }
                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    places.clear()
                    places.addAll(results.values as List<PlaceAutocomplete>)
                    notifyDataSetChanged()
                } else {
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun getPlacePredictions(constraint: String): List<PlaceAutocomplete> {
        val resultList = mutableListOf<PlaceAutocomplete>()
        val bounds = RectangularBounds.newInstance(
            LatLng(23.63936, 68.14712),
            LatLng(28.20453, 97.34466)
        )
        val request = FindAutocompletePredictionsRequest.builder()
            // .setCountry("IN")
            .setQuery(constraint)
            .build()

        val autocompletePredictions = placesClient.findAutocompletePredictions(request)

        try {
            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS)
            Log.e("AutoComplete", "Success")
        } catch (e: ExecutionException) {
            Log.e("AutoComplete", e.localizedMessage)
        } catch (e: InterruptedException) {
            Log.e("AutoComplete", e.localizedMessage)
        } catch (e: TimeoutException) {
            Log.e("AutoComplete", e.localizedMessage)
        }

        if (autocompletePredictions.isSuccessful) {
            val predictions = autocompletePredictions.result?.autocompletePredictions
            predictions?.forEach { prediction ->
                resultList.add(
                    PlaceAutocomplete(
                        prediction.placeId,
                        prediction.getPrimaryText(styleNormal).toString(),
                        prediction.getFullText(styleBold).toString()
                    )
                )
            }
        }
        return resultList
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPlaceDetail(placeId: String) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                listener?.getDetail(place)
                notifyDataSetChanged()
                // notifyDataSetInvalidated()
            }
            .addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.e("PlaceComplete", "Api on Failure: ${exception.message}")
                }
            }
    }

    data class PlaceAutocomplete(val placeId: String, val area: CharSequence, val address: CharSequence)

    fun clear() {
        places.clear()
        notifyDataSetChanged()
    }
}