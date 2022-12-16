package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.databinding.ItemEssayListBinding
import com.example.sunofbeachagain.domain.ItemEssayData
import com.example.sunofbeachagain.domain.bean.EssayList

class EssayListAdapter :
    PagingDataAdapter<EssayList, InnerHolder>(object : DiffUtil.ItemCallback<EssayList>() {
        override fun areItemsTheSame(oldItem: EssayList, newItem: EssayList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EssayList, newItem: EssayList): Boolean {
            return oldItem == newItem
        }

    }) {
    private lateinit var onEssayListItemClickListener: OnEssayListItemClickListener

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemEssayListBinding = holder.dataBinding as ItemEssayListBinding

        itemEssayListBinding.apply {
            getItem(position)?.let { essayList ->
                val itemEssayList = ItemEssayData(essayList.title,
                    if (essayList.nickName.isNullOrEmpty()) {
                        essayList.nickname
                    } else {
                        essayList.nickName
                    },
                    String.format("%.0f", essayList.viewCount),
                    String.format("%.0f", essayList.thumbUp),
                    essayList.createTime)

                essayData = itemEssayList
                val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(10))
                Glide.with(itemEssayAvatar).load(essayList.avatar).apply(requestOptions)
                    .into(itemEssayAvatar)

                Glide.with(itemEssayCover).load(if (essayList.covers.isEmpty()) {
                    R.mipmap.paimeng
                } else {
                    essayList.covers[0]
                }).into(itemEssayCover)

                root.setOnClickListener {
                    if (this@EssayListAdapter::onEssayListItemClickListener.isInitialized) {
                        onEssayListItemClickListener.onEssayListItemClick(essayList.id)
                    }
                }


                root.setOnLongClickListener {
                    if (this@EssayListAdapter::onEssayListItemClickListener.isInitialized) {
                        onEssayListItemClickListener.onShouCangLongClick(essayList.title,essayList.id)
                    }
                    true
                }
                itemEssayAvatar.setOnClickListener {
                    if (this@EssayListAdapter::onEssayListItemClickListener.isInitialized) {
                        onEssayListItemClickListener.onEssayListAvatarClick(essayList.userId)
                    }
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemEssayListBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    fun setOnEssayListItemClickListener(onEssayListItemClickListener: OnEssayListItemClickListener) {
        this.onEssayListItemClickListener = onEssayListItemClickListener
    }

    interface OnEssayListItemClickListener {
        fun onEssayListItemClick(essayId: String)

        fun onEssayListAvatarClick(userId: String)

        fun onShouCangLongClick(title:String,essayId: String)
    }
}