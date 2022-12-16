package com.example.sunofbeachagain.activity.order

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunofbeachagain.adapter.TopicAdapter
import com.example.sunofbeachagain.databinding.ActivitySelectTopicBinding
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.viewmodel.MoYuMomentViewModel

class SelectTopicActivity : AppCompatActivity() {
    private val moYuMomentViewModel by lazy {
        ViewModelProvider(this)[MoYuMomentViewModel::class.java]
    }
    private val topicAdapter by lazy {
        TopicAdapter()
    }

    private lateinit var activitySelectTopicBinding: ActivitySelectTopicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectTopicBinding = ActivitySelectTopicBinding.inflate(layoutInflater)
        setContentView(activitySelectTopicBinding.root)

        moYuMomentViewModel.getTopicData()

        activitySelectTopicBinding.apply {
            initView()
            initListener()
            initDataListener()
        }

    }


    private fun ActivitySelectTopicBinding.initView() {
        selectTopicRv.layoutManager = LinearLayoutManager(this@SelectTopicActivity)
        selectTopicRv.adapter = topicAdapter
    }

    private fun ActivitySelectTopicBinding.initListener() {
        selectTopicToolbar.setNavigationOnClickListener {
            finish()
        }
        topicAdapter.setOnTopicItemClickListener(object
            : TopicAdapter.OnTopicItemClickListener {
            override fun onTopicItemClick(topicId: String, topicName: String) {
                val intent = Intent()
                val selectTopicData = SelectTopicData(topicId,topicName)
                intent.putExtra(Constant.SOB_TOPIC_DATA,selectTopicData)
                setResult(RESULT_OK,intent)
                finish()
            }

        })
    }

    private fun ActivitySelectTopicBinding.initDataListener() {
        moYuMomentViewModel.topicLiveData.observe(this@SelectTopicActivity) {
            topicAdapter.setData(it)
        }
    }

}

data class SelectTopicData(val id: String, val name: String) : java.io.Serializable