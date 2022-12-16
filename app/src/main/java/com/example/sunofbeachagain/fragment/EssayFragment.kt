package com.example.sunofbeachagain.fragment

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.order.HomeActivity
import com.example.sunofbeachagain.adapter.EssayVpAdapter
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentEssayBinding
import com.example.sunofbeachagain.viewmodel.EssayViewModel
import com.google.android.material.tabs.TabLayoutMediator


class EssayFragment : BaseFragmentViewModel<HomeActivity, EssayViewModel>() {


    private lateinit var fragmentEssayBinding: FragmentEssayBinding

    override fun getSuccessView(): View {
        fragmentEssayBinding = FragmentEssayBinding.inflate(layoutInflater)
        return fragmentEssayBinding.root
    }

    private val TAG = "EssayFragment"

    private val essayVpAdapter by lazy {
        EssayVpAdapter(childFragmentManager, lifecycle)
    }

    private var homeActivity: HomeActivity? = null

    private lateinit var tabLayoutMediator: TabLayoutMediator
    override fun initView() {

        setToolBarTheme(false, R.color.mainColor, "文章", R.color.white)

        homeActivity = requireActivity() as HomeActivity

        currentViewModel.getEssayCategoryId()

        fragmentEssayBinding.apply {

            essayViewPage2.adapter = essayVpAdapter
        }
    }

    override fun initDataListener() {
        super.initDataListener()
        fragmentEssayBinding.apply {


            currentViewModel.apply {
                categoryDataLiveData.observe(this@EssayFragment) {
                    essayVpAdapter.setCategoryData(it)
                    tabLayoutMediator = TabLayoutMediator(essayTabLayout, essayViewPage2
                    ) { tab, position ->
                        val essayCategoryIdAndNameData = it[position]

                        val textView = TextView(currentActivity)

                        textView.text = essayCategoryIdAndNameData.name
                        textView.textSize = 14f
                        textView.gravity = Gravity.CENTER
                        textView.setTextColor(ContextCompat.getColor(currentActivity,
                            R.color.black))

                        tab.customView = textView
                    }
                    tabLayoutMediator.attach()


                    essayViewPage2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            for (i in it.indices) {
                                val tabAt = essayTabLayout.getTabAt(i)
                                if (tabAt != null) {
                                    val customView = tabAt.customView as TextView
                                    if (i == position) {
                                        customView.textSize = 16f
                                        customView.setTextColor(ContextCompat.getColor(
                                            currentActivity,
                                            R.color.mainColor))
                                    } else {
                                        customView.textSize = 14f
                                        customView.setTextColor(ContextCompat.getColor(
                                            currentActivity,
                                            R.color.black))
                                    }
                                }
                            }
                        }
                    })

                }
                essayCategoryLoadStateLiveData.observe(this@EssayFragment) {
                    when (it) {
                        EssayViewModel.EssayCategoryLoadStatus.LOADING -> {
                            switchDispatchLoadViewState(FragmentLoadViewStatus.LOADING)
                        }

                        EssayViewModel.EssayCategoryLoadStatus.SUCCESS -> {
                            switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
                        }

                        EssayViewModel.EssayCategoryLoadStatus.ERROR -> {
                            switchDispatchLoadViewState(FragmentLoadViewStatus.ERROR)
                        }

                        EssayViewModel.EssayCategoryLoadStatus.EMPTY -> {
                            switchDispatchLoadViewState(FragmentLoadViewStatus.EMPTY)
                        }

                        else -> {}
                    }
                }
            }

        }

        doubleClickBackTop()


    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        homeActivity = if (hidden) {
            null
        } else {
            requireActivity() as HomeActivity
        }

        if (!hidden) {
            doubleClickBackTop()
        }
    }

    private fun doubleClickBackTop() {
        homeActivity?.setOnHomeDoubleClickListener(object :
            HomeActivity.OnHomeDoubleClickListener {
            override fun onHomeDoubleClick() {
                childFragmentManager.fragments.forEach {
                    val essayListFragment = it as EssayListFragment
                    essayListFragment.fragmentEssayListBinding.essayListRv.smoothScrollToPosition(0)
                }

            }

        })
    }

    override fun errorReLoad() {
        super.errorReLoad()
        currentViewModel.getEssayCategoryId()
    }


    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }

    override fun initListener() {
        super.initListener()
    }

    override fun getMyCurrentActivity(): HomeActivity {
        return requireActivity() as HomeActivity
    }

    override fun getCurrentViewModel() = EssayViewModel::class.java


}