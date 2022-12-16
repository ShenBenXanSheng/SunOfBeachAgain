package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.databinding.ItemQuestionRankingsBinding
import com.example.sunofbeachagain.domain.bean.QuestionRankingsData

class QuestionRankingsAdapter : RecyclerView.Adapter<InnerHolder>() {
    private lateinit var onQuestionRankingsClickListener: OnQuestionRankingsClickListener
    private val rankingsList = mutableListOf<QuestionRankingsData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemQuestionRankingsBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val questionRankingsData = rankingsList[position]

        val itemQuestionRankingsBinding = holder.dataBinding as ItemQuestionRankingsBinding

        itemQuestionRankingsBinding.apply {
            val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))
            Glide.with(itemQuestionRankingsAvatar).load(questionRankingsData.avatar)
                .apply(requestOptions).into(itemQuestionRankingsAvatar)

            itemQuestionRankingsNickname.text = questionRankingsData.nickname

            itemQuestionRankingsCount.text = "回答数:${questionRankingsData.count}"

            itemQuestionRankingsAvatar.setOnClickListener {
                if (this@QuestionRankingsAdapter::onQuestionRankingsClickListener.isInitialized) {
                    onQuestionRankingsClickListener.onQuestionRankingsAvatarClickListener(
                        questionRankingsData.userId)
                }
            }
        }

    }

    override fun getItemCount() = rankingsList.size

    fun setData(it: List<QuestionRankingsData>?) {
        rankingsList.clear()
        if (it != null) {
            rankingsList.addAll(it)
        }
        notifyDataSetChanged()

    }

    fun setOnQuestionRankingsClickListener(onQuestionRankingsClickListener: OnQuestionRankingsClickListener) {
        this.onQuestionRankingsClickListener = onQuestionRankingsClickListener
    }

    interface OnQuestionRankingsClickListener {
        fun onQuestionRankingsAvatarClickListener(userId: String)
    }
}