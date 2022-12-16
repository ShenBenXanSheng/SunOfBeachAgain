package com.example.sunofbeachagain.activity.order

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.adapter.CheckImageAdapter
import com.example.sunofbeachagain.databinding.ActivityCheckImageBinding
import com.example.sunofbeachagain.domain.CheckImageData
import com.example.sunofbeachagain.utils.Constant


class CheckImageActivity : AppCompatActivity() {
    private lateinit var checkImageData: CheckImageData

    private val TAG = "CheckImageActivity"

    private lateinit var checkImageAdapter: CheckImageAdapter

    private val activityCheckImageBinding by lazy {
        ActivityCheckImageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityCheckImageBinding.root)

        val intent = intent
        checkImageData = intent.getSerializableExtra(Constant.SOB_CHECK_IMAGE) as CheckImageData

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        initView()
        initListener()
    }


    private fun initView() {
        checkImageAdapter = CheckImageAdapter(checkImageData.imageList, this)

        hidePointByListSize(checkImageData.imageList.size)
        activityCheckImageBinding.apply {
            checkImageVp.adapter = checkImageAdapter
            checkImageVp.currentItem = checkImageData.position
            selectPointByPosition(checkImageData.position)
        }
    }

    private fun hidePointByListSize(size: Int) {
        activityCheckImageBinding.apply {

            when (size) {
                1 -> {
                    hideView(View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE)
                }

                2 -> {

                    hideView(View.VISIBLE,
                        View.VISIBLE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE)
                }

                3 -> {


                    hideView(View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE)
                }

                4 -> {


                    hideView(View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE)
                }

                5 -> {

                    hideView(View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.GONE,
                        View.GONE,
                        View.GONE,
                        View.GONE)
                }

                6 -> {

                    hideView(View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.GONE,
                        View.GONE,
                        View.GONE)
                }

                7 -> {

                    hideView(View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.GONE,
                        View.GONE)
                }

                8 -> {


                    hideView(View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.GONE)
                }

                9 -> {
                    hideView(View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE,
                        View.VISIBLE)

                }
            }
        }
    }

    fun hideView(
        point1Visibility: Int,
        point2Visibility: Int,
        point3Visibility: Int,
        point4Visibility: Int,
        point5Visibility: Int,
        point6Visibility: Int,
        point7Visibility: Int,
        point8Visibility: Int,
        point9Visibility: Int,
    ) {
        activityCheckImageBinding.apply {
            checkImagePoint1.visibility = point1Visibility
            checkImagePoint2.visibility = point2Visibility
            checkImagePoint3.visibility = point3Visibility
            checkImagePoint4.visibility = point4Visibility
            checkImagePoint5.visibility = point5Visibility
            checkImagePoint6.visibility = point6Visibility
            checkImagePoint7.visibility = point7Visibility
            checkImagePoint8.visibility = point8Visibility
            checkImagePoint9.visibility = point9Visibility
        }

    }

    private fun initListener() {
        activityCheckImageBinding.apply {
            checkImageVp.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {

                }

                override fun onPageSelected(position: Int) {
                    selectPointByPosition(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })
        }
    }

    private fun selectPointByPosition(position: Int) {
        activityCheckImageBinding.apply {
            checkImagePoint1.isSelected = false
            checkImagePoint2.isSelected = false
            checkImagePoint3.isSelected = false
            checkImagePoint4.isSelected = false
            checkImagePoint5.isSelected = false
            checkImagePoint6.isSelected = false
            checkImagePoint7.isSelected = false
            checkImagePoint8.isSelected = false
            checkImagePoint9.isSelected = false


            when(position){
                0->{
                    checkImagePoint1.isSelected = true
                }

                1->{
                    checkImagePoint2.isSelected = true
                }

                2->{
                    checkImagePoint3.isSelected = true
                }

                3->{
                    checkImagePoint4.isSelected = true
                }

                4->{
                    checkImagePoint5.isSelected = true
                }

                5->{
                    checkImagePoint6.isSelected = true
                }

                6->{
                    checkImagePoint7.isSelected = true
                }

                7->{
                    checkImagePoint8.isSelected = true
                }

                8->{
                    checkImagePoint9.isSelected = true
                }
            }
        }
    }


}