package com.example.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BitmapView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var bitmap : Bitmap
    var paint : Paint
    init {
        bitmap = BitmapFactory.decodeResource(resources,R.drawable.penguin)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.textSize = pxToDp(24).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap,null, Rect(pxToDp(100),pxToDp(100),pxToDp(500),pxToDp(800)),paint)
        canvas?.drawText("Hello world",100f,100f,paint)
    }

    fun pxToDp(px : Int): Int {
        return px * (resources.displayMetrics.density).toInt()
    }
}

