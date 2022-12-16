package com.example.sunofbeachagain.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.databinding.PopQuestionBinding

class QuestionPopupWindow(val context: Context) :
    PopupWindow(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) {

    private lateinit var onPopItemClickListener: OnPopItemClickListener

    private val popQuestionBinding by lazy {
        PopQuestionBinding.inflate(LayoutInflater.from(context))
    }

    init {
        contentView = popQuestionBinding.root
        animationStyle = R.style.pop_anim

        isOutsideTouchable = true

        popQuestionBinding.popLastestQuestion.setTextColor(ContextCompat.getColor(context,
            R.color.mainColor))
        popQuestionBinding.initListener()
    }

    private fun PopQuestionBinding.initListener() {
        popLastestQuestion.setOnClickListener {
            setClickTextColor(popLastestQuestion)
            if (this@QuestionPopupWindow::onPopItemClickListener.isInitialized) {
                onPopItemClickListener.onPopItemClick("lastest", popLastestQuestion.text.toString())
            }
        }

        popNoAnswerQuestion.setOnClickListener {
            setClickTextColor(popNoAnswerQuestion)

            if (this@QuestionPopupWindow::onPopItemClickListener.isInitialized) {
                onPopItemClickListener.onPopItemClick("noanswer",
                    popNoAnswerQuestion.text.toString())
            }
        }

        popHotQuestion.setOnClickListener {
            setClickTextColor(popHotQuestion)

            if (this@QuestionPopupWindow::onPopItemClickListener.isInitialized) {
                onPopItemClickListener.onPopItemClick("hot", popHotQuestion.text.toString())
            }


        }

        popQuestionLeaderboard.setOnClickListener {
            setClickTextColor(popQuestionLeaderboard)

            if (this@QuestionPopupWindow::onPopItemClickListener.isInitialized) {
                onPopItemClickListener.onPopItemClick("排行", popQuestionLeaderboard.text.toString())
            }
        }
    }


    private fun setClickTextColor(textView: TextView) {
        popQuestionBinding.apply {
            popLastestQuestion.setTextColor(ContextCompat.getColor(context, R.color.black))
            popNoAnswerQuestion.setTextColor(ContextCompat.getColor(context, R.color.black))
            popHotQuestion.setTextColor(ContextCompat.getColor(context, R.color.black))
            popQuestionLeaderboard.setTextColor(ContextCompat.getColor(context, R.color.black))

            textView.setTextColor(ContextCompat.getColor(context, R.color.mainColor))
        }
        dismiss()
    }

    fun setOnPopItemClickListener(onPopItemClickListener: OnPopItemClickListener) {
        this.onPopItemClickListener = onPopItemClickListener
    }

    interface OnPopItemClickListener {
        fun onPopItemClick(state: String, title: String)
    }
}