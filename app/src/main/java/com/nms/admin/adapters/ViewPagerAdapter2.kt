package com.nms.admin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nms.admin.fragments.TabCategoryFragment
import com.nms.admin.fragments.TabProductFragment

class ViewPagerAdapter2(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TabProductFragment()
            1 -> TabCategoryFragment()
            else -> Fragment()
        }
    }
}