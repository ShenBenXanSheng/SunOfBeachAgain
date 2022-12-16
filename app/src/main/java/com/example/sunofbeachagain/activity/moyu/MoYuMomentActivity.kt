package com.example.sunofbeachagain.activity.moyu

//noinspection ExifInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeFile
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.order.MoYuMomentCheckImageActivity
import com.example.sunofbeachagain.activity.order.SelectTopicActivity
import com.example.sunofbeachagain.activity.order.SelectTopicData
import com.example.sunofbeachagain.adapter.MoYuMomentAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityMoyuMomentBinding
import com.example.sunofbeachagain.domain.CheckImageData
import com.example.sunofbeachagain.domain.body.MoYuMomentBody
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.MoYuMomentViewModel
import me.nereo.multi_image_selector.MultiImageSelectorActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MoYuMomentActivity : BaseActivityViewModel<MoYuMomentViewModel>() {
    private lateinit var activityMoyuMomentBinding: ActivityMoyuMomentBinding

    private val defaultList = arrayListOf<String>()

    private val firstSelectCode = 1

    private val checkImageCode = 2

    private val checkTopicCode = 3

    private val currentImageList = mutableListOf<Any>()

    private val moYuMomentAdapter by lazy {
        MoYuMomentAdapter()
    }

    private val serverImageList = mutableListOf<String>()

    override fun getSuccessView(): View {
        activityMoyuMomentBinding = ActivityMoyuMomentBinding.inflate(layoutInflater)

        return activityMoyuMomentBinding.root
    }

    private var copyFilePath: File? = null

    private var currentTopicId = "1384518752720130050"

    private var moyuMomentText = ""

    override fun initView() {
        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)

        setToolBarTheme(true, null, false)

        setMyTAG(this::class.simpleName)

        copyFilePath = getExternalFilesDir(null)

        activityMoyuMomentBinding.apply {
            moyuMomentImageRv.adapter = moYuMomentAdapter

            moyuMomentImageRv.layoutManager = GridLayoutManager(this@MoYuMomentActivity, 3)
        }
    }

    override fun initListener() {
        super.initListener()
        activityMoyuMomentBinding.apply {
            loginViewModel.checkTokenResultLiveData.observe(this@MoYuMomentActivity) { checkTolen ->
                moyuMomentFirstAddImage.setOnClickListener {
                    startSelectImageActivity(Constant.SOB_SELECT_IMAGE_MAX_COUNT)
                }

                moYuMomentAdapter.setOnMoYuMomentItemClickListener(object
                    : MoYuMomentAdapter.OnMoYuMomentItemClickListener {
                    override fun addImageAgain(imageList: MutableList<Any>) {
                        currentImageList.removeAt(currentImageList.size - 1)
                        startSelectImageActivity(Constant.SOB_SELECT_IMAGE_MAX_COUNT - imageList.size)

                    }

                    override fun checkImage(imageList: MutableList<Any>, position: Int) {
                        val intent = Intent(this@MoYuMomentActivity,
                            MoYuMomentCheckImageActivity::class.java)

                        val stringImageList = mutableListOf<String>()

                        imageList.forEach {
                            if (it is String)
                                stringImageList.add(it.toString())
                        }
                        val checkImageData = CheckImageData(stringImageList, position)

                        intent.putExtra(Constant.SOB_CHECK_IMAGE, checkImageData)

                        startActivityForResult(intent, checkImageCode)
                    }

                })

                moyuMomentToolbar.setNavigationOnClickListener {
                    setTokenToPreActivity()
                    finish()
                }

                moyuMomentPublishTv.setOnClickListener {
                    serverImageList.clear()

                    if (checkTolen != null) {
                        moyuMomentText = moyuMomentEt.text.toString().trim()
                        if (moyuMomentText.isNullOrEmpty()) {
                            ToastUtil.setText("分享一下日常吧")
                        } else {
                            if (copyFilePath != null) {
                                if (copyFilePath!!.listFiles()!!.isEmpty()) {


                                    val moYuMomentBody = MoYuMomentBody(moyuMomentText,
                                        currentTopicId,
                                        serverImageList)

                                    currentViewModel?.postMoYuMoment(checkTolen.token,
                                        moYuMomentBody)
                                } else {
                                    copyFilePath?.listFiles()?.forEach {
                                        if (it.exists()) {
                                            val body =
                                                RequestBody.create("image/jpeg".toMediaTypeOrNull(),
                                                    it)
                                            val multipart =
                                                MultipartBody.Part.createFormData("image",
                                                    it.name,
                                                    body)
                                            currentViewModel?.postImage(checkTolen.token, multipart)

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                moyuMomentCheckTopicTv.setOnClickListener {
                    val intent = Intent(this@MoYuMomentActivity, SelectTopicActivity::class.java)
                    startActivityForResult(intent, checkTopicCode)
                }
            }
        }
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel?.apply {
            loginViewModel.checkTokenResultLiveData.observe(this@MoYuMomentActivity) { checkToken ->
                postImageLiveData.observe(this@MoYuMomentActivity) {
                    if (it.success) {
                        serverImageList.add(it.data)
                    }

                    if (serverImageList.size == copyFilePath?.listFiles()?.size) {


                        val moYuMomentBody =
                            MoYuMomentBody(moyuMomentText, currentTopicId, serverImageList)
                        if (checkToken != null) {
                            currentViewModel?.postMoYuMoment(checkToken.token, moYuMomentBody)
                        }


                    }
                }
            }

            postMoYuMomentLiveData.observe(this@MoYuMomentActivity) {
                if (it.success) {
                    setTokenToPreActivity()
                    finish()
                }
                ToastUtil.setText(it.message)

            }
        }
    }

    private fun startSelectImageActivity(imageCount: Int) {
        val intent = Intent(this@MoYuMomentActivity, MultiImageSelectorActivity::class.java)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, imageCount)

        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false)

        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
            MultiImageSelectorActivity.MODE_MULTI)

        intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultList)

        startActivityForResult(intent, firstSelectCode)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            firstSelectCode -> {
                if (resultCode == RESULT_OK) {
                    data?.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)?.let {
                        handleCoverAndSetDataToAdapter(it)
                    }
                }
            }

            checkImageCode -> {
                if (resultCode == RESULT_OK) {
                    data?.getSerializableExtra(Constant.SOB_CHECK_IMAGE)?.let {
                        val isCheckOverImageList = it as List<String>

                        deleteImageFile()
                        currentImageList.clear()
                        serverImageList.clear()

                        handleCoverAndSetDataToAdapter(isCheckOverImageList)

                    }
                }
            }

            checkTopicCode -> {
                if (resultCode == RESULT_OK) {
                    data?.getSerializableExtra(Constant.SOB_TOPIC_DATA)?.let {
                        val selectTopicData = it as SelectTopicData
                        currentTopicId = selectTopicData.id

                        activityMoyuMomentBinding.moyuMomentCheckTopicTv.text = selectTopicData.name
                    }
                }
            }


        }
    }

    private fun handleCoverAndSetDataToAdapter(it: List<String>) {
        it.forEach {

            handleCover(it)

            currentImageList.add(it)
        }

        if (currentImageList.isNotEmpty()) {
            activityMoyuMomentBinding.moyuMomentFirstAddImage.visibility = View.GONE
            activityMoyuMomentBinding.moyuMomentImageRv.visibility = View.VISIBLE

            if (currentImageList.size < Constant.SOB_SELECT_IMAGE_MAX_COUNT) {
                currentImageList.add(R.mipmap.add_image)
            }


            moYuMomentAdapter.setData(currentImageList)
        } else {
            activityMoyuMomentBinding.moyuMomentImageRv.visibility = View.GONE
            activityMoyuMomentBinding.moyuMomentFirstAddImage.visibility = View.VISIBLE
        }


    }

    var i = 0


    private fun handleCover(it: String) {

        try {
            val currentFile = File(it)

            var desFile = File("${copyFilePath!!.path}/${currentFile.name}")

            if (desFile.exists()) {
                desFile = File("${copyFilePath!!.path}/副本${i}${currentFile.name}")
            }

            currentFile.copyTo(desFile, false, 1024)

            val readPictureDegree = readPictureDegree(desFile.path)

            val decodeBitmap = decodeFile(desFile.path)

            val toturnBitmap = toturn(decodeBitmap, readPictureDegree.toFloat())

            if (desFile.exists()) {
                desFile.delete()
            }

            desFile.createNewFile()

            val outputStream = FileOutputStream(desFile)

            toturnBitmap?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

            outputStream.flush()

            outputStream.close()


        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    /**
     * 读取照片exif信息中的旋转角度
     * @param path 照片路径
     * @return角度
     */
    private fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation: Int = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    fun toturn(img: Bitmap, degree: Float): Bitmap? {
        var img = img
        val matrix = Matrix()
        matrix.postRotate(degree) /*翻转90度*/
        val width = img.width
        val height = img.height
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true)
        return img
    }

    fun deleteImageFile() {
        copyFilePath?.listFiles()?.forEach {
            if (it.exists()) {
                it.delete()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        deleteImageFile()
    }


    override fun getCurrentViewModel() = MoYuMomentViewModel::class.java
}