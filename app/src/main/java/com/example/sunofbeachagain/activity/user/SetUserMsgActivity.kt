package com.example.sunofbeachagain.activity.user

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.bumptech.glide.Glide
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivitySetUserMsgBinding
import com.example.sunofbeachagain.domain.bean.Result
import com.example.sunofbeachagain.domain.body.ChangeUserInfoBody
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.view.UserMsgPopWindow
import com.example.sunofbeachagain.view.UserPriceCityPopWindow
import com.example.sunofbeachagain.view.UserPriceDistrictPopWindow
import com.example.sunofbeachagain.view.UserPriceProvincePopWindow
import com.example.sunofbeachagain.viewmodel.SetUserMsgViewModel
import me.nereo.multi_image_selector.MultiImageSelectorActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SetUserMsgActivity : BaseActivityViewModel<SetUserMsgViewModel>() {
    private lateinit var activitySetUserMsgBinding: ActivitySetUserMsgBinding

    private var mUserId = ""

    private var sexCode = ""

    private var cityId = ""

    private var districtId = ""

    private val defaultList = arrayListOf<String>()

    override fun getSuccessView(): View {
        activitySetUserMsgBinding = ActivitySetUserMsgBinding.inflate(layoutInflater)
        return activitySetUserMsgBinding.root
    }

    private lateinit var popUserSex: UserMsgPopWindow

    private lateinit var popUserPriceProvince: UserPriceProvincePopWindow

    private lateinit var popUserPriceCity: UserPriceCityPopWindow

    private lateinit var popUserPriceDistrict: UserPriceDistrictPopWindow

    private var avatarFile: File? = null

    override fun initView() {
        intent.getStringExtra(Constant.SOB_USER_ID)?.let {
            mUserId = it
        }
        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)

        setToolBarTheme("编辑信息", R.mipmap.back, R.color.mainColor, R.color.white, false)

        setMyTAG(this::class.simpleName)

        currentViewModel?.apply {
            getUserInfo(firsttoken)
        }

        avatarFile = getExternalFilesDir(null)

        deleteAvatarFile()

    }

    override fun initListener() {
        super.initListener()
        activitySetUserMsgBinding.apply {
            currentViewModel?.apply {
                userMsgNickname.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                        if (s != null) {
                            checkUserNameCanUser(s.toString())
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                })
            }
            userMsgContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
                OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    popUserSex = UserMsgPopWindow(this@SetUserMsgActivity,
                        userMsgSelectSexContainer.measuredWidth)

                    popUserPriceProvince = UserPriceProvincePopWindow(this@SetUserMsgActivity,
                        userMsgSelectSheng.measuredWidth)

                    userMsgSelectSexContainer.setOnClickListener {
                        if (!popUserSex.isShowing) {

                            popUserSex.isFocusable = true
                            popUserSex.isTouchable = true

                            popUserSex.showAsDropDown(it, 0, 0)
                            userMsgSelectSexIv.setImageResource(R.mipmap.xiangshang)
                            setWindowAlpha(0.98f)
                        } else {
                            popUserSex.dismiss()
                        }

                    }

                    popUserSex.setOnDismissListener {
                        setWindowAlpha(1f)
                        userMsgSelectSexIv.setImageResource(R.mipmap.xiangxia)
                    }

                    popUserSex.setOnPopUserSexClickListener(object :
                        UserMsgPopWindow.OnPopUserSexClickListener {
                        override fun onUserSexClick(text: String, code: String) {
                            sexCode = code
                            userMsgSelectSexTv.text = text
                        }

                    })

                    //======选择省=======
                    userMsgSelectSheng.setOnClickListener {
                        if (!popUserPriceProvince.isShowing) {
                            popUserPriceProvince.isFocusable = true
                            popUserPriceProvince.isTouchable = true

                            popUserPriceProvince.showAsDropDown(it, 0, 0)
                            userMsgSelectShengIv.setImageResource(R.mipmap.xiangshang)
                            setWindowAlpha(0.98f)
                        } else {
                            popUserPriceProvince.dismiss()
                        }
                    }

                    popUserPriceProvince.setOnDismissListener {
                        setWindowAlpha(1f)
                        userMsgSelectShengIv.setImageResource(R.mipmap.xiangxia)
                    }

                    popUserPriceProvince.setOnUserPriceProvinceClickListener(object :
                        UserPriceProvincePopWindow.OnUserPriceProvinceClickListener {
                        override fun onUserPriceProvinceClick(result: Result) {
                            cityId = result.id.substring(0, 2)

                            popUserPriceCity = UserPriceCityPopWindow(cityId,
                                this@SetUserMsgActivity,
                                userMsgSelectSexContainer.measuredWidth)

                            if (cityId == "11" || cityId == "12" || cityId == "31" || cityId == "50") {
                                userMsgSelectShiTv.text = result.fullname
                                userMsgSelectQuxianTv.text = "请选择"
                                userMsgSelectShi.isEnabled = false
                            } else {
                                userMsgSelectShiTv.text = "请选择"
                                userMsgSelectQuxianTv.text = "请选择"
                                userMsgSelectShi.isEnabled = true
                            }
                            userMsgSelectShengTv.text = result.fullname

                        }
                    })

                    //======选择市=======
                    userMsgSelectShi.setOnClickListener {

                        if (this@SetUserMsgActivity::popUserPriceCity.isInitialized) {
                            if (!popUserPriceCity.isShowing) {
                                popUserPriceCity.isFocusable = true
                                popUserPriceCity.isTouchable = true

                                popUserPriceCity.showAsDropDown(it, 0, 0)
                                userMsgSelectShiIv.setImageResource(R.mipmap.xiangshang)
                                setWindowAlpha(0.98f)
                            } else {
                                popUserPriceCity.dismiss()
                            }

                        }
                    }

                    if (this@SetUserMsgActivity::popUserPriceCity.isInitialized) {
                        popUserPriceCity.setOnDismissListener {
                            setWindowAlpha(1f)
                            userMsgSelectShiIv.setImageResource(R.mipmap.xiangxia)
                        }

                        popUserPriceCity.setOnPopUserPriceCityClickListener(object :
                            UserPriceCityPopWindow.OnPopUserPriceCityClickListener {
                            override fun setOnPopUserPriceCityClick(result: Result) {
                                districtId = result.id.substring(0, 4)

                                popUserPriceDistrict = UserPriceDistrictPopWindow(districtId,
                                    this@SetUserMsgActivity,
                                    userMsgSelectShi.measuredWidth)

                                userMsgSelectShiTv.text = result.fullname
                            }

                        })
                    }

                    //======选择区县=======
                    userMsgSelectQuxian.setOnClickListener {
                        if (!userMsgSelectShi.isEnabled) {
                            if (this@SetUserMsgActivity::popUserPriceCity.isInitialized) {
                                if (!popUserPriceCity.isShowing) {
                                    popUserPriceCity.isFocusable = true
                                    popUserPriceCity.isTouchable = true

                                    popUserPriceCity.showAsDropDown(it, 0, 0)
                                    userMsgSelectQuxianIv.setImageResource(R.mipmap.xiangshang)
                                    setWindowAlpha(0.98f)
                                } else {
                                    popUserPriceCity.dismiss()
                                }
                            }
                        } else {
                            if (this@SetUserMsgActivity::popUserPriceDistrict.isInitialized) {
                                if (!popUserPriceDistrict.isShowing) {
                                    popUserPriceDistrict.isFocusable = true
                                    popUserPriceDistrict.isTouchable = true

                                    popUserPriceDistrict.showAsDropDown(it, 0, 0)
                                    userMsgSelectQuxianIv.setImageResource(R.mipmap.xiangshang)
                                    setWindowAlpha(0.98f)
                                } else {
                                    popUserPriceDistrict.dismiss()
                                }
                            }
                        }
                    }

                    if (this@SetUserMsgActivity::popUserPriceCity.isInitialized) {
                        popUserPriceCity.setOnDismissListener {
                            setWindowAlpha(1f)
                            userMsgSelectQuxianIv.setImageResource(R.mipmap.xiangxia)
                        }

                        popUserPriceCity.setOnPopUserZhiXiaShiCLickListener(object :
                            UserPriceCityPopWindow.OnPopUserZhiXiaShiCLickListener {
                            override fun onZhiXiaShiClick(result: Result) {
                                userMsgSelectQuxianTv.text = result.fullname
                            }

                        })
                    }


                    if (this@SetUserMsgActivity::popUserPriceDistrict.isInitialized) {
                        popUserPriceDistrict.setOnDismissListener {
                            setWindowAlpha(1f)
                            userMsgSelectQuxianIv.setImageResource(R.mipmap.xiangxia)
                        }

                        popUserPriceDistrict.setOnPopUserPriceDistrictClickListener(object :
                            UserPriceDistrictPopWindow.OnPopUserPriceDistrictClickListener {
                            override fun setOnPopUserPriceCityClick(result: Result) {
                                userMsgSelectQuxianTv.text = result.fullname
                            }

                        })
                    }


                    userMsgChangeBt.setOnClickListener {
                        val nickNameText = userMsgNickname.text.toString().trim()

                        val price =
                            "${userMsgSelectShengTv.text}/${userMsgSelectShiTv.text}/${userMsgSelectQuxianTv.text}"

                        val companyText = userMsgCompany.text.toString().trim()

                        val goodAtText = userMsgGoodAt.text.toString().trim()

                        val positionText = userMsgPosition.text.toString().trim()

                        val signText = userMsgSign.text.toString().trim()


                        currentViewModel?.changeUserSexAndName(firsttoken, sexCode, nickNameText)

                        val changeUserInfoBody = ChangeUserInfoBody(price,
                            companyText,
                            goodAtText,
                            positionText,
                            signText,
                            mUserId)
                        currentViewModel?.changeUserInfo(firsttoken, changeUserInfoBody)
                    }
                }
            })

            userMsgAvatar.setOnClickListener {
                val intent = Intent(this@SetUserMsgActivity, MultiImageSelectorActivity::class.java)

                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                    MultiImageSelectorActivity.MODE_SINGLE)

                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false)

                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1)

                intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultList)

                startActivityForResult(intent, 1)
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
                            it.forEach { avatar ->
                                handleAvatar(avatar)
                                Glide.with(activitySetUserMsgBinding.userMsgAvatar).load(avatar)
                                    .into(activitySetUserMsgBinding.userMsgAvatar)


                            }
                        }
                    }

                }
            }
        }
    }

    private fun handleAvatar(avatar: String) {
        val currentFile = File(avatar)

        val desFile = File("${avatarFile?.path}/${currentFile.name}")

        currentFile.copyTo(desFile,false,1024)

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

        avatarFile?.listFiles()?.forEach {
            val requestBody =
                RequestBody.create("image/jpeg".toMediaTypeOrNull(),
                    it)

            val multipartBody =
                MultipartBody.Part.createFormData("image",
                    it.name,
                    requestBody)

            currentViewModel?.postUserAvatar(firsttoken,multipartBody)
        }


    }

    override fun initDataListener() {
        super.initDataListener()
        activitySetUserMsgBinding.apply {
            currentViewModel?.apply {
                postUserAvatarLiveData.observe(this@SetUserMsgActivity){
                    if (it.success) {
                        deleteAvatarFile()
                        changeUserAvatar(firsttoken,it.data)
                    }
                }


                changeUserAvatarLiveData.observe(this@SetUserMsgActivity){
                    if (it.success){
                        ToastUtil.setText("修改头像成功")
                    }
                }

                userInfoLiveData.observe(this@SetUserMsgActivity) {
                    Glide.with(userMsgAvatar).load(it.avatar).into(userMsgAvatar)

                    userMsgCompany.setText(it.company)

                    userMsgGoodAt.setText(it.goodAt)

                    userMsgPosition.setText(it.position)

                    userMsgSign.setText(it.sign)
                }

                userNameCanUserLiveData.observe(this@SetUserMsgActivity) {
                    Glide.with(userMsgCanUseNickname).load(if (!it.success) {
                        R.mipmap.duihao
                    } else {
                        R.mipmap.cuo
                    }).into(userMsgCanUseNickname)

                    userMsgChangeBt.isEnabled = !it.success
                }

                changeUserSexAndNickNameLiveData.observe(this@SetUserMsgActivity) {
                    if (it.success) {
                        finish()
                        ToastUtil.setText("修改成功")
                    }
                }

                changeUserInfoResultLiveData.observe(this@SetUserMsgActivity) {
                    if (it.success) {
                        finish()
                        ToastUtil.setText("修改成功")
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        deleteAvatarFile()

    }

    private fun deleteAvatarFile() {
        avatarFile?.listFiles()?.forEach {
            it.delete()
        }
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
    override fun getCurrentViewModel() = SetUserMsgViewModel::class.java


    private fun setWindowAlpha(alpha: Float) {
        val attributes = this.window.attributes

        attributes.alpha = alpha

        this.window.attributes = attributes
    }
}