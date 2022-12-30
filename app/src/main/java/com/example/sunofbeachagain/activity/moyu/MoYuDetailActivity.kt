package com.example.sunofbeachagain.activity.moyu

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.order.CheckImageActivity
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.MoYuCommentAdapter
import com.example.sunofbeachagain.adapter.MoYuImageAdapter
import com.example.sunofbeachagain.adapter.MoYuListAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.base.BaseApp
import com.example.sunofbeachagain.databinding.ActivityMoYuDetailBinding
import com.example.sunofbeachagain.databinding.ItemMoyuListBinding
import com.example.sunofbeachagain.domain.CheckImageData
import com.example.sunofbeachagain.domain.CheckTokenDataHasToken
import com.example.sunofbeachagain.domain.ItemMoYuListData
import com.example.sunofbeachagain.domain.bean.MoYuComment
import com.example.sunofbeachagain.domain.bean.MoYuData
import com.example.sunofbeachagain.domain.body.MoYuCommentBody
import com.example.sunofbeachagain.domain.body.MoYuSubCommentBody
import com.example.sunofbeachagain.utils.*
import com.example.sunofbeachagain.view.TextEditTextView
import com.example.sunofbeachagain.viewmodel.MoYuDetailViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat

class MoYuDetailActivity : BaseActivityViewModel<MoYuDetailViewModel>() {


    private val activityMoYuDetailBinding by lazy {
        ActivityMoYuDetailBinding.inflate(layoutInflater)
    }

    override fun getSuccessView(): View {
        return activityMoYuDetailBinding.root
    }

    private val moYuListAdapter by lazy {
        MoYuListAdapter()
    }

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private val oneDay = 86400000
    private val oneHour = 3600000

    private val moYuImageAdapter by lazy {
        MoYuImageAdapter()
    }

    private val moYuCommentAdapter by lazy {
        MoYuCommentAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    var currentMomentId = ""

    var currentTargetUserId = ""

    var currentCommentId = ""
    override fun initView() {
        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)

        setMyTAG(this::class.simpleName)

        setToolBarTheme("摸鱼详情", R.mipmap.back, R.color.mainColor, R.color.white, false)
        val intent = intent

        currentMomentId = intent.getStringExtra(Constant.SOB_MOYU_ID).toString()


        currentViewModel?.apply {
            getSingleMoYuDetail(currentMomentId)
            getMoYuCommentData(currentMomentId)
        }
        moYuListAdapter.getContentLine(100)


        activityMoYuDetailBinding.apply {
            moyuDetailComment.adapter = moYuCommentAdapter.withLoadStateFooter(loadMoreAdapter)

            moyuDetailSmartRefresh.setEnableLoadMore(false)
            moyuDetailSmartRefresh.setEnableRefresh(true)
            moyuDetailSmartRefresh.setRefreshHeader(MaterialHeader(this@MoYuDetailActivity))
            moyuDetailSmartRefresh.setEnableHeaderTranslationContent(true)


            moyuDetailInputSubComment.isFocusable = true
            moyuDetailInputSubComment.isFocusableInTouchMode = true
            moyuDetailInputSubComment.requestFocus()
        }
    }


