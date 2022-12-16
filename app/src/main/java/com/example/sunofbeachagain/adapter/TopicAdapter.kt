package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.databinding.ItemTopicBinding
import com.example.sunofbeachagain.domain.bean.TopicData

class TopicAdapter : RecyclerView.Adapter<InnerHolder>() {
    private lateinit var onTopicItemClickListener: OnTopicItemClickListener

    private val topicList = mutableListOf<TopicData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemTopicBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val topicData = topicList[position]

        val itemTopicBinding = holder.dataBinding as ItemTopicBinding

        val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))

        Glide.with(itemTopicBinding.topicIv).load(topicData.cover).apply(requestOptions)
            .into(itemTopicBinding.topicIv)

        itemTopicBinding.topicTv.text = topicData.topicName

        itemTopicBinding.root.setOnClickListener {
            if (this::onTopicItemClickListener.isInitialized) {
                onTopicItemClickListener.onTopicItemClick(topicData.id, topicData.topicName)
            }
        }
    }

    override fun getItemCount() = topicList.size

    fun setData(it: List<TopicData>?) {
        topicList.clear()
        if (it != null) {
            topicList.addAll(it)
        }

        notifyDataSetChanged()
    }

    fun setOnTopicItemClickListener(onTopicItemClickListener: OnTopicItemClickListener) {
        this.onTopicItemClickListener = onTopicItemClickListener
    }

    interface OnTopicItemClickListener {
        fun onTopicItemClick(topicId: String, topicName: String)
    }
}