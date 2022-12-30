package com.example.sunofbeachagain.activity.user

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.view.View
import com.bumptech.glide.Glide
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityUserCreateShouCangBinding
import com.example.sunofbeachagain.domain.body.CreateShouCangBody
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.UserCreateShouCangViewModel
import me.nereo.multi_image_selector.MultiImageSelectorActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UserCreateShouCangActivity : BaseActivityViewModel<UserCreateShouCangViewModel>() {
    private lateinit var activityUserCreateShouCangBinding: ActivityUserCreateShouCangBinding

    private val defaultList = arrayListOf<String>()

    override fun getSuccessView(): View {
        activityUserCreateShouCangBinding =
            ActivityUserCreateShouCangBinding.inflate(layoutInflater)

        return activityUserCreateShouCangBinding.root
    }

    private var coverFile: File? = null

    private var isPublish = 1

    private var name: String = ""

    private var msg: String = ""
    override fun initView() {
        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
        setToolBarTheme(true, null, false)

        setMyTAG(this::class.simpleName)

        coverFile = getExternalFilesDir(null)


        deleteImageFile()
    }


    override fun initListener() {
        super.initListener()
        activityUserCreateShouCangBinding.apply {
            createShouCangAddImage.setOnClickListener {
                val intent =
                    Intent(this@UserCreateShouCangActivity, MultiImageSelectorActivity::class.java)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                    MultiImageSelectorActivity.MODE_SINGLE)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultList)

                startActivityForResult(intent, 1)
            }

            createShouCangToolbar.setNavigationOnClickListener {
                finish()
            }

            loginViewModel.checkTokenResultLiveData.observe(this@UserCreateShouCangActivity) { checkToken ->

                userCreateConfirmCreateTv.setOnClickListener {
                    if (checkToken != null) {
                        if (checkToken.checkTokenBean.success) {
                            name = createShouCangNameEd.text.toString().trim()
                            msg = createShouCangMsgEd.text.toString().trim()

                            if (name.isEmpty() && msg.isEmpty()) {
                                ToastUtil.setText("请输入信息")
                            } else {
                                if (coverFile != null) {
                                    if (coverFile?.listFiles().isNullOrEmpty()) {
                                        ToastUtil.setText("请上传封面")
                                    } else {
                                        coverFile?.listFiles()?.forEach {

                                            val requestBody =
                                                RequestBody.create("image/jpeg".toMediaTypeOrNull(),
                                                    it)

                                            val multipartBody =
                                                MultipartBody.Part.createFormData("image",
                                                    it.name,
                                                    requestBody)

                                            currentViewModel?.postShouCangCover(checkToken.token,
                                                multipartBody)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            createShouCangIsPublic.setOnCheckedChangeListener { buttonView, isChecked ->
                isPublish = if (isChecked) {
                    0
                } else {
                    1
                }
            }
        }
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel?.apply {
            loginViewModel.checkTokenResultLiveData.observe(this@UserCreateShouCangActivity) { checkToken ->
                shouCangCoverLiveData.observe(this@UserCreateShouCangActivity) {
                    if (it.success) {
                        if (checkToken != null) {
                            if (checkToken.checkTokenBean.success) {
                                val createShouCangBody =
                                    CreateShouCangBody(name, msg, it.data, isPublish.toString())
                                postCreateShouCang(checkToken.token, createShouCangBody)
                            }
                        }

                    }
                }
            }

            createShouCangLiveData.observe(this@UserCreateShouCangActivity){
                ToastUtil.setText(it.message)

                if (it.success){
                    finish()
                    setTokenToPreActivity()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)?.let {
                            it.forEach {
                                handleShouCangCover(it)

                                Glide.with(activityUserCreateShouCangBinding.createShouCangAddImage)
                                    .load(it)
                                    .into(activityUserCreateShouCangBinding.createShouCangAddImage)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun handleShouCangCover(it: String) {
        val currentFile = File(it)

        val desFile = File("${coverFile?.path}/${currentFile.name}")

        currentFile.copyTo(desFile, false, 1024)

        val readPictureDegree = readPictureDegree(desFile.path)

        val bitmap = BitmapFactory.decodeFile(desFile.path)

        val toturn = toturn(bitmap, readPictureDegree.toFloat())

        if (desFile.exists()) {
            desFile.delete()
        }

        desFile.createNewFile()

        val outputStream = FileOutputStream(desFile)

        toturn?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        outputStream.flush()

        outputStream.close()


    }

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
        coverFile?.listFiles()?.forEach {
            if (it.exists()) {
                it.delete()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        deleteImageFile()
    }

    override fun getCurrentViewModel() = UserCreateShouCangViewModel::class.java

}