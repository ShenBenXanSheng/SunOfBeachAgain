package com.example.sunofbeachagain.view

import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.adapter.UserPriceProvinceAdapter
import com.example.sunofbeachagain.databinding.PopUserPriceBinding
import com.example.sunofbeachagain.domain.bean.Result
import com.example.sunofbeachagain.viewmodel.UserPriceViewModel

class UserPriceProvincePopWindow(val activity: AppCompatActivity, width: Int) :
    PopupWindow(width, LayoutParams.WRAP_CONTENT) {

    private lateinit var onUserPriceProvinceClickListener: OnUserPriceProvinceClickListener

    private val popUserPriceBinding by lazy {
        PopUserPriceBinding.inflate(LayoutInflater.from(activity), null)
    }

    private val userPriceViewModel by lazy {
        ViewModelProvider(activity)[UserPriceViewModel::class.java]
    }

    private val userPriceProvinceAdapter by lazy {
        UserPriceProvinceAdapter()
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
            if (!it.result.isNullOrEmpty()) {
                userPriceProvinceAdapter.setData(it.result[0])
            }

        }

       userPriceProvinceAdapter.setOnUserPriceClickListener(object :UserPriceProvinceAdapter.OnUserPriceClickListener{
           override fun onUserPriceClick(result: Result) {
               if (this@UserPriceProvincePopWindow::onUserPriceProvinceClickListener.isInitialized) {
                   onUserPriceProvinceClickListener.onUserPriceProvinceClick(result)
               }
               dismiss()
           }
       })
    }

    fun setOnUserPriceProvinceClickListener(onUserPriceProvinceClickListener: OnUserPriceProvinceClickListener){
        this.onUserPriceProvinceClickListener = onUserPriceProvinceClickListener
    }

    interface OnUserPriceProvinceClickListener{
        fun onUserPriceProvinceClick(result: com.example.sunofbeachagain.domain.bean.Result)
    }



}