package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.sunofbeachagain.databinding.ItemMessageSystemBinding
import com.example.sunofbeachagain.domain.bean.MessageSystemData
import com.example.sunofbeachagain.utils.StringUtil

class MessageSystemAdapter : PagingDataAdapter<MessageSystemData, InnerHolder>(object :
    DiffUtil.ItemCallback<MessageSystemData>() {
    override fun areItemsTheSame(oldItem: MessageSystemData, newItem: MessageSystemData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MessageSystemData,
        newItem: MessageSystemData,
    ): Boolean {
        return oldItem == newItem
    }

}) {
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemMessageSystemBinding = holder.dataBinding as ItemMessageSystemBinding

        itemMessageSystemBinding.apply {
            getItem(position)?.let { messageSystemData ->

                itemMessageSystemTime.text = messageSystemData.publishTime

                itemMessageSystemContent.text =
                    StringUtil.stripHtml(messageSystemData.content.trim())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemMessageSystemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }
}