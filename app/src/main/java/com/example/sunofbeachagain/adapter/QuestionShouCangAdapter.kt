package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.databinding.ItemQuestionListBinding
import com.example.sunofbeachagain.domain.ItemQuestionData
import com.example.sunofbeachagain.domain.bean.QuestionData
import com.example.sunofbeachagain.room.QuestionEntity

class QuestionShouCangAdapter : RecyclerView.Adapter<InnerHolder>() {
    private lateinit var onQuestionListItemClickListener: OnQuestionListItemClickListener

    private val questionShouCangList = mutableListOf<QuestionEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemQuestionListBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemQuestionListBinding = holder.dataBinding as ItemQuestionListBinding

        itemQuestionListBinding.apply {
            questionShouCangList.reversed()[position].let { questionData ->
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
                    if (this@QuestionShouCangAdapter::onQuestionListItemClickListener.isInitialized) {
                        onQuestionListItemClickListener.onQuestionListItemClick(questionData.wendaId)
                    }
                }

                root.setOnLongClickListener {
                    if (this@QuestionShouCangAdapter::onQuestionListItemClickListener.isInitialized) {
                        onQuestionListItemClickListener.onQuestionShouCangClick(questionData)
                    }
                    true
                }

                itemQuestionAvatar.setOnClickListener {
                    if (this@QuestionShouCangAdapter::onQuestionListItemClickListener.isInitialized) {
                        onQuestionListItemClickListener.onQuestionListAvatarClick(questionData.userId)
                    }
                }
            }
        }

    }

    override fun getItemCount() = questionShouCangList.size

    fun setData(it: List<QuestionEntity>) {
        questionShouCangList.clear()
        questionShouCangList.addAll(it)
        notifyDataSetChanged()
    }


    fun setOnQuestionListItemClickListener(onQuestionListItemClickListener: OnQuestionListItemClickListener) {
        this.onQuestionListItemClickListener = onQuestionListItemClickListener
    }

    interface OnQuestionListItemClickListener {
        fun onQuestionListItemClick(wendaId: String)

        fun onQuestionListAvatarClick(userId: String)

        fun onQuestionShouCangClick(questionData: QuestionEntity)
    }
}