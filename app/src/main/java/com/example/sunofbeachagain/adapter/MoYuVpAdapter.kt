package com.example.sunofbeachagain.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sunofbeachagain.utils.MoYuFragmentControl

class MoYuVpAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val titleList = mutableListOf<String>()

    override fun getItemCount() = titleList.size

    override fun createFragment(position: Int): Fragment {
        val fragmentByPosition = MoYuFragmentControl.getFragmentByPosition(position)
        if (fragmentByPosition != null) {
            return fragmentByPosition
        } else {
            throw  NullPointerException()
        }

    }

    fun getTitleList(moYuTitleList: List<String>) {
        titleList.clear()

        titleList.addAll(moYuTitleList)
    }


}