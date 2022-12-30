package com.example.sunofbeachagain.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.order.HomeActivity
import com.example.sunofbeachagain.activity.question.QuestionDetailActivity
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.QuestionListAdapter
import com.example.sunofbeachagain.adapter.QuestionRankingsAdapter
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentQuestionBinding
import com.example.sunofbeachagain.databinding.FragmentQuestionSuccessBinding
import com.example.sunofbeachagain.domain.bean.QuestionData
import com.example.sunofbeachagain.repository.QuestionListRepository
import com.example.sunofbeachagain.room.QuestionEntity
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.StatusBarHeightUtil
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.view.QuestionPopupWindow
import com.example.sunofbeachagain.view.SobDialog
import com.example.sunofbeachagain.viewmodel.QuestionViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.flow.collectLatest


class QuestionFragment : BaseFragmentViewModel<HomeActivity, QuestionViewModel>() {

    private var state: String = "lastest"
    private lateinit var fragmentQuestionBinding: FragmentQuestionBinding

    private lateinit var fragmentQuestionSuccessBinding: FragmentQuestionSuccessBinding


    override fun setRootView(): View {
        fragmentQuestionBinding = FragmentQuestionBinding.inflate(layoutInflater)
        return fragmentQuestionBinding.root
    }

    override fun getSuccessView(): View {
        fragmentQuestionSuccessBinding = FragmentQuestionSuccessBinding.inflate(layoutInflater)
        return fragmentQuestionSuccessBinding.root
    }

    private val questionPopupWindow by lazy {
        QuestionPopupWindow(currentActivity)
    }

    private val questionListAdapter by lazy {
        QuestionListAdapter()
    }

    private val questionRankingsAdapter by lazy {
        QuestionRankingsAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    private val sobDialog by lazy {
        SobDialog(currentActivity)
    }

    private val questionShouCangIdList by lazy {

        mutableListOf<String>()
    }

    private var homeActivity: HomeActivity? = null

    override fun initView() {

        homeActivity = requireActivity() as HomeActivity

        currentViewModel.getQuestionList(state)

        fragmentQuestionBinding.apply {
            fragmentQuestionSuccessBinding.apply {
                questionRv.adapter = questionListAdapter.withLoadStateFooter(loadMoreAdapter)

                questionRankingsRv.adapter = questionRankingsAdapter

                questionSmartRefresh.setRefreshHeader(MaterialHeader(currentActivity))
            }
        }

        for (i in 0..100){
            questionShouCangIdList.add("1")
        }

    }


    override fun initListener() {
        super.initListener()
        fragmentQuestionBinding.apply {
            questionPopupWindow.apply {
                fragmentQuestionSuccessBinding.apply {

                    questionTbContentContainer.setOnClickListener {

                        if (isShowing) {
                            dismiss()
                        } else {
                            questionTbIv.setImageResource(R.mipmap.xiangshang)
                            setWindowAlpha(0.8f)
                            isFocusable = true
                            isTouchable = true
                            questionPopupWindow.showAtLocation(it,
                                Gravity.TOP,
                                0,
                                questionToolbar.measuredHeight + StatusBarHeightUtil.getStatusBarHeight(
                                    currentActivity))

                        }
                    }

                    setOnDismissListener {
                        questionTbIv.setImageResource(R.mipmap.xiangxia)
                        setWindowAlpha(1f)

                    }

                    setOnPopItemClickListener(object : QuestionPopupWindow.OnPopItemClickListener {
                        override fun onPopItemClick(state: String, title: String) {
                            this@QuestionFragment.state = state
                            if (state != "排行") {
                                currentViewModel.getQuestionList(state)
                                questionSmartRefresh.visibility = View.VISIBLE
                                questionRankingsRv.visibility = View.GONE
                            } else {
                                questionSmartRefresh.visibility = View.GONE
                                questionRankingsRv.visibility = View.VISIBLE
                                currentViewModel.getQuestionRankingsList()
                            }
                            questionTbTv.text = title
                        }

                    })
                }

                fragmentQuestionSuccessBinding.questionSmartRefresh.setOnRefreshListener {
                    currentViewModel.getQuestionList(state)
                    it.finishRefresh()

                }

            }

            loginViewModel.checkTokenResultLiveData.observe(this@QuestionFragment) { tokenBean ->
                questionListAdapter.setOnQuestionListItemClickListener(object
                    : QuestionListAdapter.OnQuestionListItemClickListener {
                    override fun onQuestionListItemClick(wendaId: String) {
                        if (tokenBean != null) {
                            val intent = Intent(currentActivity, QuestionDetailActivity::class.java)

                            intent.putExtra(Constant.SOB_TOKEN, tokenBean.token)
                            intent.putExtra(Constant.SOB_QUESTION_ID, wendaId)

                            registerForActivityResult.launch(intent)

                        }
                    }

                    override fun onQuestionListAvatarClick(userId: String) {
                        val intent = Intent(currentActivity, UserCenterActivity::class.java)

                        if (tokenBean != null) {
                            intent.putExtra(Constant.SOB_TOKEN, tokenBean.token)
                        }

                        intent.putExtra(Constant.SOB_USER_ID, userId)

                        registerForActivityResult.launch(intent)
                    }

                    override fun onQuestionShouCangClick(questionData: QuestionData) {
                        questionData.apply {
                            val questionEntity = QuestionEntity(id,
                                userId,
                                avatar,
                                nickname,
                                createTime,
                                title,
                                viewCount,
                                thumbUp,
                                sob,
                                answerCount)


                            if (questionShouCangIdList.isNullOrEmpty()) {
                                sobDialog.setMsgText("确定要收藏吗?","收藏")

                                shouCangQuestion(questionEntity)

                            }else{
                                if (questionShouCangIdList.contains(questionEntity.wendaId)) {
                                    sobDialog.setMsgText("已经收藏过了","好吧")
                                }else{
                                    sobDialog.setMsgText("确定要收藏吗?","收藏")
                                    shouCangQuestion(questionEntity)
                                }
                            }

                            sobDialog.show()
                        }

                    }
                })

                questionRankingsAdapter.setOnQuestionRankingsClickListener(object :
                    QuestionRankingsAdapter.OnQuestionRankingsClickListener {
                    override fun onQuestionRankingsAvatarClickListener(userId: String) {
                        val intent = Intent(currentActivity, UserCenterActivity::class.java)

                        if (tokenBean != null) {
                            intent.putExtra(Constant.SOB_TOKEN, tokenBean.token)
                        }

                        intent.putExtra(Constant.SOB_USER_ID, userId)

                        registerForActivityResult.launch(intent)
                    }

                })
            }

            doubleClickBackTop()
        }
    }

    private fun shouCangQuestion(questionEntity: QuestionEntity) {
        sobDialog.setOnDialogShouCangConfirmClick(object : SobDialog.OnDialogShouCangConfirmClick {
            override fun onDialogShouCangConfirmClick() {
                currentViewModel.insertQuestionEntity(questionEntity)
            }
        })
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.apply {
            questionListLiveData.observe(this@QuestionFragment) {
                lifecycleScope.launchWhenCreated {
                    it.collectLatest {
                        questionListAdapter.submitData(it)
                    }
                }
            }

            questionRankingsLiveData.observe(this@QuestionFragment) {
                questionRankingsAdapter.setData(it)
            }

            questionListRepository.questionListLoadState.observe(this@QuestionFragment) {
                when (it) {
                    QuestionListRepository.QuestionListLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.LOADING)
                    }

                    QuestionListRepository.QuestionListLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
                    }

                    QuestionListRepository.QuestionListLoadStatus.EMPTY -> {
                        ToastUtil.setText("没有更多内容了")
                    }

                    QuestionListRepository.QuestionListLoadStatus.ERROR -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.ERROR)
                    }