    override fun initListener() {
        super.initListener()
        activityMoYuDetailBinding.apply {


            moyuDetailInputSubComment.setOnKeyBoardHideListener(object :
                TextEditTextView.OnKeyBoardHideListener {
                override fun onKeyHide(keyCode: Int, event: KeyEvent?) {
                    moyuDetailCommentContainer.visibility = View.VISIBLE

                    moyuDetailSubCommentContainer.visibility = View.GONE
                }

            })
        }


        loginViewModel.checkTokenResultLiveData.observe(this) { checkToken ->


            activityMoYuDetailBinding.apply {

                moYuCommentAdapter.setOnMoYuCommentCommentClickListener(object :
                    MoYuCommentAdapter.OnMoYuCommentCommentClickListener {
                    override fun onMoYuCommentCommentClick(
                        momentId: String,
                        targetUserId: String,
                        commentId: String,
                        targetUserName: String,
                    ) {
                        currentMomentId = momentId

                        currentTargetUserId = targetUserId

                        currentCommentId = commentId


                        moyuDetailCommentContainer.visibility = View.GONE

                        moyuDetailSubCommentContainer.visibility = View.VISIBLE

                        moyuDetailSubCcctv.text = "回复:${targetUserName}"

                        moyuDetailInputSubComment.isFocusable = true
                        moyuDetailInputSubComment.isFocusableInTouchMode = true
                        moyuDetailInputSubComment.requestFocus()



                        KeyBordUtil.showKeyBord(this@MoYuDetailActivity, moyuDetailInputSubComment)
                    }

                })

                moyuDetailSendComment.setOnClickListener {
                    val inputText = moyuDetailInputComment.text.toString().trim()

                    if (inputText.isEmpty()) {
                        ToastUtil.setText("说点什么吧")
                    } else {
                        if (checkToken != null) {
                            if (checkToken.checkTokenBean.success) {

                                val moYuCommentBody = MoYuCommentBody(currentMomentId, inputText)

                                currentViewModel?.postMoYuComment(checkToken.token, moYuCommentBody)

                                KeyBordUtil.hideKeyBord(this@MoYuDetailActivity,
                                    activityMoYuDetailBinding.moyuDetailInputComment)
                                activityMoYuDetailBinding.moyuDetailInputComment.setText("")

                                ToastUtil.setText("评论成功，刷新一下吧")


                                currentViewModel?.getMoYuCommentData(currentMomentId)

                            } else {
                                ToastUtil.setText("先登陆一下")
                                getTokenAgain()
                            }
                        }
                    }

                }

                moyuDetailSendSubComment.setOnClickListener {
                    val subCommentInputText = moyuDetailInputSubComment.text.toString().trim()

                    if (subCommentInputText.isEmpty()) {
                        ToastUtil.setText("说点什么吧")
                    } else {
                        if (checkToken != null) {
                            if (checkToken.checkTokenBean.success) {
                                val moYuSubCommentBody = MoYuSubCommentBody(subCommentInputText,
                                    currentMomentId,
                                    currentTargetUserId,
                                    currentCommentId)

                                currentViewModel?.postMoYuSubComment(checkToken.token,
                                    moYuSubCommentBody)

                                currentViewModel?.getMoYuCommentData(currentMomentId)
                            } else {
                                ToastUtil.setText("先登陆一下")
                                getTokenAgain()
                            }

                            KeyBordUtil.hideKeyBord(this@MoYuDetailActivity,
                                activityMoYuDetailBinding.moyuDetailInputSubComment)
                            activityMoYuDetailBinding.moyuDetailInputSubComment.setText("")

                            moyuDetailCommentContainer.visibility = View.VISIBLE

                            moyuDetailSubCommentContainer.visibility = View.GONE

                            ToastUtil.setText("评论成功，刷新一下吧")

                        }
                    }
                }
            }

            currentViewModel?.apply {


                moYuCommentAdapter.setOnMoYuCommentItemClickListener(object :
                    MoYuCommentAdapter.OnMoYuCommentItemClickListener {
                    override fun onMoYuCommentClick(moYuComment: MoYuComment) {

                        if (NetWorkUtils.isConnected(this@MoYuDetailActivity)) {

                            val intent =
                                Intent(this@MoYuDetailActivity,
                                    MoYuSubCommentActivity::class.java)

                               intent.putExtra(Constant.SOB_TOKEN, checkToken?.token)

                            intent.putExtra(Constant.SOB_DETAIL_TO_SUB_COMMENT_DATA,
                                moYuComment)

                            startActivity(intent)

                            //   registerForActivityResult.launch(intent)
                        } else {
                            ToastUtil.setText("好像没有网络")
                        }

                    }

                    override fun onMoYuAvatarClick(userId: String) {
                        if (checkToken != null) {
                            val intent = Intent(this@MoYuDetailActivity, UserCenterActivity::class.java)

                            intent.putExtra(Constant.SOB_USER_ID, userId)

                            intent.putExtra(Constant.SOB_TOKEN, checkToken.token)

                            registerForActivityResult.launch(intent)
                        } else {
                            ToastUtil.setText("网络出错")
                        }
                    }

                })


                activityMoYuDetailBinding.apply {
                    moyuDetailSmartRefresh.setOnRefreshListener {
                        getSingleMoYuDetail(currentMomentId)
                        getMoYuCommentData(currentMomentId)
                        it.finishRefresh()
                    }
                }
            }
        }

        moYuImageAdapter.setOnMoYuImageClickListener(object :
            MoYuImageAdapter.OnMoYuImageClickListener {
            override fun onMoYuImageClick(images: List<String>, position: Int) {
                val checkImageData = CheckImageData(images, position)

                val intent = Intent(this@MoYuDetailActivity, CheckImageActivity::class.java)

                intent.putExtra(Constant.SOB_CHECK_IMAGE, checkImageData)

                startActivity(intent)
            }

        })


    }


