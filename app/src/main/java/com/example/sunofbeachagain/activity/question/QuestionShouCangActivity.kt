package com.example.sunofbeachagain.activity.question

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.QuestionListAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityQuestionShouCangBinding
import com.example.sunofbeachagain.domain.bean.QuestionData
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.view.SobDialog
import com.example.sunofbeachagain.viewmodel.QuestionShouCangViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class QuestionShouCangActivity : BaseActivityViewModel<QuestionShouCangViewModel>() {
    private val questionShouCangBinding by lazy {
        ActivityQuestionShouCangBinding.inflate(layoutInflater)
    }

    override fun getSuccessView(): View {
        return questionShouCangBinding.root
    }

    private val questionListAdapter by lazy {
        QuestionListAdapter()
    }

    private val sobDialog by lazy {
        SobDialog(this)
    }

    override fun initView() {
        setToolBarTheme(true, null, false)

        setMyTAG(this::class.simpleName)

        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)

        currentViewModel?.queryQuestionList(this@QuestionShouCangActivity)

        questionShouCangBinding.questionShoucangRv.adapter = questionListAdapter
    }


    override fun initListener() {
        super.initListener()
        questionShouCangBinding.apply {
            questionShoucangToolbar.setNavigationOnClickListener {
                finish()
            }

            questionShoucangToolbar.setOnMenuItemClickListener {

                when (it.itemId) {

                    R.id.menu_question_shoucang_clean -> {
                        sobDialog.setMsgText("真的要清空所有收藏吗?","确定")

                        sobDialog.setOnDialogCleanShouCangConfirmClick(object :SobDialog.OnDialogCleanShouCangConfirmClick{
                            override fun onDialogCleanShouCangClick() {
                                currentViewModel?.clearQuestion()
                                currentViewModel?.queryQuestionList(this@QuestionShouCangActivity)
                            }
                        })

                        sobDialog.show()
                    }
                }

                true
            }

            val menu = questionShoucangToolbar.menu

            val searchItem = menu.findItem(R.id.menu_question_shoucang_search)

            val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    if (query != null) {
                        if (query.isEmpty()){
                            currentViewModel?.queryQuestionList(this@QuestionShouCangActivity)
                        }else{
                            currentViewModel?.querySingleQuestion(query,this@QuestionShouCangActivity)
                        }

                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {


                    if (newText.isNullOrEmpty()) {
                        currentViewModel?.queryQuestionList(this@QuestionShouCangActivity)
                    }

                    return true
                }

            })
        }

        loginViewModel.checkTokenResultLiveData.observe(this) {tokenBean->
            questionListAdapter.setOnQuestionListItemClickListener(object :
                QuestionListAdapter.OnQuestionListItemClickListener {
                override fun onQuestionListItemClick(wendaId: String) {
                    if (tokenBean != null) {
                        val intent = Intent(this@QuestionShouCangActivity, QuestionDetailActivity::class.java)

                        intent.putExtra(Constant.SOB_TOKEN, tokenBean.token)
                        intent.putExtra(Constant.SOB_QUESTION_ID, wendaId)

                        startActivity(intent)

                    }
                }

                override fun onQuestionListAvatarClick(userId: String) {

                        val intent = Intent(this@QuestionShouCangActivity, UserCenterActivity::class.java)

                        if (tokenBean != null) {
                            intent.putExtra(Constant.SOB_TOKEN, tokenBean.token)
                        }

                        intent.putExtra(Constant.SOB_USER_ID, userId)

                        registerForActivityResult.launch(intent)

                }

                override fun onQuestionShouCangClick(questionData: QuestionData) {
                    sobDialog.setMsgText("真的要删除收藏吗?","确定")

                    sobDialog.setOnDialogDeleteShouCangConfirmClick(object :SobDialog.OnDialogDeleteShouCangConfirmClick{
                        override fun onDialogDeleteShouCangClick() {
                            currentViewModel?.deleteQuestion(questionData.id)
                            currentViewModel?.queryQuestionList(this@QuestionShouCangActivity)
                        }

                    })

                    sobDialog.show()
                }

            })
        }


    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel?.queryQuestionLiveData?.observe(this) {
            lifecycleScope.launchWhenCreated {
                it.collectLatest {
                    questionListAdapter.submitData(it)
                }
            }
        }
    }

    override fun getCurrentViewModel() = QuestionShouCangViewModel::class.java
}