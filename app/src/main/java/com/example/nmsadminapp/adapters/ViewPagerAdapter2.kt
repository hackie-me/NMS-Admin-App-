package com.example.nmsadminapp.adapters

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nmsadminapp.fragments.CategoryTabFragment
import com.example.nmsadminapp.fragments.ProductTabFragment

class ViewPagerAdapter2(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductTabFragment()
            1 -> CategoryTabFragment()
            else -> Fragment()
        }
    }
}