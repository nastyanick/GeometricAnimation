package com.nastynick.geometricanimation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class WavesView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val wavePaint: Paint
    private val waveGap: Float
    private val waveAnimationDuration: Long

    private lateinit var wavesAnimator: ValueAnimator

    private var initialRadius = 0F
    private var maxRadius = 0F
    private val centerPoint = PointF(0F, 0F)
    private var waveRaduisOffset = 0f

    init {
        val attributesArray = context.obtainStyledAttributes(
            attrs, R.styleable.WavesView, R.attr.wavesStyle, R.style.WaveViewStyle
        )

        waveGap = attributesArray.getDimension(R.styleable.WavesView_waveGap, 50F)
        waveAnimationDuration = attributesArray.getInt(R.styleable.WavesView_waveAnimationDuration, 1500).toLong()
        wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = attributesArray.getColor(R.styleable.WavesView_waveColor, 0)
            strokeWidth = attributesArray.getDimension(R.styleable.WavesView_waveStrokeWidth, 0F)
            style = Paint.Style.STROKE
        }
        attributesArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        wavesAnimator = ValueAnimator.ofFloat(0f, waveGap).apply {
            duration = waveAnimationDuration
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
        wavesAnimator.addUpdateListener {
            waveRaduisOffset = it.animatedValue as Float
            postInvalidateOnAnimation()
        }
        wavesAnimator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        wavesAnimator.cancel()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val centerX = w / 2f
        val centerY = h / 2f
        centerPoint.set(centerX, centerY)
        maxRadius = Math.hypot(centerX.toDouble(), centerY.toDouble()).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var currentRadius = initialRadius + waveRaduisOffset
        while (currentRadius < maxRadius) {
            canvas.drawCircle(centerPoint.x, centerPoint.y, currentRadius, wavePaint)
            currentRadius += waveGap
        }
    }
}