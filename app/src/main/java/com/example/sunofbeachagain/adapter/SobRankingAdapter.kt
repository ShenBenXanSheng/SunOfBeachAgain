package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.databinding.ItemSobRankingsBinding
import com.example.sunofbeachagain.domain.bean.SobRankingData

class SobRankingAdapter : RecyclerView.Adapter<InnerHolder>() {
    private val rankingsList = mutableListOf<SobRankingData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemSobRankingsBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemSobRankingsBinding = holder.dataBinding as ItemSobRankingsBinding

        val sobRankingData = rankingsList[position]

        itemSobRankingsBinding.apply {
            val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))

            Glide.with(itemSobRankingsAvatar).load(sobRankingData.avatar).apply(requestOptions)
                .into(itemSobRankingsAvatar)

            itemSobRankingsNickname.text = sobRankingData.nickname

            itemSobRankingsSobCount.text = String.format("%.0f",sobRankingData.sob)
        }
    }

    override fun getItemCount() = rankingsList.size

    fun setData(it: List<SobRankingData>?) {
        rankingsList.clear()
        if (it != null) {
            rankingsList.addAll(it)
        }

        notifyDataSetChanged()
    }
}