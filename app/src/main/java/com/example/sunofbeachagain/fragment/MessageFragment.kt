package com.example.sunofbeachagain.fragment

import android.content.Intent
import android.util.Log
import android.view.View
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.message.MessageDetailActivity
import com.example.sunofbeachagain.activity.order.HomeActivity
import com.example.sunofbeachagain.base.BaseFragment
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentMessageBinding
import com.example.sunofbeachagain.domain.CheckTokenDataHasToken
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.MessageViewModel


class MessageFragment : BaseFragmentViewModel<HomeActivity, MessageViewModel>() {


    private lateinit var fragmentMessageBinding: FragmentMessageBinding

    private val TAG = "MessageFragment"
    override fun getSuccessView(): View {
        fragmentMessageBinding = FragmentMessageBinding.inflate(layoutInflater)
        return fragmentMessageBinding.root
    }

    override fun initView() {
        switchDispatchLoadViewState(BaseFragment.FragmentLoadViewStatus.SUCCESS)

        setToolBarTheme(true, R.color.mainColor, "消息", R.color.white)


    }

    override fun initDataListener() {
        super.initDataListener()
        fragmentMessageBinding.apply {

            loginViewModel.checkTokenResultLiveData.observe(this@MessageFragment) { checkToken ->
                if (checkToken != null) {
                    if (checkToken.checkTokenBean.success) {
                        currentActivity.messageViewModel.getMessageCount(checkToken.token)
                    }
                }
                messageReadAllMsg.setOnClickListener {
                    if (checkToken != null) {
                        if (checkToken.checkTokenBean.success) {
                            currentViewModel.readAllMessage(checkToken.token)

                        } else {
                            getTokenAgain()
                        }
                    }
                }
                messageWendaContainer.setOnClickListener {
                    checkTokenToActivity(checkToken, "问答消息")
                }

                messageUpupContainer.setOnClickListener {
                    checkTokenToActivity(checkToken, "点赞消息")
                }

                messageMoyuContainer.setOnClickListener {
                    checkTokenToActivity(checkToken, "摸鱼消息")
                }
                messageAiteContainer.setOnClickListener {
                    checkTokenToActivity(checkToken, "@消息")
                }

                messageSystemContainer.setOnClickListener {
                    checkTokenToActivity(checkToken, "系统消息")
                }


                currentViewModel.messageReadAllLiveData.observe(this@MessageFragment){
                    if (it.success) {
                        if (checkToken != null) {
                            currentActivity.messageViewModel.getMessageCount(checkToken.token)
                        }

                    }
                }
            }

            currentViewModel.messageCountLiveData.observe(this@MessageFragment) {
                if (it != null) {



                if (it.wendaMsgCount != 0) {
                    messageWendaCount.visibility = View.VISIBLE
                    messageWendaCount.setRoundMessageText(it.wendaMsgCount.toString())
                } else {
                    messageWendaCount.visibility = View.GONE
                }

                if (it.thumbUpMsgCount != 0) {
                    messageUpupCount.visibility = View.VISIBLE
                    messageUpupCount.setRoundMessageText(it.thumbUpMsgCount.toString())
                } else {
                    messageUpupCount.visibility = View.GONE
                }

                if (it.momentCommentCount != 0) {
                    messageMoyuCount.visibility = View.VISIBLE
                    messageMoyuCount.setRoundMessageText(it.momentCommentCount.toString())
                } else {
                    messageMoyuCount.visibility = View.GONE
                }

                if (it.atMsgCount != 0) {
                    messageAiteCount.visibility = View.VISIBLE
                    messageAiteCount.setRoundMessageText(it.atMsgCount.toString())
                } else {
                    messageAiteCount.visibility = View.GONE
                }

                if (it.systemMsgCount != 0) {
                    messageSystemCount.visibility = View.VISIBLE
                    messageSystemCount.setRoundMessageText(it.systemMsgCount.toString())
                } else {
                    messageSystemCount.visibility = View.GONE
                }

                }
            }
        }

    }

    private fun checkTokenToActivity(
        checkToken: CheckTokenDataHasToken?,
        state: String,
    ) {
        if (checkToken != null) {

            if (checkToken.checkTokenBean.success) {
                val intent = Intent(currentActivity, MessageDetailActivity::class.java)
                intent.putExtra(Constant.SOB_TOKEN, checkToken.token)
                intent.putExtra(Constant.SOB_MESSAGE_STATE, state)
                registerForActivityResult.launch(intent)

                //startActivity(intent)
            } else {
                getTokenAgain()
            }
        } else {
            ToastUtil.setText("太多请求或没有网络!")
        }
    }


    override fun getMyCurrentActivity(): HomeActivity {
        return requireActivity() as HomeActivity
    }

    override fun getCurrentViewModel() = MessageViewModel::class.java


}