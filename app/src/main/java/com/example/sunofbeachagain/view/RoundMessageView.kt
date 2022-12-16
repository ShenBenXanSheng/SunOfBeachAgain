package com.example.sunofbeachagain.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class RoundMessageView : View {
    private var textSize: Float = 0f

    private var text: String = ""

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var textPaint: Paint = Paint()

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
    ) {
        paint.color = Color.RED

        textPaint.color = Color.WHITE
        textPaint.textSize = 22f

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = width / 2;
        val height = height / 2;

        val radius = width.coerceAtMost(height) / 2;
        canvas?.drawCircle((width).toFloat(), (height).toFloat(), radius.toFloat(), paint);

        canvas?.drawText(text,
            (width).toFloat() - (textPaint.measureText(text) / 2),
            (height).toFloat() + textPaint.textSize / 3,
            textPaint)
    }

    fun setRoundMessageText(text: String) {
        this.text = text
    }


}