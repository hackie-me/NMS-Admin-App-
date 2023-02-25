package com.example.nmsadminapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.nmsadminapp.R
import com.example.nmsadminapp.databinding.ActivityMainBinding
import com.straiberry.android.charts.extenstions.HorizontalChartXLabels
import com.straiberry.android.charts.extenstions.horizontalChartData
import com.straiberry.android.charts.tooltip.SliderTooltip

class HomeFragment : Fragment() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sliderTooltip: SliderTooltip
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sliderTooltip = SliderTooltip()
        setupHorizontalChart()
    }

    /** Setup data for horizontal chart */
    private fun setupHorizontalChart() {
        horizontalChartData(
            requireContext(), HorizontalChartXLabels(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_home)!!, "user",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_home)!!, "user",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_home)!!, "age",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_home)!!, "gender",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_home)!!, "location",
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_home)!!, "master",
            ), listOf(6F, 7F, 2F, 7F, 7F, 5F)
        )
    }
}