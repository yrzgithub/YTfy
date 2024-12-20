package com.yrzapps.ytfy.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yrzapps.ytfy.R
import com.yrzapps.ytfy.adapters.FragmentAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val pager: ViewPager2 = findViewById(R.id.pager)

        val fragmentAdapter: FragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)

        pager.adapter = fragmentAdapter

        TabLayoutMediator(tabLayout, pager, true) {

                tab, position ->
            tab.setIcon(
                when (position) {
                    0 -> R.drawable.home
                    1 -> R.drawable.search
                    2 -> R.drawable.favourites
                    else -> 0
                }
            )

        }.attach()
    }
}