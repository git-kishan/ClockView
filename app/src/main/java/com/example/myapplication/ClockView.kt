package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ClockView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var viewHeight = 0
    var viewWidth = 0

    var clockCenterX = 0
    var clockCenterY = 0
    var clockRadius = 0

    val padding = 50
    var paint: Paint
    var linePaint: Paint
    var bounds: Rect

    var secHandLength = 0.0
    var minHandLength = 0.0
    var hourHandLength = 0.0

    private val angleDiff = Math.toDegrees(Math.PI / 6).toFloat() // 30 degree

    val timeArray = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    init {
        viewHeight = height
        viewWidth = width
        paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            strokeJoin = Paint.Join.ROUND
            textSize = 60f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        linePaint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }
        bounds = Rect()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        clockCenterX = viewWidth / 2
        clockCenterY = viewHeight / 2
        clockRadius = minOf(viewHeight, viewWidth) / 2 - padding
        secHandLength = 0.85 * clockRadius
        minHandLength = 0.70 * clockRadius
        hourHandLength = 0.55 * clockRadius

    }

    fun next() {
        invalidate()
    }

    private fun setPaintStyle(
        color: Int = Color.BLACK, style: Paint.Style = Paint.Style.FILL,
        strokeWidth: Int = 0, cap: Paint.Cap = Paint.Cap.ROUND
    ) {
        paint.apply {
            this.color = color
            this.style = style
            if (strokeWidth > 0) {
                this.strokeWidth = strokeWidth.toFloat()
            }
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            this.strokeCap = cap
        }
    }

    private fun getTime(): List<Int> {
        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        hour = if (hour > 12) (hour - 12) else hour
        return listOf(hour, min, second)

    }

    private fun drawNumbers(canvas: Canvas) {
        canvas.apply {
            translate(clockCenterX.toFloat(), clockCenterY.toFloat())
            setPaintStyle(style = Paint.Style.FILL, strokeWidth = 0)

            val r = clockRadius - 0.22 * clockRadius
            val theta = -2 * angleDiff

            timeArray.forEachIndexed { index, it ->
                paint.getTextBounds(it.toString(), 0, it.toString().length, bounds)
                var x = r * cos(Math.toRadians(theta.toDouble() + index * angleDiff))
                val y =
                    r * sin(Math.toRadians(theta.toDouble() + index * angleDiff)) + bounds.height() / 2
                if (it % 6 != 0) {
                    if (it > 6) {
                        x -= bounds.width() / 2
                    } else {
                        x += bounds.width() / 2
                    }
                }
                drawText(it.toString(), x.toFloat(), y.toFloat(), paint)
            }
        }
    }

    private fun drawBoundaryAndCenter(canvas: Canvas) {
        canvas.apply {
            drawCircle(clockCenterX.toFloat(), clockCenterY.toFloat(), 30f, paint)
            setPaintStyle(style = Paint.Style.STROKE, strokeWidth = 40)
            drawCircle(clockCenterX.toFloat(), clockCenterY.toFloat(), clockRadius.toFloat(), paint)
            val DIGIT_DASH_WIDTH = 16f
            val DASH_WIDTH = 6f
            save()
            translate(clockCenterX.toFloat(), clockCenterY.toFloat())
            var currX = 0f
            var currY = 0f

//            for (i in 0..60) {
//                if (i % 5 == 0) {
//                    currX = clockRadius - 20 - DIGIT_DASH_WIDTH
//                    setPaintStyle(strokeWidth = 20, cap = Paint.Cap.SQUARE)
//                    drawLine(currX, currY, currX + DIGIT_DASH_WIDTH, currY, paint)
//
//                } else {
//                    currX = clockRadius - 20 - DASH_WIDTH
//                    setPaintStyle(strokeWidth = 10, cap = Paint.Cap.SQUARE)
//                    drawLine(currX, currY, currX + DASH_WIDTH, currY, paint)
//
//                }
//                rotate((6.0).toFloat())
//            }
            restore()
        }
    }

    private fun drawHands(canvas: Canvas, theta: Double, handsLength: Double,isSecondHand : Boolean = false) {
        var currX = 0.0
        var currY = 0.0

        if(isSecondHand){
            currX = 100 * cos(Math.toRadians(theta+180.0))
            currY = 100 *  sin(Math.toRadians(theta+180))
        }
        val destX = handsLength * cos(Math.toRadians(theta))
        val destY = handsLength * sin(Math.toRadians(theta))
        canvas.drawLine(
            currX.toFloat(),
            currY.toFloat(),
            destX.toFloat(),
            destY.toFloat(),
            paint
        )
    }

    private fun drawSecondHand(canvas: Canvas) {
        val currentTime = getTime()

        var theta = currentTime[2] * 6.0 - 90.0  // currentTime[2] is second
        setPaintStyle(style = Paint.Style.FILL, strokeWidth = 10)
        drawHands(canvas, theta, secHandLength,true)

        setPaintStyle(style = Paint.Style.FILL, strokeWidth = 18)
        theta = currentTime[1] * 6.0 + (theta.toFloat() / 360) * 6 - 90f // currentTime[1] is minute
        drawHands(canvas, theta, minHandLength)

        setPaintStyle(style = Paint.Style.FILL, strokeWidth = 26)
        theta =
            currentTime[0] * 30.0 + (theta.toFloat() / 360) * 30.0 - 90 // currentTime[0] is hour
        drawHands(canvas, theta, hourHandLength)

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawBoundaryAndCenter(this)
            save()
            drawNumbers(this)
            restore()
            save()
            translate(clockCenterX.toFloat(), clockCenterY.toFloat())
            drawSecondHand(this)
            restore()
        }
    }


}