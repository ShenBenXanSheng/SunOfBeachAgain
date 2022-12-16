package com.example.sunofbeachagain.activity.message

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.MessageDetailAdapter
import com.example.sunofbeachagain.adapter.MessageSystemAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityMessageDetailBinding
import com.example.sunofbeachagain.repository.MessageDetailRepository
import com.example.sunofbeachagain.repository.MessageSystemDetailRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.MessageDetailViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.flow.collectLatest

class MessageDetailActivity : BaseActivityViewModel<MessageDetailViewModel>() {
    private lateinit var activityMessageDetailBinding: ActivityMessageDetailBinding

    override fun getSuccessView(): View {
        activityMessageDetailBinding = ActivityMessageDetailBinding.inflate(layoutInflater)

        return activityMessageDetailBinding.root
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }


    private var currentMessageState = ""
    private val messageDetailAdapter by lazy {
        MessageDetailAdapter()
    }

    private val messageSystemAdapter by lazy {
        MessageSystemAdapter()
    }

    override fun initView() {

        intent.getStringExtra(Constant.SOB_MESSAGE_STATE)?.let {
            currentMessageState = it
        }

        setToolBarTheme(currentMessageState, R.mipmap.back, R.color.mainColor, R.color.white, false)

        activityMessageDetailBinding.apply {
            messageDetailSmartRefresh.setRefreshHeader(MaterialHeader(this@MessageDetailActivity))
            if (currentMessageState == "系统消息") {
                activityMessageDetailBinding.rvControl(messageDetailSystemRv)
                currentViewModel?.getMessageSystemDetailData(firsttoken)

            } else {

                activityMessageDetailBinding.rvControl(messageDetailSmartRefresh)
                currentViewModel?.getMessageDetailData(firsttoken, currentMessageState)
            }

            messageDetailRv.adapter = messageDetailAdapter.withLoadStateFooter(loadMoreAdapter)

            messageDetailSystemRv.adapter =
                messageSystemAdapter.withLoadStateFooter(loadMoreAdapter)
        }

    }

    override fun initListener() {
        super.initListener()
        activityMessageDetailBinding.apply {
            messageDetailSmartRefresh.setOnRefreshListener {
                currentViewModel?.getMessageDetailData(firsttoken, currentMessageState)
                it.finishRefresh()
            }

            loginViewModel.checkTokenResultLiveData.observe(this@MessageDetailActivity) {

                messageDetailAdapter.setOnMessageDetailItemClickListener(object :
                    MessageDetailAdapter.OnMessageDetailItemClickListener {
                    override fun onMessageDetailItemClick(
                        activityClass: Class<*>?,
                        intentKey: String?,
                        exId: String,
                        aiTe: Boolean,
                        readId: String,
                    ) {

                        if (activityClass != null || intentKey != null) {
                            if (it != null) {
                                if (aiTe) {
                                    currentViewModel?.putAiTeMessage(it.token,readId)
                                }else{
                                    when (intentKey) {
                                        Constant.SOB_QUESTION_ID -> {
                                            currentViewModel?.putWenDaMessage(it.token,readId)
                                        }

                                        Constant.SOB_MOYU_ID -> {
                                            currentViewModel?.putMoYuMessage(it.token,readId)
                                        }

                                        Constant.SOB_ESSAY_ID -> {

                                        }
                                    }
                                }
                            }


                            val intent = Intent(this@MessageDetailActivity, activityClass)
                            intent.putExtra(Constant.SOB_TOKEN, firsttoken)
                            intent.putExtra(intentKey, exId)

                            registerForActivityResult.launch(intent)
                        }
                    }

                    override fun onMessageDetailAvatarClick(userId: String) {
                        val intent =
                            Intent(this@MessageDetailActivity, UserCenterActivity::class.java)
                        intent.putExtra(Constant.SOB_USER_ID, userId)
                        if (it != null) {
                            intent.putExtra(Constant.SOB_TOKEN, it.token)
                        }
                        registerForActivityResult.launch(intent)

                    }

                })

            }
        }
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel?.apply {
            messageDetailDataLiveData.observe(this@MessageDetailActivity) {
                lifecycleScope.launchWhenCreated {
                    it.collectLatest {
                        messageDetailAdapter.submitData(it)
                    }
                }
            }

            messageDetailRepository.messageDetailLoadStateLiveData.observe(this@MessageDetailActivity) {
                when (it) {
                    MessageDetailRepository.MessageDetailLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.LOADING)
                    }

                    MessageDetailRepository.MessageDetailLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
                    }

                    MessageDetailRepository.MessageDetailLoadStatus.EMPTY -> {
                        ToastUtil.setText("没有更多内容了")
                    }

                    MessageDetailRepository.MessageDetailLoadStatus.ERROR -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.ERROR)
                    }
                }
            }

            messageSystemDetailLiveData.observe(this@MessageDetailActivity) {
                lifecycleScope.launchWhenCreated {
                    it.collectLatest {
                        messageSystemAdapter.submitData(it)
                    }
                }
            }

            messageSystemDetailRepository.messageSystemDetailLoadStateLiveData.observe(this@MessageDetailActivity) {
                when (it) {
                    MessageSystemDetailRepository.MessageSystemLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.LOADING)
                    }

                    MessageSystemDetailRepository.MessageSystemLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
                    }

                    MessageSystemDetailRepository.MessageSystemLoadStatus.EMPTY -> {
                        ToastUtil.setText("没有更多内容了")
                    }

                    MessageSystemDetailRepository.MessageSystemLoadStatus.ERROR -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.ERROR)
                    }
                }
            }

            putAiTeMessageRequestLiveData.observe(this@MessageDetailActivity){



            }

            putWenDaMessageRequestLiveData.observe(this@MessageDetailActivity){

            }

            putMoYuMessageRequestLiveData.observe(this@MessageDetailActivity){

            }
        }
    }


    private fun ActivityMessageDetailBinding.rvControl(view: View) {
        messageDetailSmartRefresh.visibility = View.GONE
        messageDetailSystemRv.visibility = View.GONE

        view.visibility = View.VISIBLE
    }

    override fun getCurrentViewModel() = MessageDetailViewModel::class.java
}