package com.example.newideasapp

import android.animation.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent.*
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
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
        val oldY = imageToMov.y
        val oldX = imageToMov.x

        flipButton.setOnClickListener {
            val width = imageToMov.width
            val height = imageToMov.height
            //top
            val distanceYToAdd = (height * 1.1 - height).toFloat()
            //right
            val distanceXToAdd = (width * 1.1 - width).toFloat()

            if (isScaled.not()) {
                isScaled = true
                imageToMov.animate().scaleY(1.2F).scaleX(1.2F)
                    .y(imageToMov.y + distanceYToAdd)
                    .x(imageToMov.x - distanceXToAdd).setDuration(1000)
                    .start()

            } else {
                imageToMov.animate().scaleY(1F).scaleX(1F).y(imageToMov.y - distanceYToAdd)
                    .x(imageToMov.x + distanceXToAdd).setDuration(1000)
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

    fun getImageDirection(): Direction = with(binding) {
        val centerYPos: Float = imageToMov.y + imageToMov.height / 2
        val centerXPos: Float = imageToMov.x + imageToMov.width / 2
        return if (centerXPos >= mWidth / 2) {
            return if (centerYPos <= mHeight / 2) {
                Direction.TOP_RIGHT
            } else {
                Direction.BOTTOM_RIGHT
            }
        } else {
            if (centerYPos <= mHeight / 2) {
                Direction.TOP_LEFT
            } else {
                Direction.BOTTOM_LEFT
            }

        }
    }

    private fun scaleView(v: View, fromY: Float, toY: Float, fromX: Float, toX: Float) {
        //x = 0 left, y = 1 ->top
        val requiredPair = when (getImageDirection()) {
            Direction.TOP_RIGHT -> {
                Pair(1F, 0F)
            }
            Direction.TOP_LEFT -> {
                Pair(0F, 0F)
            }
            Direction.BOTTOM_LEFT -> {
                Pair(0F, 1F)
            }
            else -> {
                Pair(1F, 1F)
            }
        }
        val anim: Animation = ScaleAnimation(
            fromX, toX,  // Start and end values for the X axis scaling
            fromY, toY,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, requiredPair.first,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, requiredPair.second // Pivot point of Y scaling
        )
        anim.fillBefore = true // Needed to keep the result of the animation
        anim.isFillEnabled = true
        anim.fillAfter = true
        anim.duration = 1000
        v.startAnimation(anim)
    }
}