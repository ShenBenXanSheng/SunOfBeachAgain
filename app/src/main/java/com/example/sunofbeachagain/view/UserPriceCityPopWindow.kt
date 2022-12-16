package com.example.sunofbeachagain.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.adapter.UserPriceCityAdapter
import com.example.sunofbeachagain.databinding.PopUserPriceBinding
import com.example.sunofbeachagain.domain.bean.Result
import com.example.sunofbeachagain.viewmodel.UserPriceViewModel

class UserPriceCityPopWindow(val cityId:String,val activity: AppCompatActivity, width: Int) :
    PopupWindow(width, ViewGroup.LayoutParams.WRAP_CONTENT) {

    private lateinit var onPopUserZhiXiaShiCLickListener: OnPopUserZhiXiaShiCLickListener

    private lateinit var onPopUserPriceCityClickListener: OnPopUserPriceCityClickListener

    private val popUserPriceBinding by lazy {
        PopUserPriceBinding.inflate(LayoutInflater.from(activity), null,false)
    }

    private val userPriceViewModel by lazy {
        ViewModelProvider(activity)[UserPriceViewModel::class.java]
    }

    private val userPriceProvinceAdapter by lazy {
        UserPriceCityAdapter(cityId)
    }

    init {
        isOutsideTouchable = true

        animationStyle = R.style.pop_anim

        contentView = popUserPriceBinding.root

        popUserPriceBinding.apply {
            initView()

            initListener()
        }

    }


    private fun PopUserPriceBinding.initView() {
        userPriceViewModel.getUserPrice()

        userPriceRv.adapter = userPriceProvinceAdapter
    }

    private fun PopUserPriceBinding.initListener() {
        userPriceViewModel.userPriceLiveData.observe(activity){
            userPriceProvinceAdapter.setData(it.result[1])
        }

        userPriceProvinceAdapter.setOnUserPriceCityClickListener(object :UserPriceCityAdapter.OnUserPriceCityClickListener{
            override fun onUserPriceClick(result: Result) {
                if (this@UserPriceCityPopWindow::onPopUserPriceCityClickListener.isInitialized) {
                    onPopUserPriceCityClickListener.setOnPopUserPriceCityClick(result)
                }

                dismiss()
            }
        })


        userPriceProvinceAdapter.setOnPopUserZhiXiaShiCLickListener(object :UserPriceCityAdapter.OnPopUserZhiXiaShiCLickListener{
            override fun onPopZhiXiaShiClick(result: Result) {
                if (this@UserPriceCityPopWindow::onPopUserZhiXiaShiCLickListener.isInitialized) {
                    onPopUserZhiXiaShiCLickListener.onZhiXiaShiClick(result)
                }

                dismiss()
            }

        })


    }

    fun setOnPopUserPriceCityClickListener(onPopUserPriceCityClickListener: OnPopUserPriceCityClickListener){
        this.onPopUserPriceCityClickListener = onPopUserPriceCityClickListener
    }

    interface OnPopUserPriceCityClickListener{
        fun setOnPopUserPriceCityClick(result: com.example.sunofbeachagain.domain.bean.Result)
    }


    fun setOnPopUserZhiXiaShiCLickListener(onPopUserZhiXiaShiCLickListener: OnPopUserZhiXiaShiCLickListener){
        this.onPopUserZhiXiaShiCLickListener = onPopUserZhiXiaShiCLickListener
    }

    interface OnPopUserZhiXiaShiCLickListener{
        fun onZhiXiaShiClick(result: com.example.sunofbeachagain.domain.bean.Result)
    }

}