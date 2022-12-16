package com.example.sunofbeachagain.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.databinding.ItemMoyuCommentBinding
import com.example.sunofbeachagain.domain.ItemMoYuCommentData
import com.example.sunofbeachagain.domain.bean.MoYuComment
import com.example.sunofbeachagain.utils.StringUtil
import java.text.SimpleDateFormat

class MoYuCommentAdapter :
    PagingDataAdapter<MoYuComment, InnerHolder>(object : DiffUtil.ItemCallback<MoYuComment>() {
        override fun areItemsTheSame(oldItem: MoYuComment, newItem: MoYuComment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MoYuComment, newItem: MoYuComment): Boolean {
            return oldItem == newItem
        }

    }) {
    private lateinit var onMoYuCommentCommentClickListener: OnMoYuCommentCommentClickListener

    private lateinit var onMoYuCommentClickListener: OnMoYuCommentItemClickListener

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private val oneDay = 86400000
    private val oneHour = 3600000

    override fun onBindViewHolder(
        holder: InnerHolder,
        @SuppressLint("RecyclerView") position: Int,
    ) {
        val itemMoyuCommentBinding = holder.dataBinding as ItemMoyuCommentBinding
        itemMoyuCommentBinding.apply {

            getItem(position)?.let {
                val userPosition = if (it.position.isNullOrBlank()) {
                    "刁民"
                } else {
                    it.position
                }

                itemMoyuCommentRv.visibility = if (it.subComments.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                val currentTime = System.currentTimeMillis()

                val dataTime = simpleDateFormat.parse(it.createTime)!!.time

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

                moYuCommentData = ItemMoYuCommentData(it.nickname,
                    userPosition,
                    StringUtil.stripHtml(it.content.trim()),
                    moYuTime)

                val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))
                Glide.with(itemMoyuCommentAvatar).load(it.avatar).apply(requestOptions)
                    .into(itemMoyuCommentAvatar)


                val moYuCommentSubAdapter = MoYuCommentSubAdapter()
                moYuCommentSubAdapter.setData(it.subComments)

                itemMoyuCommentRv.adapter = moYuCommentSubAdapter



                itemMoyuCommentCommentContainer.setOnClickListener { view ->
                    if (this@MoYuCommentAdapter::onMoYuCommentCommentClickListener.isInitialized) {
                        onMoYuCommentCommentClickListener.onMoYuCommentCommentClick(it.momentId,
                            it.userId,
                            it.id,
                            it.nickname)
                    }
                }

                moYuCommentSubAdapter.setOnMoYuCommentSubCommentClickListener(object :
                    MoYuCommentSubAdapter.OnMoYuCommentSubCommentClickListener {
                    override fun onMoYuCommentSubCommentClick(
                        targetUserId: String,
                        commentId: String,
                        targetUserName: String,
                    ) {
                        if (this@MoYuCommentAdapter::onMoYuCommentCommentClickListener.isInitialized) {
                            onMoYuCommentCommentClickListener.onMoYuCommentCommentClick(it.momentId,
                                targetUserId,
                                commentId,
                                targetUserName)
                        }
                    }


                })


                moYuCommentSubAdapter.setOnMoYuCommentSubClickListener(object :
                    MoYuCommentSubAdapter.OnMoYuCommentSubClickListener {
                    override fun onMoYuCommentSubClick() {
                        if (this@MoYuCommentAdapter::onMoYuCommentClickListener.isInitialized) {
                            onMoYuCommentClickListener.onMoYuCommentClick(it)
                        }
                    }

                })

                itemMoyuCommentBinding.root.setOnClickListener { view ->
                    if (this@MoYuCommentAdapter::onMoYuCommentClickListener.isInitialized) {
                        onMoYuCommentClickListener.onMoYuCommentClick(it)
                    }
                }

                itemMoyuCommentBinding.itemMoyuCommentAvatar.setOnClickListener {v->
                    if (this@MoYuCommentAdapter::onMoYuCommentClickListener.isInitialized) {
                        onMoYuCommentClickListener.onMoYuAvatarClick(it.userId)
                    }
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemMoyuCommentBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    fun setOnMoYuCommentItemClickListener(onMoYuCommentClickListener: OnMoYuCommentItemClickListener) {
        this.onMoYuCommentClickListener = onMoYuCommentClickListener
    }

    interface OnMoYuCommentItemClickListener {
        fun onMoYuCommentClick(moYuComment: MoYuComment)

        fun onMoYuAvatarClick(userId: String)
    }


    fun setOnMoYuCommentCommentClickListener(onMoYuCommentCommentClickListener: OnMoYuCommentCommentClickListener) {
        this.onMoYuCommentCommentClickListener = onMoYuCommentCommentClickListener
    }

    interface OnMoYuCommentCommentClickListener {
        fun onMoYuCommentCommentClick(
            momentId: String,
            targetUserId: String,
            commentId: String,
            targetUserName: String,
        )
    }


}