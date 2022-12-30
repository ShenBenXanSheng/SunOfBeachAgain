package com.example.sunofbeachagain.activity.essay

import android.util.Log
import android.view.View
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityEssayDetailCommentBinding
import com.example.sunofbeachagain.domain.bean.WebSubCommentData
import com.example.sunofbeachagain.domain.body.EssayCommentBody
import com.example.sunofbeachagain.domain.body.EssaySubCommentBody
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.EssayCommentViewModel

class EssayCommentActivity : BaseActivityViewModel<EssayCommentViewModel>() {
    private lateinit var activityEssayDetailCommentBinding: ActivityEssayDetailCommentBinding

    private var currentEssayId = ""

    private lateinit var essaySubCommentData: WebSubCommentData

    private var isComment = false

    override fun getSuccessView(): View {
        activityEssayDetailCommentBinding =
            ActivityEssayDetailCommentBinding.inflate(layoutInflater)
        return activityEssayDetailCommentBinding.root
    }

    override fun initView() {
        setMyTAG(this::class.simpleName)

        setToolBarTheme(true, null, false)

        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)

        intent.getStringExtra(Constant.SOB_ESSAY_ID)?.let {
            currentEssayId = it
        }

        intent.getSerializableExtra(Constant.SOB_ESSAY_SUB_COMMENT)?.let {
            essaySubCommentData = (it as WebSubCommentData)
        }

        intent.getBooleanExtra(Constant.SOB_ESSAY_IS_COMMENT, false).let {
            isComment = it
        }


        if (this@EssayCommentActivity::essaySubCommentData.isInitialized) {
            activityEssayDetailCommentBinding.essayDetailCommentMsg.text = "回复${essaySubCommentData.beNickname}:"
        }
    }

    override fun initListener() {
        super.initListener()
        activityEssayDetailCommentBinding.apply {
            essayCommentToolbar.setNavigationOnClickListener {
                finish()
            }

            essayCommentPublish.setOnClickListener {
                val trim = essayCommentEt.text.toString().trim()
                if (!trim.isNullOrEmpty()) {
                    if (isComment) {
                        val essayCommentBody = EssayCommentBody("0", currentEssayId, trim)
                        currentViewModel?.postEssayComment(firsttoken, essayCommentBody)
                    } else {
                        if (this@EssayCommentActivity::essaySubCommentData.isInitialized) {
                            val essaySubCommentBody = EssaySubCommentBody(essaySubCommentData.articleId,
                                essaySubCommentData.parentId,
                                essaySubCommentData.beUid, essaySubCommentData.beNickname, trim)
                            currentViewModel?.postEssaySubComment(firsttoken, essaySubCommentBody)
                        }

                    }
                } else {
                    ToastUtil.setText("说点什么吧")
                }
            }
        }
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel?.postEssayCommentLiveData?.observe(this){
            if (it.success){
                val intent = intent
                intent.putExtra(Constant.SOB_ESSAY_COMMENT_RESULT,"完成")
                setResult(RESULT_OK,intent)
                finish()
            }
            ToastUtil.setText(it.message)
        }
    }

    override fun getCurrentViewModel() = EssayCommentViewModel::class.java
}