package com.example.sunofbeachagain.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sunofbeachagain.databinding.ItemUserPriceBinding
import com.example.sunofbeachagain.domain.bean.Result

class UserPriceProvinceAdapter() : RecyclerView.Adapter<InnerHolder>() {

    private lateinit var onUserPriceClickListener: OnUserPriceClickListener

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
            if (this::onUserPriceClickListener.isInitialized) {
                onUserPriceClickListener.onUserPriceClick(result)
            }
        }

    }

    override fun getItemCount() = priceList.size

    fun setData(results: List<Result>) {
        priceList.clear()

        priceList.addAll(results)

        notifyDataSetChanged()
    }


    fun setOnUserPriceClickListener(onUserPriceClickListener: OnUserPriceClickListener) {
        this.onUserPriceClickListener = onUserPriceClickListener
    }

    interface OnUserPriceClickListener {
        fun onUserPriceClick(result: com.example.sunofbeachagain.domain.bean.Result)
    }


}