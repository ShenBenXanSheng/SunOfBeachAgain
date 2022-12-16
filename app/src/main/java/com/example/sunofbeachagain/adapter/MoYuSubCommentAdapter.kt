package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

import com.example.sunofbeachagain.databinding.ItemMoyuSubCommentBinding
import com.example.sunofbeachagain.domain.ItemSubCommentData
import com.example.sunofbeachagain.utils.StringUtil
import java.text.SimpleDateFormat

class MoYuSubCommentAdapter : RecyclerView.Adapter<InnerHolder>() {
    private lateinit var onMoYuSubCommentItemClickListener: OnMoYuSubCommentItemClickListener
    private val subCommentData = mutableListOf<ItemSubCommentData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemMoyuSubCommentBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private val oneDay = 86400000
    private val oneHour = 3600000

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemMoyuSubCommentBinding = holder.dataBinding as ItemMoyuSubCommentBinding
        subCommentData[position].let { itemSubCommentData ->
            val currentTime = System.currentTimeMillis()

            val dataTime = simpleDateFormat.parse(itemSubCommentData.createTime)!!.time

            val intervalTime = currentTime - dataTime

            val format = simpleDateFormat.format(dataTime)
            //大于一天
            val moYuTime = if (intervalTime >= oneDay * 2) {
                format

            } else if (intervalTime >= oneDay) {
                "昨天${
                    format.substring(format.indexOf(' '), format.length)
                }"

            } else if (intervalTime <= oneHour) {
                "${(intervalTime) / 60 / 1000}分钟前"

            } else {

                "${intervalTime / 60 / 60 / 1000}小时前"
            }

            itemMoyuSubCommentBinding.apply {
                subCommentData = ItemSubCommentData(itemSubCommentData.avatar,
                    itemSubCommentData.nickname,
                    if (itemSubCommentData.position.isNullOrEmpty()) {
                        "刁民"
                    } else {
                        itemSubCommentData.position
                    },
                    moYuTime,
                    itemSubCommentData.targetName,
                    StringUtil.stripHtml(itemSubCommentData.content.trim()),
                    itemSubCommentData.userId,
                    itemSubCommentData.momentId,
                    itemSubCommentData.commentId)

                val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))
                Glide.with(itemSubCommentAvatar)
                    .load(itemSubCommentData.avatar).apply(requestOptions)
                    .into(itemSubCommentAvatar)

                itemSubCommentAvatar.setOnClickListener {
                    if (this@MoYuSubCommentAdapter::onMoYuSubCommentItemClickListener.isInitialized) {
                        onMoYuSubCommentItemClickListener.onMoYuSubCommentItemClick(itemSubCommentData)
                    }
                }
            }


        }


    }

    override fun getItemCount() = subCommentData.size

    fun setData(it: List<ItemSubCommentData>?) {
        subCommentData.clear()
        if (it != null) {
            subCommentData.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun setOnMoYuSubCommentItemClickListener(onMoYuSubCommentItemClickListener: OnMoYuSubCommentItemClickListener){
        this.onMoYuSubCommentItemClickListener = onMoYuSubCommentItemClickListener
    }

    interface OnMoYuSubCommentItemClickListener{
        fun onMoYuSubCommentItemClick(itemSubCommentData: ItemSubCommentData)
    }
}