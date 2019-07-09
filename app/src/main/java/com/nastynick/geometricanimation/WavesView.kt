package com.nastynick.geometricanimation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.View

class WavesView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val wavePaint: Paint
    private val waveGap: Float

    private var initialRadius = 0F
    private var maxRadius = 0F
    private val centerPoint = PointF(0F, 0F)

    init {
        val attributesArray = context.obtainStyledAttributes(
            attrs, R.styleable.WavesView, R.attr.wavesStyle, R.style.WaveViewStyle
        )

        waveGap = attributesArray.getDimension(R.styleable.WavesView_waveGap, 50F)
        wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = attributesArray.getColor(R.styleable.WavesView_waveColor, 0)
            strokeWidth = attributesArray.getDimension(R.styleable.WavesView_waveStrokeWidth, 0F)
            style = Paint.Style.STROKE
        }
        attributesArray.recycle()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val centerX = w / 2f
        val centerY = h / 2f
        centerPoint.set(centerX, centerY)
        maxRadius = Math.hypot(centerX.toDouble(), centerY.toDouble()).toFloat()
        initialRadius = waveGap

        Log.i("WavesView", "onSizeChanged: centerX=$centerX, centerY=$centerY, initialRadius=$initialRadius")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.i("WavesView", "onDraw: start")

        var currentRadius = initialRadius
        while (currentRadius < maxRadius) {
            Log.i("WavesView", "onDraw: currentRadius=$currentRadius")
            canvas.drawCircle(centerPoint.x, centerPoint.y, currentRadius, wavePaint)
            currentRadius += waveGap
        }

        Log.i("WavesView", "onDraw: end")

    }
}