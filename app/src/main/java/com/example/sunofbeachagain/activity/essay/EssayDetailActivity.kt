package com.example.sunofbeachagain.activity.essay

import android.content.Intent
import android.graphics.Bitmap
import android.text.InputType
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.order.CheckImageActivity
import com.example.sunofbeachagain.activity.user.UserShouCansActivity
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityEssayDetailBinding
import com.example.sunofbeachagain.domain.CheckImageData
import com.example.sunofbeachagain.domain.bean.WebCheckImageBean
import com.example.sunofbeachagain.domain.bean.WebSubCommentBean
import com.example.sunofbeachagain.domain.bean.WebUpAndTippingAndComment
import com.example.sunofbeachagain.domain.body.EssayTippingBody
import com.example.sunofbeachagain.request.AndroidtoJs
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.view.TippingDialog
import com.example.sunofbeachagain.viewmodel.EssayDetailViewModel
import com.google.gson.Gson

class EssayDetailActivity : BaseActivityViewModel<EssayDetailViewModel>() {
    private lateinit var activityEssayDetailBinding: ActivityEssayDetailBinding
    override fun getSuccessView(): View {
        activityEssayDetailBinding = ActivityEssayDetailBinding.inflate(layoutInflater)

        return activityEssayDetailBinding.root
    }

    private var currentEssayId = ""

    private var currentEssayTitle = ""

    private var commentVis = false


    private val tippingDialog by lazy {
        TippingDialog(this)
    }

