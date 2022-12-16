package com.example.sunofbeachagain.activity.moyu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.MoYuSubCommentAdapter
import com.example.sunofbeachagain.databinding.ActivityMoYuSubCommentBinding
import com.example.sunofbeachagain.domain.ItemSubCommentData
import com.example.sunofbeachagain.domain.bean.MoYuComment
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.NetWorkUtils
import com.example.sunofbeachagain.utils.ToastUtil

class MoYuSubCommentActivity : AppCompatActivity() {
    private val activityMoYuSubCommentBinding by lazy {
        ActivityMoYuSubCommentBinding.inflate(layoutInflater)
    }

    private lateinit var moYuComment: MoYuComment


    private val moYuSubCommentAdapter by lazy {
        MoYuSubCommentAdapter()
    }

    private var currentToken = ""

    private val subCommentList = mutableListOf<ItemSubCommentData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMoYuSubCommentBinding.root)

        intent.getSerializableExtra(Constant.SOB_DETAIL_TO_SUB_COMMENT_DATA).let {
            moYuComment = it as MoYuComment
        }

        intent.getStringExtra(Constant.SOB_TOKEN)?.let {
            currentToken = it
        }

        moYuComment.apply {
            val firstSubComment = ItemSubCommentData(avatar,
                nickname,
                position,
                createTime,
                "楼主",
                content, userId, momentId, id)
            subCommentList.add(firstSubComment)


            subComments.forEach {
                val subCommentData = ItemSubCommentData(
                    it.avatar,
                    it.nickname,
                    it.position,
                    it.createTime,
                    it.targetUserNickname,
                    it.content,
                    it.userId,
                    momentId,
                    it.commentId
                )

                subCommentList.add(subCommentData)
            }
        }

        moYuSubCommentAdapter.setData(subCommentList)

        activityMoYuSubCommentBinding.moyuSubCommentRv.adapter = moYuSubCommentAdapter

        activityMoYuSubCommentBinding.moyuSubCommentToolbar.setNavigationOnClickListener {
            finish()
        }

        moYuSubCommentAdapter.setOnMoYuSubCommentItemClickListener(object
            :MoYuSubCommentAdapter.OnMoYuSubCommentItemClickListener{
            override fun onMoYuSubCommentItemClick(itemSubCommentData: ItemSubCommentData) {
                val intent = Intent(this@MoYuSubCommentActivity, UserCenterActivity::class.java)

                intent.putExtra(Constant.SOB_USER_ID, itemSubCommentData.userId)

                intent.putExtra(Constant.SOB_TOKEN, currentToken)

                startActivity(intent)

            }

        })
    }
}