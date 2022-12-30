package com.example.sunofbeachagain.activity.question

import android.util.Log
import android.view.View
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityQuestionCommentBinding
import com.example.sunofbeachagain.domain.bean.WenDaDetailSubCommentData
import com.example.sunofbeachagain.domain.body.QuestionCommentBody
import com.example.sunofbeachagain.domain.body.QuestionSubCommentBody
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.QuestionCommentViewModel

class QuestionCommentActivity : BaseActivityViewModel<QuestionCommentViewModel>() {
    private lateinit var activityQuestionCommentBinding: ActivityQuestionCommentBinding

    override fun getSuccessView(): View {
        activityQuestionCommentBinding = ActivityQuestionCommentBinding.inflate(layoutInflater)
        return activityQuestionCommentBinding.root
    }

    private var questionId = ""

    private lateinit var questionCommentData: WenDaDetailSubCommentData

    override fun initView() {
        intent.getStringExtra(Constant.SOB_QUESTION_ID)?.let {
            questionId = it
        }
        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
        setMyTAG(this::class.simpleName)
        setToolBarTheme(true, null, false)

        intent.getSerializableExtra(Constant.SOB_QUESTION_SUB_COMMENT)?.let {
            questionCommentData = it as WenDaDetailSubCommentData
        }

        if (this::questionCommentData.isInitialized) {
            activityQuestionCommentBinding.questionDetailCommentMsg.text =
                "回复${questionCommentData.beNickname}:"
        }
    }

    override fun initListener() {
        super.initListener()
        activityQuestionCommentBinding.apply {
            questionCommentToolbar.setNavigationOnClickListener {
                finish()
            }

            questionCommentPublish.setOnClickListener {
                val edText = questionCommentEt.text.toString().trim()
                if (!edText.isNullOrEmpty()) {
                    if (this@QuestionCommentActivity::questionCommentData.isInitialized) {
                       // Log.d("TAG", questionCommentData.toString())

                        val questionSubCommentBody = QuestionSubCommentBody(edText,
                            questionCommentData.parentId,
                            questionCommentData.beNickname,
                            questionCommentData.beUid,
                            questionCommentData.wendaId)

                        currentViewModel?.postQuestionSubComment(firsttoken,questionSubCommentBody)
                    } else {
                        val questionCommentBody = QuestionCommentBody(questionId, edText)
                        currentViewModel?.postQuestionComment(firsttoken, questionCommentBody)
                        // Log.d("TAG", questionId.toString())
                    }
                } else {
                    ToastUtil.setText("说点什么吧")
                }

            }
        }
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel?.apply {
            questionCommentLiveData.observe(this@QuestionCommentActivity){
                if (it.success){
                    val intent = intent

                    intent.putExtra(Constant.SOB_QUESTION_COMMENT_RESULT,"问答评论")

                    setResult(RESULT_OK,intent)

                    finish()
                }

                ToastUtil.setText(it.message)
            }
        }
    }

    override fun getCurrentViewModel() = QuestionCommentViewModel::class.java
}