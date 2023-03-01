package com.nms.admin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.nms.admin.R
import com.nms.admin.adapters.ViewPagerOrderAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrdersFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pager: ViewPager2 = view.findViewById(R.id.viewPager) // creating object of ViewPager
        val tab: TabLayout = view.findViewById(R.id.tabs)  // creating object of TabLayout

        // creating object of ViewPagerAdapter2
        val adapter = ViewPagerOrderAdapter(childFragmentManager, lifecycle)

        // setting adapter to pager
        pager.adapter = adapter

        // set save enabled to false
        pager.isSaveEnabled = false
        TabLayoutMediator(tab, pager) { tab, position ->
            when (position) {
                0 -> tab.text = "New Orders"
                1 -> tab.text = "Custom Orders"
            }
        }.attach()
    }
}