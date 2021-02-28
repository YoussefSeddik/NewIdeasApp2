package com.example.newideasapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.newideasapp.databinding.ActivityMainBinding
import kotlin.math.hypot

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpController()
    }

    private fun setUpController()= with(binding){
        dragToDismissButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,DragToDismissActivity::class.java))
        }
        moveButton.setOnClickListener {
            startActivity(Intent(this@MainActivity,MovingViewActivity::class.java))
        }
    }
}