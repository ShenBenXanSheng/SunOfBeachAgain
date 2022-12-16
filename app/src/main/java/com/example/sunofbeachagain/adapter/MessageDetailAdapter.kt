package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.activity.essay.EssayDetailActivity
import com.example.sunofbeachagain.activity.moyu.MoYuDetailActivity
import com.example.sunofbeachagain.activity.question.QuestionDetailActivity
import com.example.sunofbeachagain.databinding.ItemMessageDetailBinding
import com.example.sunofbeachagain.domain.MessageDetailData
import com.example.sunofbeachagain.utils.Constant

class MessageDetailAdapter : PagingDataAdapter<MessageDetailData, InnerHolder>(object :
    DiffUtil.ItemCallback<MessageDetailData>() {
    override fun areItemsTheSame(oldItem: MessageDetailData, newItem: MessageDetailData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MessageDetailData,
        newItem: MessageDetailData,
    ): Boolean {
        return oldItem == newItem
    }

}) {
    private var isRead = false

    private lateinit var onMessageDetailItemClickListener: OnMessageDetailItemClickListener

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemMessageDetailBinding = holder.dataBinding as ItemMessageDetailBinding
        itemMessageDetailBinding.apply {
            getItem(position)?.let { itemDetailData ->
                val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))
                Glide.with(itemMessageDetailAvatar).load(itemDetailData.avatar)
                    .apply(requestOptions).into(itemMessageDetailAvatar)
                messageDetailData = itemDetailData



                when (itemDetailData.readState) {
                    0 -> {
                        itemMessageDetailReadState.visibility = View.VISIBLE
                    }

                    1 -> {
                        itemMessageDetailReadState.visibility = View.GONE
                    }

                    2 -> {
                        itemMessageDetailReadState.visibility = View.GONE
                    }
                }



                root.setOnClickListener {
                    val activityClass = when (itemDetailData.type) {
                        "问答消息" -> {
                            QuestionDetailActivity::class.java
                        }

                        "点赞消息" -> {
                            null
                        }

                        "摸鱼消息" -> {
                            MoYuDetailActivity::class.java
                        }
                        else -> {
                            EssayDetailActivity::class.java
                        }
                    }

                    val intentKey = when (itemDetailData.type) {
                        "问答消息" -> {
                            Constant.SOB_QUESTION_ID
                        }

                        "点赞消息" -> {
                            null
                        }
                        "摸鱼消息" -> {
                            Constant.SOB_MOYU_ID
                        }

                        else -> {
                            Constant.SOB_ESSAY_ID
                        }
                    }

                    if (this@MessageDetailAdapter::onMessageDetailItemClickListener.isInitialized) {
                        itemMessageDetailReadState.visibility = View.GONE

                        onMessageDetailItemClickListener.onMessageDetailItemClick(activityClass,
                            intentKey,
                            itemDetailData.id, itemDetailData.isAiTe,itemDetailData.msgId)
                    }

                }

                itemMessageDetailAvatar.setOnClickListener {
                    if (this@MessageDetailAdapter::onMessageDetailItemClickListener.isInitialized) {
                        onMessageDetailItemClickListener.onMessageDetailAvatarClick(itemDetailData.userId)
                    }
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemMessageDetailBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    fun setOnMessageDetailItemClickListener(onMessageDetailItemClickListener: OnMessageDetailItemClickListener) {
        this.onMessageDetailItemClickListener = onMessageDetailItemClickListener
    }


    interface OnMessageDetailItemClickListener {
        fun onMessageDetailItemClick(
            activityClass: Class<*>?,
            intentKey: String?,
            exId: String,
            aiTe: Boolean,
            readId: String,
        )

        fun onMessageDetailAvatarClick(userId: String)
    }
}