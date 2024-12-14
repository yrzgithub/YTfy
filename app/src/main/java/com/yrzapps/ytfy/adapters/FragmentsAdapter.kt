package com.yrzapps.ytfy.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yrzapps.ytfy.fragments.FavouritesFragment
import com.yrzapps.ytfy.fragments.PlayerFragment
import com.yrzapps.ytfy.fragments.SearchFragment

class FragmentAdapter(manager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(manager, lifecycle) {

    val fragments: List<Fragment> = listOf(PlayerFragment(), SearchFragment(), FavouritesFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}