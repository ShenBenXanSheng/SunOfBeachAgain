package com.example.sunofbeachagain.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sunofbeachagain.databinding.ItemUserPriceBinding
import com.example.sunofbeachagain.domain.bean.Result

class UserPriceCityAdapter(val cityId: String) : RecyclerView.Adapter<InnerHolder>() {

    private lateinit var onPopUserZhiXiaShiCLickListener: OnPopUserZhiXiaShiCLickListener

    private lateinit var onUserPriceClickListener: OnUserPriceCityClickListener

    private val priceList = mutableListOf<Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemUserPriceBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val result = priceList[position]

        val itemUserPriceBinding = holder.dataBinding as ItemUserPriceBinding


        itemUserPriceBinding.itemUserPriceTv.text = result.fullname


        itemUserPriceBinding.root.setOnClickListener {
            if (cityId == "11" || cityId == "12" || cityId == "31" || cityId == "50") {
                if (this::onPopUserZhiXiaShiCLickListener.isInitialized) {
                    onPopUserZhiXiaShiCLickListener.onPopZhiXiaShiClick(result)
                }
            }else{
                if (this::onUserPriceClickListener.isInitialized) {
                    onUserPriceClickListener.onUserPriceClick(result)
                }
            }

        }

    }

    override fun getItemCount() = priceList.size

    fun setData(results: List<Result>) {
        priceList.clear()

        results.forEach {
            if (it.id.substring(0, 2).contains(cityId)) {
                priceList.add(it)
            }

        }

        notifyDataSetChanged()
    }


    fun setOnUserPriceCityClickListener(onUserPriceClickListener: OnUserPriceCityClickListener) {
        this.onUserPriceClickListener = onUserPriceClickListener
    }

    interface OnUserPriceCityClickListener {
        fun onUserPriceClick(result: com.example.sunofbeachagain.domain.bean.Result)
    }

    fun setOnPopUserZhiXiaShiCLickListener(onPopUserZhiXiaShiCLickListener: OnPopUserZhiXiaShiCLickListener){
        this.onPopUserZhiXiaShiCLickListener = onPopUserZhiXiaShiCLickListener
    }

    interface OnPopUserZhiXiaShiCLickListener{
        fun onPopZhiXiaShiClick(result: com.example.sunofbeachagain.domain.bean.Result)
    }



}