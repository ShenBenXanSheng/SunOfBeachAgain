package com.example.sunofbeachagain.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.adapter.UserPriceCityAdapter
import com.example.sunofbeachagain.adapter.UserPriceDistrictAdapter
import com.example.sunofbeachagain.databinding.PopUserPriceBinding
import com.example.sunofbeachagain.domain.bean.Result
import com.example.sunofbeachagain.viewmodel.UserPriceViewModel

class UserPriceDistrictPopWindow(val cityId:String, val activity: AppCompatActivity, width: Int) :
    PopupWindow(width, ViewGroup.LayoutParams.WRAP_CONTENT) {

    private lateinit var onPopUserPriceCityClickListener: OnPopUserPriceDistrictClickListener

    private val popUserPriceBinding by lazy {
        PopUserPriceBinding.inflate(LayoutInflater.from(activity), null,false)
    }

    private val userPriceViewModel by lazy {
        ViewModelProvider(activity)[UserPriceViewModel::class.java]
    }

    private val userPriceProvinceAdapter by lazy {
        UserPriceDistrictAdapter(cityId)
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
            userPriceProvinceAdapter.setData(it.result[2])
        }

        userPriceProvinceAdapter.setOnUserPriceCityClickListener(object :UserPriceDistrictAdapter.OnUserPriceDistrictClickListener{
            override fun onUserPriceClick(result: Result) {
                if (this@UserPriceDistrictPopWindow::onPopUserPriceCityClickListener.isInitialized) {
                    onPopUserPriceCityClickListener.setOnPopUserPriceCityClick(result)
                }

                dismiss()
            }

        })
    }

    fun setOnPopUserPriceDistrictClickListener(onPopUserPriceCityClickListener: OnPopUserPriceDistrictClickListener){
        this.onPopUserPriceCityClickListener = onPopUserPriceCityClickListener
    }

    interface OnPopUserPriceDistrictClickListener{
        fun setOnPopUserPriceCityClick(result: com.example.sunofbeachagain.domain.bean.Result)
    }

}