                    else -> {}
                }
            }

            questionRankingsLoadStateLiveData.observe(this@QuestionFragment) {
                when (it) {
                    QuestionViewModel.QuestionRankingsLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.LOADING)
                    }

                    QuestionViewModel.QuestionRankingsLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
                    }

                    QuestionViewModel.QuestionRankingsLoadStatus.EMPTY -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.EMPTY)
                    }

                    QuestionViewModel.QuestionRankingsLoadStatus.ERROR -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.ERROR)
                    }

                    else -> {}
                }
            }

            queryQuestion().observe(this@QuestionFragment){
                if (it.size>questionShouCangIdList.size){
                    ToastUtil.setText("收藏成功!")
                }
                questionShouCangIdList.clear()
                it.forEach { data->
                    questionShouCangIdList.add(data.wendaId)
                }

            }
        }


    }

    override fun errorReLoad() {
        super.errorReLoad()
        currentViewModel.getQuestionList(state)
    }

    override fun getMyCurrentActivity(): HomeActivity {
        return requireActivity() as HomeActivity
    }

    override fun getCurrentViewModel() = QuestionViewModel::class.java


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        homeActivity = if (hidden) {
            null
        } else {
            requireActivity() as HomeActivity
        }

        if (!hidden) {
            doubleClickBackTop()
        }
    }

    private fun doubleClickBackTop() {
        homeActivity?.setOnHomeDoubleClickListener(object : HomeActivity.OnHomeDoubleClickListener {
            override fun onHomeDoubleClick() {
                fragmentQuestionSuccessBinding.questionRv.smoothScrollToPosition(0)
            }

        })
    }


    private fun setWindowAlpha(alpha: Float) {
        val attributes = currentActivity.window.attributes

        attributes.alpha = alpha

        currentActivity.window.attributes = attributes
    }


}