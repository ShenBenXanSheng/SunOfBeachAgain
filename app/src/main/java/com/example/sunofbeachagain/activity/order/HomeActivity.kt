package com.example.sunofbeachagain.activity.order

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.base.BaseActivity
import com.example.sunofbeachagain.base.BaseApp
import com.example.sunofbeachagain.databinding.ActivityHomeBinding
import com.example.sunofbeachagain.utils.NetWorkUtils
import com.example.sunofbeachagain.viewmodel.MessageViewModel
import java.util.*

class HomeActivity : BaseActivity() {
    private lateinit var onHomeDoubleClickListener: OnHomeDoubleClickListener

    private lateinit var homeBinding: ActivityHomeBinding


    override fun getSuccessView(): View {
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)

        return homeBinding.root
    }

    private val notHasNet = Timer()

    private val hasNet = Timer()

    var startHasNet = false

    var startNotHasNet = true

    private val upSp = BaseApp.mContext?.getSharedPreferences("UpSp", MODE_PRIVATE)

    private lateinit var messageIcon: MenuItem
    private val permissionList = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    val messageViewModel by lazy {
        ViewModelProvider(this)[MessageViewModel::class.java]
    }

    override fun initView() {
        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)

        setToolBarTheme(true, null, false)

        setMyTAG(this::class.java.simpleName)

        notHasNet.schedule(object : TimerTask() {
            override fun run() {
                if (!NetWorkUtils.isConnected(this@HomeActivity)) {
                    if (startNotHasNet) {
                        loginViewModel.checkToken("exit")
                        startHasNet = true
                        startNotHasNet = false
                    }
                }
            }
        }, 0, 1 * 1000)

        hasNet.schedule(object : TimerTask() {
            override fun run() {
                if (NetWorkUtils.isConnected(this@HomeActivity)) {
                    if (startHasNet) {
                        loginViewModel.checkToken(firsttoken)
                        startHasNet = false
                        startNotHasNet = true
                    }

                }
            }
        }, 0, 1 * 1000)


        val readPermission = checkSelfPermission(permissionList[0])

        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissionList, 1)
        }

        deleteImageFile()

        messageViewModel.getMessageCount(firsttoken)

        messageIcon = homeBinding.homeBottomNav.menu.findItem(R.id.fragment_message)


    }

    private fun deleteImageFile() {
        val externalFilesDirs = getExternalFilesDir(null)

        externalFilesDirs?.listFiles()?.forEach {
            if (it.exists()) {
                it.delete()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    AlertDialog.Builder(this)
                        .setTitle("监测到没有读写等权限")
                        .setMessage("是否申请权限?(点击空白取消)")
                        .setCancelable(true)
                        .setPositiveButton("跳转申请", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", packageName, null)

                                intent.data = uri

                                startActivityForResult(intent, 5)
                            }

                        })
                        .show()
                }
            }
        }
    }

    private lateinit var gestureDetector: GestureDetector

    override fun initListener() {
        super.initListener()

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if (this@HomeActivity::onHomeDoubleClickListener.isInitialized) {
                    onHomeDoubleClickListener.onHomeDoubleClick()

                }
                return true
            }

        })

        val findNavController = findNavController(R.id.home_fragment_container)

        homeBinding.apply {
            homeBottomNav.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.fragment_moyu -> {
                        findNavController.navigate(R.id.fragment_moyu)
                    }

                    R.id.fragment_essay -> {
                        findNavController.navigate(R.id.fragment_essay)
                    }

                    R.id.fragment_question -> {
                        findNavController.navigate(R.id.fragment_question)
                    }

                    R.id.fragment_message -> {
                        findNavController.navigate(R.id.fragment_message)
                    }

                    R.id.fragment_user -> {
                        findNavController.navigate(R.id.fragment_user)
                    }
                }
                true
            }

            homeBottomNav.setItemOnTouchListener(R.id.fragment_moyu, object : OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if (event != null) {
                        gestureDetector.onTouchEvent(event)
                    }


                    return false
                }
            })


            homeBottomNav.setItemOnTouchListener(R.id.fragment_essay, object : OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if (event != null) {
                        gestureDetector.onTouchEvent(event)
                    }
                    return false
                }
            })

            homeBottomNav.setItemOnTouchListener(R.id.fragment_question, object : OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if (event != null) {
                        gestureDetector.onTouchEvent(event)
                    }
                    return false
                }
            })




        }
    }


    override fun initDataListener() {
        super.initDataListener()

        messageViewModel.messageCountLiveData.observe(this) {
            if (it != null) {
                if (it.systemMsgCount != 0 ||
                    it.atMsgCount != 0 ||
                    it.momentCommentCount != 0 ||
                    it.thumbUpMsgCount != 0 ||
                    it.shareMsgCount != 0 ||
                    it.wendaMsgCount != 0 ||
                    it.articleMsgCount != 0
                ) {


                    messageIcon.setIcon(R.mipmap.hasxiaoxi)
                } else {
                    messageIcon.setIcon(R.mipmap.xiaoxi)
                }
            }
        }
    }


    fun setOnHomeDoubleClickListener(onHomeDoubleClickListener: OnHomeDoubleClickListener) {
        this.onHomeDoubleClickListener = onHomeDoubleClickListener
    }

    interface OnHomeDoubleClickListener {
        fun onHomeDoubleClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        upSp?.edit()?.clear()?.apply()
    }

}



