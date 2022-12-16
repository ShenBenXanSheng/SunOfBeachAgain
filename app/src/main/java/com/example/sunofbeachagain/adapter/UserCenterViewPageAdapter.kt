package com.example.sunofbeachagain.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sunofbeachagain.fragment.UserCenterEssayFragment
import com.example.sunofbeachagain.fragment.UserCenterFansFragment
import com.example.sunofbeachagain.fragment.UserCenterFollowFragment
import com.example.sunofbeachagain.fragment.UserCenterMoYuFragment

class UserCenterViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var token: String = ""
    private val tabTitleList = mutableListOf<String>()
    private var userId = ""
    override fun getItemCount() = tabTitleList.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                UserCenterMoYuFragment(userId,token)

            }

            1 -> {
                UserCenterEssayFragment(userId,token)
            }

            2 -> {
                UserCenterFollowFragment(userId,token)
            }


            else -> {
                UserCenterFansFragment(userId,token)
            }
        }
    }

    fun getToken(token: String) {
        this.token = token
    }

    fun getUserId(userId: String) {
        this.userId = userId
    }

    fun getTitleData(userCenterTabLayoutTitles: MutableList<String>) {
        tabTitleList.clear()
        tabTitleList.addAll(userCenterTabLayoutTitles)
        notifyDataSetChanged()
    }
}