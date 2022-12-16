package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sunofbeachagain.databinding.ItemMoyuCommentSubBinding
import com.example.sunofbeachagain.domain.ItemMoYuCommentSubData
import com.example.sunofbeachagain.domain.bean.SubComment

class MoYuCommentSubAdapter : RecyclerView.Adapter<InnerHolder>() {
    private lateinit var onMoYuCommentSubCommentClickListener: OnMoYuCommentSubCommentClickListener

    private lateinit var onMoYuCommentSubClickListener: OnMoYuCommentSubClickListener

    private val subList = mutableListOf<SubComment>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemMoyuCommentSubBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemMoyuCommentSubBinding = holder.dataBinding as ItemMoyuCommentSubBinding

        val subComment = subList[position]

        itemMoyuCommentSubBinding.moYuCommentSub = ItemMoYuCommentSubData(subComment.nickname,
            subComment.targetUserNickname,
            subComment.content)

        itemMoyuCommentSubBinding.root.setOnClickListener {
            if (this::onMoYuCommentSubClickListener.isInitialized) {
                onMoYuCommentSubClickListener.onMoYuCommentSubClick()
            }
        }

        itemMoyuCommentSubBinding.itemMoyuSubCommentCommentContainer.setOnClickListener {
            if (this@MoYuCommentSubAdapter::onMoYuCommentSubCommentClickListener.isInitialized) {
                onMoYuCommentSubCommentClickListener.onMoYuCommentSubCommentClick(subComment.userId,subComment.commentId,subComment.nickname)
            }
        }
    }

    override fun getItemCount() = subList.size

    fun setData(subComments: List<SubComment>) {
        subList.clear()

        if (!subComments.isNullOrEmpty()) {
            subList.addAll(subComments)
        }
        notifyDataSetChanged()

    }

    fun setOnMoYuCommentSubClickListener(onMoYuCommentSubClickListener: OnMoYuCommentSubClickListener) {
        this.onMoYuCommentSubClickListener = onMoYuCommentSubClickListener
    }

    interface OnMoYuCommentSubClickListener {
        fun onMoYuCommentSubClick()
    }

    fun setOnMoYuCommentSubCommentClickListener(onMoYuCommentSubCommentClickListener: OnMoYuCommentSubCommentClickListener) {
        this.onMoYuCommentSubCommentClickListener = onMoYuCommentSubCommentClickListener
    }

    interface OnMoYuCommentSubCommentClickListener {
        fun onMoYuCommentSubCommentClick(targetUserId:String,commentId:String,targetUserName:String)
    }
}