    override fun initDataListener() {
        super.initDataListener()
        activityMoYuDetailBinding.moyuDetailHead.apply {
            currentViewModel?.apply {
                loginViewModel.checkTokenResultLiveData.observe(this@MoYuDetailActivity) { checkToken ->

                    singleMoYuDataLiveData.observe(this@MoYuDetailActivity) {

                        handleHeadData(it, if (checkToken != null) {
                            if (checkToken.checkTokenBean.success) {
                                checkToken
                            } else {
                                null
                            }
                        } else {
                            null
                        })
                    }
                }


                singleMoYuLoadStateLiveData.observe(this@MoYuDetailActivity) {
                    when (it) {
                        MoYuDetailViewModel.SingleMoYuDataLoadStatus.LOADING -> {
                            //   switchDispatchLoadViewState(ActivityLoadViewStatus.LOADING)
                        }

                        MoYuDetailViewModel.SingleMoYuDataLoadStatus.SUCCESS -> {
                            switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
                        }

                        MoYuDetailViewModel.SingleMoYuDataLoadStatus.ERROR -> {
                            switchDispatchLoadViewState(ActivityLoadViewStatus.ERROR)
                        }

                        MoYuDetailViewModel.SingleMoYuDataLoadStatus.EMPTY -> {
                            switchDispatchLoadViewState(ActivityLoadViewStatus.EMPTY)
                        }
                    }
                }

                moYuCommentDataLiveData.observe(this@MoYuDetailActivity) {
                    lifecycleScope.launchWhenCreated {
                        it.collectLatest {
                            moYuCommentAdapter.submitData(it)
                        }
                    }
                }
            }
        }

    }

    override fun errorReload() {
        super.errorReload()
        currentViewModel?.apply {
            getSingleMoYuDetail(currentMomentId)
            getMoYuCommentData(currentMomentId)
        }
    }


