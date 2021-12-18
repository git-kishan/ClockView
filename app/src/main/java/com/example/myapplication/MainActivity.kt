package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.SeekBar
import java.time.Clock
import java.util.*

class MainActivity : AppCompatActivity() {
    var timer : Timer? = null
    var clockView : ClockView? = null

    private val timerTask = object : TimerTask(){
        override fun run() {
            clockView?.next()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clockView = findViewById(R.id.clock_view)
        timer = Timer().apply {
            scheduleAtFixedRate(timerTask,0,1000)
        }



    }



}