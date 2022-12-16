package com.example.sunofbeachagain.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.NestedScrollView
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.databinding.ActivityUserCenterBinding
import com.example.sunofbeachagain.utils.UtilBigDecimal

class UserCenterNestedScrollView : NestedScrollView {
    private lateinit var activityUserCenterBinding: ActivityUserCenterBinding

    private lateinit var userCenterActivity: UserCenterActivity

    private var headViewHeight: Int = 0
    private var scrollSize: Int = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(
        context,
        attributeSet,
        0
    )

    constructor(context: Context, attributeSet: AttributeSet?, defaultAttr: Int) : super(
        context,
        attributeSet,
        defaultAttr
    )


    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        activityUserCenterBinding.apply {
            if (headViewHeight > scrollSize) {
                scrollBy(dx, dy)
                consumed[0] = dx
                consumed[1] = dy

                if (scrollSize == 0) {
                    userCenterToolbar2Container.alpha = 0f

                    userCenterToolbar1Container.visibility = View.VISIBLE
                } else {
                    userCenterToolbar1Container.visibility = View.GONE
                    userCenterToolbar2Container.alpha =
                        UtilBigDecimal.div(scrollSize.toDouble(), 1000.toDouble()).toFloat()
                }
            } else {
                userCenterToolbar2Container.alpha = 1f
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        this.scrollSize = t
    }

    fun getHeadViewHeight(i: Int) {
        this.headViewHeight = i
    }

    fun getActivityAndDataBinding(
        userCenterActivity: UserCenterActivity,
        activityUserCenterBinding: ActivityUserCenterBinding,
    ) {
        this.userCenterActivity = userCenterActivity
        this.activityUserCenterBinding = activityUserCenterBinding
    }


}