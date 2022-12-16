package com.example.sunofbeachagain.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.databinding.ItemMoyuBannerBinding.inflate
import com.example.sunofbeachagain.domain.bean.MoYuBannerData

class MoYuListBannerAdapter(val moYuBannerList: List<MoYuBannerData>) : PagerAdapter() {


    private lateinit var onMoYuBannerClickListener: OnMoYuBannerClickListener

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layout = inflate(LayoutInflater.from(container.context))

        val moYuBannerData = moYuBannerList[position%moYuBannerList.size]


        Glide.with(layout.itemMoyuBannerIv).load(if (moYuBannerList.isEmpty()) {
            R.mipmap.paimeng
        } else {
            moYuBannerData.picUrl
        }).into(layout.itemMoyuBannerIv)

        layout.itemMoyuBannerIv.setOnClickListener {
            if (this::onMoYuBannerClickListener.isInitialized) {
                onMoYuBannerClickListener.onMoYuBannerClick(moYuBannerData.targetUrl)
            }
        }

        container.addView(layout.root)


        return layout.root

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }


    override fun getCount() = 1000

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    fun setOnMoYuBannerClickListener(onMoYuBannerClickListener: OnMoYuBannerClickListener){
        this.onMoYuBannerClickListener = onMoYuBannerClickListener
    }

    interface OnMoYuBannerClickListener{
        fun onMoYuBannerClick(url:String)
    }


}