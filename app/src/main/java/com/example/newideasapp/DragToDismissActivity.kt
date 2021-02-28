package com.example.newideasapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.newideasapp.databinding.ActivityDragToDismissBinding
import kotlin.math.hypot


class DragToDismissActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDragToDismissBinding
    private var xCoOrdinate = 0f
    private var yCoOrdinate = 0f
    private var screenCenterX = 0.0
    private var screenCenterY = 0.0
    private var alpha = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDragToDismissBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpMovingController()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpMovingController() = with(binding) {
        binding.layout.background.alpha = 255
        val display = resources.displayMetrics
        screenCenterX = (display.widthPixels / 2).toDouble()
        screenCenterY = ((display.heightPixels - getStatusBarHeight()) / 2).toDouble()
        val maxHypo = hypot(screenCenterX, screenCenterY);

        tempImage.setOnTouchListener { view, event ->
            /**
             * Calculate hypo value of current imageview position according to center
             */
            /**
             * Calculate hypo value of current imageview position according to center
             */
            val centerYPos: Float = tempImage.y + tempImage.height / 2
            val centerXPos: Float = tempImage.x + tempImage.width / 2
            val a = screenCenterX - centerXPos
            val b = screenCenterY - centerYPos
            val hypo = hypot(a, b)

            /**
             * change alpha of background of layout
             */

            /**
             * change alpha of background of layout
             */
            alpha = (hypo * 255 / maxHypo).toInt()
            if (alpha < 255) {
                layout.background.alpha = 255 - alpha
            }

            when (event.actionMasked) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    xCoOrdinate = tempImage.x - event.rawX
                    yCoOrdinate = tempImage.y - event.rawY
                }
                android.view.MotionEvent.ACTION_MOVE -> tempImage.animate()
                    .x(event.rawX + xCoOrdinate).y(event.rawY + yCoOrdinate).setDuration(0).start()
                android.view.MotionEvent.ACTION_UP -> {
                    if (alpha > 100) {
                        supportFinishAfterTransition()
                        layout.background.alpha = 255
                        return@setOnTouchListener false
                    } else {
                        tempImage.animate().x(0F)
                            .y(screenCenterY.toFloat() - tempImage.height / 2).setDuration(100)
                            .start()
                        layout.background.alpha = 255
                    }
                    return@setOnTouchListener false
                }
                else -> {
                    return@setOnTouchListener false
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}