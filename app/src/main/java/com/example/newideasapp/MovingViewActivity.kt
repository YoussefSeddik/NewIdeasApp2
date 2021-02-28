package com.example.newideasapp

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent.*
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.example.newideasapp.databinding.ActivityMovingViewBinding

class MovingViewActivity : AppCompatActivity() {
    private var isScaled = false
    private lateinit var binding: ActivityMovingViewBinding
    private var mHeight = 0f
    private var mWidth = 0f
    private var xCoordinate = 0f
    private var yCoordinate = 0f
    private var newXCoordinate = 0F
    private var newYCoordinate = 0F
    private var direction = Direction.TOP_RIGHT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.movingContainer.postDelayed({
            mHeight = binding.movingContainer.height.toFloat()
            mWidth = binding.movingContainer.width.toFloat()
            setUpMovingController()
            setUpFlippingController()
        }, 1000)
    }

    private fun setUpFlippingController() = with(binding) {
        flipButton.setOnClickListener {
            val viewHeight = it.height
            val viewWidth = it.width
            val widthToAdd = (viewWidth * 1.2 - viewWidth).toFloat()
            val heightToAdd = (viewHeight * 1.35 - viewHeight).toFloat()
            val isXPositive = direction == Direction.TOP_LEFT && direction == Direction.BOTTOM_LEFT
            val isYPositive = direction == Direction.TOP_LEFT && direction == Direction.TOP_RIGHT

            if (isScaled.not()) {
                isScaled = true
                imageToMov.animate().translationX(if (isXPositive) widthToAdd else -widthToAdd)
                    .translationY(if (isYPositive) heightToAdd else -heightToAdd)
                    .scaleX(1.2F)
                    .scaleY(1.2F)
                    .setDuration(1000)
                    .start()
            } else {
                imageToMov.animate().translationX(0F)
                    .translationY(0F)
                    .scaleX(1F)
                    .scaleY(1F)
                    .setDuration(1000)
                    .start()
                isScaled = false
            }
//            val yAngle = imageToMov.rotationY
//            if (yAngle >= 180) {
//                imageToMov.animate().rotationY(0F).setDuration(200).start()
//            } else {
//                imageToMov.animate().rotationY(180F).setDuration(200).start()
//            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpMovingController() = with(binding) {
        imageToMov.setOnTouchListener { view, event ->
            when (event.actionMasked) {
                ACTION_DOWN -> {
                    xCoordinate = imageToMov.x - event.rawX
                    yCoordinate = imageToMov.y - event.rawY
                }
                ACTION_MOVE -> {
                    imageToMov.animate()
                        .x(event.rawX + xCoordinate).y(event.rawY + yCoordinate).setDuration(0)
                        .start()
                }
                ACTION_UP -> {
                    val centerYPos: Float = imageToMov.y + imageToMov.height / 2
                    val centerXPos: Float = imageToMov.x + imageToMov.width / 2

                    if (centerXPos >= mWidth / 2) {
                        newXCoordinate = mWidth - imageToMov.width
                        newYCoordinate = if (centerYPos <= mHeight / 2) {
                            direction = Direction.TOP_RIGHT
                            0F
                        } else {
                            direction = Direction.BOTTOM_RIGHT
                            mHeight - imageToMov.height
                        }
                    } else {
                        newXCoordinate = 0F
                        newYCoordinate = if (centerYPos <= mHeight / 2) {
                            direction = Direction.TOP_LEFT
                            0F
                        } else {
                            direction = Direction.BOTTOM_LEFT
                            mHeight - imageToMov.height
                        }

                    }
                    imageToMov.animate().x(newXCoordinate)
                        .y(newYCoordinate).setDuration(100)
                        .start()
                    return@setOnTouchListener false
                }
                else -> {
                    return@setOnTouchListener false
                }
            }
            return@setOnTouchListener true
        }
    }

    enum class Direction {
        TOP_LEFT, TOP_RIGHT,
        BOTTOM_LEFT, BOTTOM_RIGHT
    }

    fun scaleView(v: View, fromY: Float, toY: Float, fromX: Float, toX: Float) {
        val anim: Animation = ScaleAnimation(
            fromX, toX,  // Start and end values for the X axis scaling
            fromY, toY,  // Start and end values for the Y axis scaling
            Animation.ABSOLUTE, 0f,  // Pivot point of X scaling
            Animation.ABSOLUTE, 1f
        ) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 1000
        v.startAnimation(anim)
    }
}