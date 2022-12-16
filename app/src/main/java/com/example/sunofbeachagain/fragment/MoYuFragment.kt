package com.example.sunofbeachagain.fragment

import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.order.HomeActivity
import com.example.sunofbeachagain.adapter.MoYuVpAdapter
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentMoYuBinding
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.viewmodel.LoginViewModel
import com.example.sunofbeachagain.viewmodel.MoYuViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy


class MoYuFragment : BaseFragmentViewModel<HomeActivity,MoYuViewModel>() {
    private lateinit var fragmentMoYuBinding: FragmentMoYuBinding



    private val TAG = this::class.simpleName

    private lateinit var tabLayoutMediator: TabLayoutMediator

    private val moYuTitleList = arrayListOf("摸鱼","关注")

    private val moYuVpAdapter by lazy {
        MoYuVpAdapter(childFragmentManager,lifecycle)
    }

     var homeActivity: HomeActivity? = null

    override fun getSuccessView(): View {
        fragmentMoYuBinding = FragmentMoYuBinding.inflate(layoutInflater)
        return fragmentMoYuBinding.root
    }

    override fun initView() {
        homeActivity = requireActivity() as HomeActivity

        switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
        setToolBarTheme(true,null, null, null)

        moYuVpAdapter.getTitleList(moYuTitleList)

        fragmentMoYuBinding.apply {
            moyuVp2.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            moyuVp2.adapter = moYuVpAdapter
            moyuVp2.registerOnPageChangeCallback(pageChangeListener)

            tabLayoutMediator = TabLayoutMediator(moyuTabLayout,moyuVp2,object :TabConfigurationStrategy{
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    val textView = TextView(currentActivity)
                    textView.text = moYuTitleList[position]
                    textView.setTextColor(ContextCompat.getColor(currentActivity,R.color.white))
                    textView.textSize = 18f
                    tab.customView = textView
                }

            })
        }
        tabLayoutMediator.attach()
    }
    private val pageChangeListener = object :ViewPager2.OnPageChangeCallback(){
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int,
        ) {

        }

        override fun onPageSelected(position: Int) {

            for (i in 0.until(moYuTitleList.size)) {
                val tabAt = fragmentMoYuBinding.moyuTabLayout.getTabAt(i)
                val textView = tabAt?.customView as TextView
                if (i == position){
                    textView.textSize = 20f
                    textView.setTextColor(ContextCompat.getColor(currentActivity,R.color.black))
                    textView.gravity = Gravity.CENTER
                }else{
                    textView.setTextColor(ContextCompat.getColor(currentActivity,R.color.white))
                    textView.textSize = 16f
                    textView.gravity = Gravity.CENTER
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }

    }

    override fun initListener() {
        super.initListener()

    }

    override fun initDataListener() {
        super.initDataListener()

    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        homeActivity = if (hidden){
            null
        }else{
            requireActivity() as HomeActivity
        }



    }




    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
        fragmentMoYuBinding.moyuVp2.unregisterOnPageChangeCallback(pageChangeListener)
    }

    override fun getMyCurrentActivity() = requireActivity() as HomeActivity

    override fun getCurrentViewModel() = MoYuViewModel::class.java




}