    private fun ItemMoyuListBinding.handleHeadData(
        itemData: MoYuData,
        checkToken: CheckTokenDataHasToken?,
    ) {

        itemMoyuImageRv.visibility = if (itemData.images.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        itemMoyuLinkContainer.visibility =
            if (itemData.linkUrl.isNullOrEmpty() || itemData.linkTitle.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }

        val moYuPosition = if (itemData.position.isNullOrEmpty()) {
            "刁民"
        } else {
            itemData.position
        }

        val moYuTheme = if (itemData.topicName.isNullOrEmpty()) {
            "摸鱼"
        } else {
            itemData.topicName
        }

        //==================================计算时间====================================
        val currentTime = System.currentTimeMillis()

        val dataTime = simpleDateFormat.parse(itemData.createTime)!!.time

        val intervalTime = currentTime - dataTime

        val format = simpleDateFormat.format(dataTime)
        //大于一天
        val moYuTime = if (intervalTime >= oneDay * 2) {
            format

        } else if (intervalTime >= oneDay) {
            "昨天${
                format.substring(format.indexOf(' '), format.length)
            }"

        } else if (intervalTime <= oneHour) {
            "${(intervalTime) / 60 / 1000}分钟前"

        } else {

            "${intervalTime / 60 / 60 / 1000}小时前"
        }

        itemMoyuContent.maxLines = 10000

        val itemMoYuData = ItemMoYuListData(itemData.avatar,
            itemData.nickname,
            moYuPosition,
            moYuTime,
            StringUtil.stripHtml(itemData.content).replace("&nbsp", " "),
            itemData.images,
            itemData.linkTitle,
            itemData.linkUrl,
            moYuTheme,
            itemData.commentCount.toString(),
            itemData.thumbUpCount.toString())

        val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))

        Glide.with(itemMoyuAvatar).load(itemData.avatar)
            .apply(requestOptions)
            .into(itemMoyuAvatar)

        moYuData = itemMoYuData

        val moYuImageAdapter = MoYuImageAdapter()
        if (!itemData.images.isNullOrEmpty()) {
            if (itemData.images.size%3==0){
                itemMoyuImageRv.layoutManager = GridLayoutManager(BaseApp.mContext, 3)
            }else{
                if (itemData.images.size!=8&&itemData.images.size%2 ==0){
                    itemMoyuImageRv.layoutManager = GridLayoutManager(BaseApp.mContext, 2)
                }else{
                    if (itemData.images.size==1){
                        itemMoyuImageRv.layoutManager = GridLayoutManager(BaseApp.mContext, 1)
                    }else{
                        if (itemData.images.size==5){
                            itemMoyuImageRv.layoutManager = GridLayoutManager(BaseApp.mContext, 3)
                        }else{
                            if (itemData.images.size==7){
                                itemMoyuImageRv.layoutManager = GridLayoutManager(BaseApp.mContext, 4)
                            }
                        }
                    }
                }
            }

            itemMoyuImageRv.adapter = moYuImageAdapter
        }

        moYuImageAdapter.setOnMoYuImageClickListener(object :MoYuImageAdapter.OnMoYuImageClickListener{
            override fun onMoYuImageClick(images: List<String>, position: Int) {
                val checkImageData = CheckImageData(images, position)

                val intent = Intent(this@MoYuDetailActivity, CheckImageActivity::class.java)

                intent.putExtra(Constant.SOB_CHECK_IMAGE, checkImageData)

                startActivity(intent)
            }

        })

        moYuImageAdapter.setImageData(itemData.images)

        if (checkToken != null) {
            if (checkToken.checkTokenBean.success) {
                if (itemData.thumbUpList != null) {
                    if (itemData.thumbUpList.contains(checkToken.checkTokenBean.data.id)) {
                        itemMoyuUpupTv.setTextColor(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.mainColor))
                        itemMoyuUpupIv.setColorFilter(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.mainColor))

                    } else {
                        itemMoyuUpupTv.setTextColor(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.gray2))
                        itemMoyuUpupIv.setColorFilter(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.gray2))

                    }

                }


                itemMoyuUpupContainer.setOnClickListener {
                    var canUp = false
                    if (itemData.thumbUpList == null) {
                        canUp = true
                    } else {
                        if (!itemData.thumbUpList.contains(checkToken.checkTokenBean.data.id)) {
                            canUp = true
                        }else{
                            canUp = false
                            ToastUtil.setText("已经点赞过了")
                        }
                    }

                    if (canUp){
                        currentViewModel?.putMoYuMomentUp(checkToken.token,itemData.id)
                        itemMoyuUpupTv.setTextColor(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.mainColor))
                        itemMoyuUpupIv.setColorFilter(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.mainColor))

                        itemMoyuUpupTv.text = (itemData.thumbUpCount + 1).toString()
                    }
                }

            }
        }



        itemMoyuAvatar.setOnClickListener {
            if (checkToken != null) {
                val intent = Intent(this@MoYuDetailActivity, UserCenterActivity::class.java)

                intent.putExtra(Constant.SOB_USER_ID, itemData.userId)

                intent.putExtra(Constant.SOB_TOKEN, checkToken.token)

                registerForActivityResult.launch(intent)
            } else {
                ToastUtil.setText("网络出错")
            }
        }

        itemMoyuLinkContainer.setOnClickListener {
            val intent = Intent()

            intent.data = Uri.parse(itemData.linkUrl)

            startActivity(intent)
        }

    }

    override fun getCurrentViewModel() = MoYuDetailViewModel::class.java


}