package com.example.nmsadminapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nmsadminapp.R
import androidx.viewpager2.widget.ViewPager2
import com.example.nmsadminapp.adapters.ViewPagerAdapter2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pager: ViewPager2 = view.findViewById(R.id.viewPager) // creating object of ViewPager
        val tab: TabLayout = view.findViewById(R.id.tabs)  // creating object of TabLayout

        // creating object of ViewPagerAdapter2
        val adapter = ViewPagerAdapter2(childFragmentManager, lifecycle)

        // setting adapter to pager
        pager.adapter = adapter

        // set save enabled to false
        pager.isSaveEnabled = false
        TabLayoutMediator(tab, pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Products"
                1 -> tab.text = "Categories"
            }
        }.attach()
    }
}