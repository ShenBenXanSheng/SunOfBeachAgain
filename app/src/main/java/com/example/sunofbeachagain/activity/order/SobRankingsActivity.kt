package com.example.sunofbeachagain.activity.order

import android.view.View
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.adapter.SobRankingAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityRankingsBinding
import com.example.sunofbeachagain.viewmodel.RankingsViewModel

class SobRankingsActivity : BaseActivityViewModel<RankingsViewModel>() {
    private lateinit var activityRankingsBinding: ActivityRankingsBinding

    private val sobRankingAdapter by lazy {
        SobRankingAdapter()
    }

    override fun getSuccessView(): View {
        activityRankingsBinding = ActivityRankingsBinding.inflate(layoutInflater)
        return activityRankingsBinding.root
    }

    override fun initView() {

        currentViewModel?.getSobRankingsData()

        setToolBarTheme("积分榜", R.mipmap.back, R.color.mainColor, R.color.white, false)

        activityRankingsBinding.apply {
            rankingsRv.adapter = sobRankingAdapter
        }
    }

    override fun initListener() {
        super.initListener()

    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel?.apply {
            sobRankingsLiveData.observe(this@SobRankingsActivity) {
                sobRankingAdapter.setData(it)
            }

            sobRankingsLoadStateLiveData.observe(this@SobRankingsActivity) {
                when (it) {
                    RankingsViewModel.SobRankingsLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.LOADING)
                    }

                    RankingsViewModel.SobRankingsLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
                    }

                    RankingsViewModel.SobRankingsLoadStatus.EMPTY -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.EMPTY)
                    }

                    RankingsViewModel.SobRankingsLoadStatus.ERROR -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.ERROR)
                    }
                }
            }

        }
    }

    override fun errorReload() {
        super.errorReload()
        currentViewModel?.getSobRankingsData()
    }


    override fun getCurrentViewModel() = RankingsViewModel::class.java
}