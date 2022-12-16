package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.sunofbeachagain.databinding.ItemShouCangBinding
import com.example.sunofbeachagain.domain.bean.ShouCangData

class UserShouCangAdapter :
    PagingDataAdapter<ShouCangData, InnerHolder>(object : DiffUtil.ItemCallback<ShouCangData>() {
        override fun areItemsTheSame(oldItem: ShouCangData, newItem: ShouCangData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShouCangData, newItem: ShouCangData): Boolean {
            return oldItem == newItem
        }

    }) {

    private var isShouCang: Boolean = false

    private lateinit var onShouCangItemClickListener: OnShouCangItemClickListener

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        (holder.dataBinding as ItemShouCangBinding).apply {
            getItem(position)?.let { shouCangData ->
                Glide.with(itemShouCangCover).load(shouCangData.cover).into(itemShouCangCover)

                itemShouCangName.text = shouCangData.name

                itemShouCangMsg.text = shouCangData.description

                root.setOnClickListener {
                    if (this@UserShouCangAdapter::onShouCangItemClickListener.isInitialized) {
                        onShouCangItemClickListener.onShouCangItemClick(shouCangData.id,
                            shouCangData.name)
                    }
                }

                if (isShouCang) {
                    itemShouCangIsShoucang.visibility = View.VISIBLE
                }

                itemShouCangIsShoucang.setOnClickListener {
                    if (this@UserShouCangAdapter::onShouCangItemClickListener.isInitialized) {
                        onShouCangItemClickListener.onShCangNewItemClick(shouCangData.id)
                    }
                }

            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemShouCangBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    fun setOnShouCangItemClickListener(onShouCangItemClickListener: OnShouCangItemClickListener) {
        this.onShouCangItemClickListener = onShouCangItemClickListener
    }

    fun getIsShouCang(booleanExtra: Boolean) {
        this.isShouCang = booleanExtra
    }

    interface OnShouCangItemClickListener {
        fun onShouCangItemClick(shouCangId: String, name: String)

        fun onShCangNewItemClick(shouCangId: String)
    }
}