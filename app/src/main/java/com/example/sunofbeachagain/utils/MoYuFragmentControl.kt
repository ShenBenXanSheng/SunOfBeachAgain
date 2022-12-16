package com.example.sunofbeachagain.utils

import androidx.fragment.app.Fragment
import com.example.sunofbeachagain.fragment.MoYuFollowFragment
import com.example.sunofbeachagain.fragment.MoYuListFragment

object MoYuFragmentControl {

    private lateinit var currentFragment: Fragment

    fun getFragmentByPosition(position: Int): Fragment {


        when (position) {
            0 -> {
                currentFragment = MoYuListFragment()

            }

            1 -> {
                currentFragment = MoYuFollowFragment()

            }
        }



        return currentFragment


    }
}