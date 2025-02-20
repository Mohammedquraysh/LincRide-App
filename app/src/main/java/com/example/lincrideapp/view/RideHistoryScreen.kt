package com.example.lincrideapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lincrideapp.R
import com.example.lincrideapp.adapter.RideHistoryAdapter
import com.example.lincrideapp.databinding.FragmentRideHistoryScreenBinding
import com.example.lincrideapp.viewmodel.RideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RideHistoryScreen : Fragment(R.layout.fragment_ride_history_screen) {
    private lateinit var binding : FragmentRideHistoryScreenBinding
    private lateinit var historyAdapter: RideHistoryAdapter
    private lateinit var historyRecyclerView: RecyclerView
    private val viewModel: RideViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRideHistoryScreenBinding.bind(view)

        binding.apply {
            toolbarRideHistory.setNavigationIcon(R.drawable.arrow_back)
            /** to navigate back **/
            toolbarRideHistory.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        initRecyclerview()
    }

    // initialise the recyclerview to display booking history
    private fun initRecyclerview() {
        historyRecyclerView = binding.itemRecyclerview
        historyAdapter = RideHistoryAdapter()
        historyAdapter.onItemClicked = { cargo -> }
        historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
        viewModel.rideHistory.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                historyAdapter.differ.submitList(it)
            }else{
                binding.noBookTv.visibility = View.VISIBLE
                binding.noBookingImage.visibility = View.VISIBLE
            }
        }
    }
}