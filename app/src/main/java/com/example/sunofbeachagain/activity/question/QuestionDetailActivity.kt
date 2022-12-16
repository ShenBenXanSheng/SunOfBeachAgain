package com.example.sunofbeachagain.activity.question

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.order.CheckImageActivity
import com.example.sunofbeachagain.base.BaseActivity
import com.example.sunofbeachagain.databinding.ActivityQuestionDetailBinding
import com.example.sunofbeachagain.domain.CheckImageData
import com.example.sunofbeachagain.domain.bean.WebCheckImageBean
import com.example.sunofbeachagain.domain.bean.WenDaDetailCommentBean
import com.example.sunofbeachagain.domain.bean.WenDaDetailSubCommentBean
import com.example.sunofbeachagain.request.AndroidtoJs
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.google.gson.Gson

class QuestionDetailActivity : BaseActivity() {
    private lateinit var activityQuestionDetailBinding: ActivityQuestionDetailBinding
    override fun getSuccessView(): View {
        activityQuestionDetailBinding = ActivityQuestionDetailBinding.inflate(layoutInflater)

        return activityQuestionDetailBinding.root
    }


    private var currentWendaId: String = ""


    val questionCommentRegisterForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                it.data?.getStringExtra(Constant.SOB_QUESTION_COMMENT_RESULT)?.let {result->
                    if (result == "问答评论"){
                        callJs("nativeCallUpdateWendaComments", "")
                    }
                }
            }
        }

    override fun initView() {
        setToolBarTheme("问答详情", R.mipmap.back, R.color.mainColor, R.color.white, false)

        setMyTAG(this::class.simpleName)


        intent.getStringExtra(Constant.SOB_QUESTION_ID)?.let {
            currentWendaId = it
        }


        activityQuestionDetailBinding.questionDetailWebView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    switchDispatchLoadViewState(ActivityLoadViewStatus.LOADING)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    return true
                }
            }

            settings.apply {
                javaScriptEnabled = true
                cacheMode = WebSettings.LOAD_NO_CACHE

            }

            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    setToolBarTheme(if (title.isNullOrEmpty()) {
                        "文章详情"
                    } else {
                        title
                    }, R.mipmap.back, R.color.mainColor, R.color.white, false)
                }
            }

            val androidtoJs = AndroidtoJs { msg ->

                handlerJsMsg(msg)
            }
            addJavascriptInterface(androidtoJs, "DebugApp")
            loadUrl("http://www.debuglive.cn/sobwenda/${currentWendaId}")


        }

    }

    private fun handlerJsMsg(msg: String) {
        if (msg.contains("105")) {
            Log.d(TAG, "查看图片")
            val imageList = mutableListOf<String>()

            val imageBean = Gson().fromJson(msg, WebCheckImageBean::class.java)
            imageList.add(imageBean.data.imageUrl)

            val checkImageData = CheckImageData(imageList, 0)

            val intent = Intent(this@QuestionDetailActivity, CheckImageActivity::class.java)

            intent.putExtra(Constant.SOB_CHECK_IMAGE, checkImageData)

            startActivity(intent)
        }

        if (msg.contains("106")) {
            val wenDaDetailCommentBean = Gson().fromJson(msg, WenDaDetailCommentBean::class.java)

            if (!firsttoken.isNullOrEmpty()) {
                val intent =
                    Intent(this@QuestionDetailActivity, QuestionCommentActivity::class.java)

                intent.putExtra(Constant.SOB_QUESTION_ID, wenDaDetailCommentBean.data.wendaId)

                intent.putExtra(Constant.SOB_TOKEN, firsttoken)

                questionCommentRegisterForActivityResult.launch(intent)
            } else {
                ToastUtil.setText("还没登陆")
            }


        }

        if (msg.contains("107")) {
            val wenDaDetailSubCommentBean =
                Gson().fromJson(msg, WenDaDetailSubCommentBean::class.java)
            Log.d(TAG, wenDaDetailSubCommentBean.toString())
            if (!firsttoken.isNullOrEmpty()) {
                val intent =
                    Intent(this@QuestionDetailActivity, QuestionCommentActivity::class.java)

                intent.putExtra(Constant.SOB_QUESTION_SUB_COMMENT, wenDaDetailSubCommentBean.data)
                intent.putExtra(Constant.SOB_TOKEN, firsttoken)

                questionCommentRegisterForActivityResult.launch(intent)


            } else {
                ToastUtil.setText("还没登陆")
            }


        }


    }

    private fun callJs(name: String, args: String) {
        val url = "javascript:$name('$args')"
        activityQuestionDetailBinding.questionDetailWebView.loadUrl(url)
    }
}