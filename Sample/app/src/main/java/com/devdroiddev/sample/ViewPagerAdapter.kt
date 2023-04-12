package com.devdroiddev.sample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fm: FragmentActivity,
    items: List<Fragment>
) : FragmentStateAdapter(fm) {

    var pageItems = items.toMutableList()

    override fun getItemCount(): Int = pageItems.size

    override fun createFragment(position: Int): Fragment = pageItems[position]
}