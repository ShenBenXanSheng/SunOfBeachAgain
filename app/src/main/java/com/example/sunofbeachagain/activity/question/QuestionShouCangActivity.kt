package com.example.sunofbeachagain.activity.question

import android.content.Intent
import android.util.Log
import android.view.View
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.QuestionShouCangAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityQuestionShouCangBinding
import com.example.sunofbeachagain.room.QuestionEntity
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.view.SobDialog
import com.example.sunofbeachagain.viewmodel.QuestionShouCangViewModel

class QuestionShouCangActivity : BaseActivityViewModel<QuestionShouCangViewModel>() {
    private val questionShouCangBinding by lazy {
        ActivityQuestionShouCangBinding.inflate(layoutInflater)
    }

    override fun getSuccessView(): View {
        return questionShouCangBinding.root
    }


    private val sobDialog by lazy {
        SobDialog(this)
    }

    private val questionShouCangAdapter by lazy {
        QuestionShouCangAdapter()
    }

    override fun initView() {
        setToolBarTheme(true, null, false)

        setMyTAG(this::class.simpleName)

        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)

        currentViewModel?.queryQuestionList()

        questionShouCangBinding.questionShoucangRv.adapter = questionShouCangAdapter
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
                        sobDialog.setMsgText("真的要清空所有收藏吗?", "确定")

                        sobDialog.setOnDialogCleanShouCangConfirmClick(object :
                            SobDialog.OnDialogCleanShouCangConfirmClick {
                            override fun onDialogCleanShouCangClick() {
                                currentViewModel?.clearQuestion()

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
                        currentViewModel?.querySearchQuestion(query)
                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {


                    if (newText.isNullOrEmpty()) {
                        currentViewModel?.queryQuestionList()?.observe(this@QuestionShouCangActivity){
                            questionShouCangAdapter.setData(it)
                        }
                    }

                    return true
                }

            })
        }

        loginViewModel.checkTokenResultLiveData.observe(this) { tokenBean ->
            questionShouCangAdapter.setOnQuestionListItemClickListener(object :QuestionShouCangAdapter.OnQuestionListItemClickListener{
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

                override fun onQuestionShouCangClick(questionData: QuestionEntity) {
                    sobDialog.setMsgText("真的要删除收藏吗?","确定")

                    sobDialog.setOnDialogDeleteShouCangConfirmClick(object :SobDialog.OnDialogDeleteShouCangConfirmClick{
                        override fun onDialogDeleteShouCangClick() {
                            currentViewModel?.deleteQuestion(questionData.wendaId)

                        }

                    })

                    sobDialog.show()
                }

            }

            )



        }


    }

    override fun initDataListener() {
        super.initDataListener()

        currentViewModel?.apply {
            queryQuestionList().observe(this@QuestionShouCangActivity){
                Log.d(TAG,it.toString())
                questionShouCangAdapter.setData(it)
            }

            questionEntitySearchLiveData.observe(this@QuestionShouCangActivity){
                questionShouCangAdapter.setData(it)
            }


        }



    }

    override fun getCurrentViewModel() = QuestionShouCangViewModel::class.java
}