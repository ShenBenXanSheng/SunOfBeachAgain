package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.databinding.ItemQuestionListBinding
import com.example.sunofbeachagain.domain.ItemQuestionData
import com.example.sunofbeachagain.domain.bean.QuestionData

class QuestionListAdapter :
    PagingDataAdapter<QuestionData, InnerHolder>(object : DiffUtil.ItemCallback<QuestionData>() {
        override fun areItemsTheSame(oldItem: QuestionData, newItem: QuestionData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: QuestionData, newItem: QuestionData): Boolean {
            return oldItem == newItem
        }

    }) {

    private lateinit var onQuestionListItemClickListener: OnQuestionListItemClickListener

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemQuestionListBinding = holder.dataBinding as ItemQuestionListBinding
        itemQuestionListBinding.apply {
            getItem(position)?.let { questionData ->
                val itemQuestionData = ItemQuestionData(questionData.nickname,
                    questionData.createTime,
                    questionData.title,
                    "浏览:${questionData.viewCount}",
                    "点赞:${questionData.thumbUp}",
                    "投币:${questionData.sob}",
                    "问答:${questionData.answerCount}")

                itemQuestionListBinding.questionData = itemQuestionData

                val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))

                Glide.with(itemQuestionAvatar).load(questionData.avatar).apply(requestOptions)
                    .into(itemQuestionAvatar)


                root.setOnClickListener {
                    if (this@QuestionListAdapter::onQuestionListItemClickListener.isInitialized) {
                        onQuestionListItemClickListener.onQuestionListItemClick(questionData.id)
                    }
                }

                itemQuestionAvatar.setOnClickListener {
                    if (this@QuestionListAdapter::onQuestionListItemClickListener.isInitialized) {
                        onQuestionListItemClickListener.onQuestionListAvatarClick(questionData.userId)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemQuestionListBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    fun setOnQuestionListItemClickListener(onQuestionListItemClickListener: OnQuestionListItemClickListener) {
        this.onQuestionListItemClickListener = onQuestionListItemClickListener
    }

    interface OnQuestionListItemClickListener {
        fun onQuestionListItemClick(wendaId: String)

        fun onQuestionListAvatarClick(userId:String)
    }
}