package ru.deltadelete.lab13.adapter

import android.graphics.PointF
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import androidx.core.view.doOnLayout
import ru.deltadelete.lab13.utils.animateWithDetach
import ru.deltadelete.lab13.utils.scale
import ru.deltadelete.lab13.utils.setPivot

class asdasda(
    private val parent: View,
    private val punchable: ImageView
) {

    init {
        punchable.doOnLayout { originContentRect }
        parent.setOnTouchListener { view, event ->
            val first = scaleGestureDetector.onTouchEvent(event)
            val second = translationHandler.onTouch(view, event)
            if (first && second) {
                view.performClick()
            } else {
                true
            }
        }
    }

    private val originContentRect by lazy {
        punchable.run {
            val array = IntArray(2)
            getLocationOnScreen(array)
            Rect(array[0], array[1], array[0] + width, array[1] + height)
        }
    }

    private val translationHandler by lazy {
        object : View.OnTouchListener {
            private var prevX = 0f
            private var prevY = 0f
            private var moveStarted = false
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                Log.d("PinchToZoom", "Detected touch gesture")
                if (event == null || (punchable.scaleX ?: 1f) == 1f) return false

                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        prevX = event.x
                        prevY = event.y
                    }

                    MotionEvent.ACTION_POINTER_UP -> {
                        if (event.actionIndex == 0) {
                            try {
                                prevX = event.getX(1)
                                prevY = event.getY(1)
                            } catch (e: Exception) {
                            }
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (event.pointerCount > 1) {
                            prevX = event.x
                            prevY = event.y
                            return false
                        }
                        moveStarted = true
                        punchable.run {
                            translationX += (event.x - prevX)
                            translationY += (event.y - prevY)
                        }
                        prevX = event.x
                        prevY = event.y
                    }

                    MotionEvent.ACTION_UP -> {
                        if (!moveStarted) return false
                        reset()
                        translateToOriginalRect()
                    }
                }
                return true
            }

            private fun reset() {
                prevX = 0f
                prevY = 0f
                moveStarted = false
            }
        }
    }

    private val scaleGestureDetector by lazy {
        ScaleGestureDetector(parent.context, object : ScaleGestureDetector.OnScaleGestureListener {
            var totalScale = 1f

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                punchable.run {
                    val actualPivot = PointF(
                        (detector.focusX - translationX + pivotX * (totalScale - 1)) / totalScale,
                        (detector.focusY - translationY + pivotY * (totalScale - 1)) / totalScale,
                    )

                    translationX -= (pivotX - actualPivot.x) * (totalScale - 1)
                    translationY -= (pivotY - actualPivot.y) * (totalScale - 1)
                    setPivot(actualPivot)
                }
                return true
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                totalScale *= detector.scaleFactor
                totalScale = totalScale.coerceIn(MIN_SCALE_FACTOR, MAX_SCALE_FACTOR)
                punchable.run {
                    scale(totalScale)
                    getContentViewTranslation().run {
                        translationX += x
                        translationY += y
                    }
                }
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) = Unit
        })
    }

    private fun translateToOriginalRect() {
        getContentViewTranslation().takeIf { it != PointF(0f, 0f) }?.let { translation ->
            punchable.animateWithDetach()
                .translationXBy(translation.x)
                .translationYBy(translation.y)
                .apply { duration = CORRECT_LOCATION_ANIMATION_DURATION }
                .start()
        }
    }

    private fun getContentViewTranslation(): PointF {
        return punchable.run {
            originContentRect.let { rect ->
                val array = IntArray(2)
                getLocationOnScreen(array)
                PointF(
                    when {
                        array[0] > rect.left -> rect.left - array[0].toFloat()
                        array[0] + width * scaleX < rect.right -> rect.right - (array[0] + width * scaleX)
                        else -> 0f
                    },
                    when {
                        array[1] > rect.top -> rect.top - array[1].toFloat()
                        array[1] + height * scaleY < rect.bottom -> rect.bottom - (array[1] + height * scaleY)
                        else -> 0f
                    }
                )
            }
        }
    }

    companion object {
        private const val MAX_SCALE_FACTOR = 10f
        private const val MIN_SCALE_FACTOR = 1f
        private const val CORRECT_LOCATION_ANIMATION_DURATION = 300L
    }
}