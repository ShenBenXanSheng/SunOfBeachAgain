package com.example.sunofbeachagain.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sunofbeachagain.domain.EssayCategoryIdAndNameData
import com.example.sunofbeachagain.fragment.EssayListFragment

class EssayVpAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val essayCategoryList = mutableListOf<EssayCategoryIdAndNameData>()

    override fun getItemCount() = essayCategoryList.size

    override fun createFragment(position: Int): Fragment {

       return if (essayCategoryList.isNotEmpty()) {
           val essayCategoryIdAndNameData = essayCategoryList[position]
           EssayListFragment(essayCategoryIdAndNameData)
        }else{
           EssayListFragment()
       }

    }




    fun setCategoryData(it: List<EssayCategoryIdAndNameData>?) {
        essayCategoryList.clear()
        if (it != null) {
            essayCategoryList.addAll(it)
        }

        notifyDataSetChanged()
    }
}