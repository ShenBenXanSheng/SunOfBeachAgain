package com.example.sunofbeachagain.activity.order


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.adapter.MoYuCheckImageAdapter
import com.example.sunofbeachagain.domain.CheckImageData
import com.example.sunofbeachagain.utils.Constant
import kotlinx.android.synthetic.main.activity_moyy_check_image.*

class MoYuMomentCheckImageActivity : AppCompatActivity() {


    private lateinit var checkImageData: CheckImageData

    private val moYuCheckImageAdapter by lazy {
        MoYuCheckImageAdapter()
    }

    private val currentImageList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_moyy_check_image)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.gray2)
        window.statusBarColor = ContextCompat.getColor(this, R.color.gray2)

        intent.getSerializableExtra(Constant.SOB_CHECK_IMAGE)?.let {
            checkImageData = it as CheckImageData
        }
        initView()
        initListener()
    }

    private fun initView() {
        moYuCheckImageAdapter.setData(checkImageData.imageList)

        Log.d("TAG", checkImageData.position.toString())


        moyu_check_image_vp.adapter = moYuCheckImageAdapter

        moyu_check_image_vp.currentItem = checkImageData.position

        moyu_check_image_select_tv.text =
            "${checkImageData.position + 1}/${checkImageData.imageList.size}"


        currentImageList.addAll(checkImageData.imageList)

        moyu_check_image_bt.text = "确认(${currentImageList.size})"

        selectImage(checkImageData.position)

    }

    private fun initListener() {
        moyu_check_image_toolbar.setNavigationOnClickListener {
            finish()
        }

        moyu_check_image_vp.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                selectImage(position)
                moyu_check_image_select_tv.text =
                    "${position + 1}/${checkImageData.imageList.size}"
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        moyu_check_image_bt.setOnClickListener {
              val intent = Intent()
              intent.putExtra(Constant.SOB_CHECK_IMAGE, currentImageList as java.io.Serializable)
              setResult(RESULT_OK, intent)
              finish()



        }


    }

    private fun checkAndRemoveImage(checkBox: CheckBox, position: Int) {
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                currentImageList.add(checkImageData.imageList[position])
            } else {
                currentImageList.remove(checkImageData.imageList[position])
            }

            moyu_check_image_bt.text = "确认(${currentImageList.size})"
        }


    }

    private fun selectImage(position: Int) {
        moyu_check_image_cb1.visibility = View.GONE
        moyu_check_image_cb2.visibility = View.GONE
        moyu_check_image_cb3.visibility = View.GONE
        moyu_check_image_cb4.visibility = View.GONE
        moyu_check_image_cb5.visibility = View.GONE
        moyu_check_image_cb6.visibility = View.GONE
        moyu_check_image_cb7.visibility = View.GONE
        moyu_check_image_cb8.visibility = View.GONE
        moyu_check_image_cb9.visibility = View.GONE

        when (position) {
            0 -> {
                moyu_check_image_cb1.visibility = View.VISIBLE
                checkAndRemoveImage(moyu_check_image_cb1, position)

            }

            1 -> {
                moyu_check_image_cb2.visibility = View.VISIBLE
                checkAndRemoveImage(moyu_check_image_cb2, position)

            }

            2 -> {
                moyu_check_image_cb3.visibility = View.VISIBLE
                checkAndRemoveImage(moyu_check_image_cb3, position)

            }

            3 -> {
                moyu_check_image_cb4.visibility = View.VISIBLE
                checkAndRemoveImage(moyu_check_image_cb4, position)

            }

            4 -> {
                moyu_check_image_cb5.visibility = View.VISIBLE
                checkAndRemoveImage(moyu_check_image_cb5, position)

            }

            5 -> {
                moyu_check_image_cb6.visibility = View.VISIBLE
                checkAndRemoveImage(moyu_check_image_cb6, position)

            }

            6 -> {
                moyu_check_image_cb7.visibility = View.VISIBLE
                checkAndRemoveImage(moyu_check_image_cb7, position)

            }

            7 -> {
                moyu_check_image_cb8.visibility = View.VISIBLE
                checkAndRemoveImage(moyu_check_image_cb8, position)

            }

            8 -> {
                moyu_check_image_cb9.visibility = View.VISIBLE
                checkAndRemoveImage(moyu_check_image_cb9, position)
            }
        }
    }

}