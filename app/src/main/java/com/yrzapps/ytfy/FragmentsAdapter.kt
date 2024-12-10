package com.yrzapps.ytfy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(manager : FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager,lifecycle) {

    val fragments : List<Fragment> = listOf(PlayerFragment(),SearchFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}