    private val commentRegisterForActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        it.data?.getStringExtra(Constant.SOB_ESSAY_COMMENT_RESULT)?.let { result->
            if (result == "完成"){
                callJs("nativeCallUpdateComments", "")
            }
        }
    }
    override fun initView() {
        setMyTAG(this::class.simpleName)

        intent.getStringExtra(Constant.SOB_ESSAY_ID)?.let {
            currentEssayId = it
        }

        activityEssayDetailBinding.apply {
            essayDetailCommentEt.isFocusable = false

            essayDetailWeb.apply {

                settings.apply {
                    javaScriptEnabled = true
                    cacheMode = WebSettings.LOAD_NO_CACHE

                }

                loadUrl("http://www.debuglive.cn/sob/${currentEssayId}")

                val androidtoJs = AndroidtoJs { msg ->

                    initCurrentListener(msg)

                }


                addJavascriptInterface(androidtoJs, "DebugApp")


                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?,
                    ): Boolean {
                        return true
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        switchDispatchLoadViewState(ActivityLoadViewStatus.LOADING)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)

                        essayDetailGoTopOrComment.setOnClickListener {
                            if (commentVis) {
                                scrollTo(0, 0)
                                essayDetailGoTopOrComment.setImageResource(R.mipmap.huifu)

                            } else {
                                scrollTo(0, contentHeight * 3)
                                essayDetailGoTopOrComment.setImageResource(R.mipmap.zhengwenmoban)
                            }

                        }
                    }

                }


                webChromeClient = object : WebChromeClient() {
                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
                        setToolBarTheme(if (title.isNullOrEmpty()) {
                            "文章详情"
                        } else {
                            currentEssayTitle = title
                            title
                        }, R.mipmap.back, R.color.mainColor, R.color.white, false)
                    }
                }


            }
        }
    }

    private fun ActivityEssayDetailBinding.initCurrentListener(msg: String) {

        if (msg.contains("104") || msg.contains("101") || msg.contains("102")) {

            val fromJson = Gson().fromJson(msg, WebUpAndTippingAndComment::class.java)

            when (fromJson.protocolCode) {
                104 -> {
                    //点赞
                    if (!firsttoken.isNullOrEmpty()) {
                        currentViewModel?.checkHasUp(firsttoken, currentEssayId)
                    } else {
                        ToastUtil.setText("还没登陆")
                    }


                }

                101 -> {
                    //打赏
                    if (!firsttoken.isNullOrEmpty()) {
                        currentViewModel?.getAchievement(firsttoken)
                    } else {
                        ToastUtil.setText("还没登陆")
                    }

                }

                102 -> {
                    //评论
                    if (!firsttoken.isNullOrEmpty()) {
                        val intent = Intent(this@EssayDetailActivity, EssayCommentActivity::class.java)

                        intent.putExtra(Constant.SOB_TOKEN,firsttoken)
                        intent.putExtra(Constant.SOB_ESSAY_ID,fromJson.data.articleId)
                        intent.putExtra(Constant.SOB_ESSAY_IS_COMMENT,true)
                        commentRegisterForActivityResult.launch(intent)
                    } else {
                        ToastUtil.setText("还没登陆")
                    }


                }
            }
        }


        if (msg.contains("103")) {
            //回复
            val subCommentBean = Gson().fromJson(msg, WebSubCommentBean::class.java)

            val intent = Intent(this@EssayDetailActivity, EssayCommentActivity::class.java)

            intent.putExtra(Constant.SOB_TOKEN,firsttoken)

            intent.putExtra(Constant.SOB_ESSAY_SUB_COMMENT,subCommentBean.data)

            intent.putExtra(Constant.SOB_ESSAY_IS_COMMENT,false)

            commentRegisterForActivityResult.launch(intent)

            Log.d("TAG", subCommentBean.toString())
        }


        if (msg.contains("105")) {
        //查看图片
            val imageList = mutableListOf<String>()

            val imageBean = Gson().fromJson(msg, WebCheckImageBean::class.java)
            imageList.add(imageBean.data.imageUrl)

            val checkImageData = CheckImageData(imageList, 0)

            val intent = Intent(this@EssayDetailActivity, CheckImageActivity::class.java)

            intent.putExtra(Constant.SOB_CHECK_IMAGE, checkImageData)

            startActivity(intent)
        }

        commentVis = msg.contains("true")

        if (commentVis) {
            essayDetailGoTopOrComment.setImageResource(R.mipmap.zhengwenmoban)
        } else {
            essayDetailGoTopOrComment.setImageResource(R.mipmap.huifu)
        }


    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel?.apply {
            essayHasUpLiveData.observe(this@EssayDetailActivity) {
                if (it.success) {
                    ToastUtil.setText(it.message)
                } else {
                    putEssayUpUp(firsttoken, currentEssayId)

                }
            }

            essayUpUpLiveData.observe(this@EssayDetailActivity) {
                ToastUtil.setText(it.message)
            }

            essayUserAchievement.observe(this@EssayDetailActivity) {
                tippingDialog.getMaxSob(it.sob)

                tippingDialog.setOnTippingDialogClickListener(object :
                    TippingDialog.OnTippingDialogClickListener {
                    override fun onTippingClick(tippingSob: Int) {
                        val essayTippingBody = EssayTippingBody(currentEssayId, tippingSob)
                        postEssayTipping(firsttoken, essayTippingBody)

                        // Log.d("TAG",tippingSob.toString())
                    }

                })

                tippingDialog.show()
            }

            essayTippingLiveData.observe(this@EssayDetailActivity) {
                if (it.success) {
                    callJs("nativeCallUpdateReward", "")
                }
                ToastUtil.setText(it.message)
            }

        }
    }

    private fun callJs(name: String, args: String) {
        val url = "javascript:$name('$args')"
        activityEssayDetailBinding.essayDetailWeb.loadUrl(url)
    }

    override fun initListener() {
        super.initListener()

        activityEssayDetailBinding.essayDetailShoucang.setOnClickListener {
            if (!firsttoken.isNullOrEmpty()) {

                val intent = Intent(this, UserShouCansActivity::class.java)

                intent.putExtra(Constant.SOB_ESSAY_TITLE, currentEssayTitle)

                intent.putExtra(Constant.SOB_ESSAY_ID,
                    "http://www.debuglive.cn/sob/${currentEssayId}")

                intent.putExtra(Constant.SOB_IS_SHOUCHANG, true)


                intent.putExtra(Constant.SOB_TOKEN, firsttoken)

                registerForActivityResult.launch(intent)

            } else {
                ToastUtil.setText("还没登陆")
            }
        }

        activityEssayDetailBinding.essayDetailCommentEt.setOnClickListener {
            if (firsttoken.isNullOrEmpty()) {
                ToastUtil.setText("还没登陆")
            } else {
                val intent = Intent(this@EssayDetailActivity, EssayCommentActivity::class.java)

                intent.putExtra(Constant.SOB_TOKEN,firsttoken)
                intent.putExtra(Constant.SOB_ESSAY_ID,currentEssayId)
                intent.putExtra(Constant.SOB_ESSAY_IS_COMMENT,true)
                commentRegisterForActivityResult.launch(intent)


            }
        }

    }

    override fun getCurrentViewModel() = EssayDetailViewModel::class.java

}