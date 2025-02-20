package com.example.lincrideapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lincrideapp.data.room.Ride
import com.example.lincrideapp.databinding.HistoryLayoutBinding
import java.text.SimpleDateFormat
import java.util.Date

//class RideHistoryAdapter(private val rides: List<Ride>) : RecyclerView.Adapter<RideHistoryAdapter.RideViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(android.R.layout.simple_list_item_2, parent, false)
//        return RideViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
//        val ride = rides[position]
//        holder.bind(ride)
//    }
//
//    override fun getItemCount(): Int = rides.size
//
//    class RideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(ride: Ride) {
//            itemView.findViewById<TextView>(android.R.id.text1).text =
//                "From: ${ride.pickupLocation} To: ${ride.destinationLocation}"
//            itemView.findViewById<TextView>(android.R.id.text2).text =
//                "Fare: $${ride.fare} | ${SimpleDateFormat("MMM dd, yyyy HH:mm").format(Date(ride.timestamp))}"
//        }
//    }
//}




class RideHistoryAdapter : ListAdapter<Ride, RideHistoryAdapter.PendingCheckoutViewHolder>(DiffUtilCallBack) {

    var differ = AsyncListDiffer(this, DiffUtilCallBack)
    var onItemClicked: ((Ride) -> Unit)? = null


    inner class PendingCheckoutViewHolder(binding: HistoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val pickupLocation = binding.tvPickupLocation
        private val dropOffLocation = binding.tvDropoffLocation
        private val bookedDate = binding.tvDate




        @RequiresApi(Build.VERSION_CODES.O)
        fun bindView(item: Ride) {

            bookedDate.text = "${item.timestamp}"
            pickupLocation.text = item.pickupLocation
            dropOffLocation.text = item.destinationLocation
            itemView.setOnClickListener {
                onItemClicked?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingCheckoutViewHolder {
        val binding = HistoryLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PendingCheckoutViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PendingCheckoutViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bindView(item)
    }


    object DiffUtilCallBack : DiffUtil.ItemCallback<Ride>() {
        override fun areItemsTheSame(oldItem: Ride, newItem: Ride): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ride, newItem: Ride): Boolean {
            return oldItem.id == newItem.id && oldItem.pickupLocation == newItem.pickupLocation
        }
    }

    fun clearData() {
        differ.submitList(emptyList())
    }


}
