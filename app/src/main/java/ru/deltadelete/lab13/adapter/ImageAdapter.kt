package ru.deltadelete.lab13.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.PointF
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.deltadelete.lab13.R
import ru.deltadelete.lab13.databinding.ImageItemBinding
import ru.deltadelete.lab13.utils.animateWithDetach
import ru.deltadelete.lab13.utils.scale
import ru.deltadelete.lab13.utils.setPivot


class ImageAdapter(
    private val dataSet: MutableList<String>
) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.imageView.load(
                data = item,
//                builder = {
//                    target(
//                        onStart = {
//                            binding.indicator.isIndeterminate = true
//                            binding.indicator.visibility = View.VISIBLE
//                        },
//                        onError = {
//                            binding.indicator.visibility = View.GONE
//                        },
//                        onSuccess = {
//                            binding.indicator.visibility = View.GONE
//                        }
//                    ).data(item)
//                }
            )

            binding.card.setOnLongClickListener {
                val clipman =
                    it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("image", item)
                clipman.setPrimaryClip(clip)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ImageItemBinding>(inflater, R.layout.image_item, parent, false)
        PinchToZoom(binding.container, binding.imageView)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun add(item: String) {
        dataSet.add(item)
    }


    fun remove(item: String) {
        dataSet.remove(item)
    }

    fun addAll(vararg items: String) {
        val before = dataSet.lastIndex
        dataSet.addAll(items)
        notifyItemRangeInserted(before, dataSet.lastIndex)
    }

    fun addAll(items: List<String>) {
        val before = dataSet.lastIndex
        dataSet.addAll(items)
        notifyItemRangeInserted(before, dataSet.lastIndex)
    }

    fun removeAll(vararg items: String) {
        dataSet.removeAll(items.toSet())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        dataSet.clear()
        notifyDataSetChanged()
    }
}

class PinchToZoom(
    private val parent: View,
    private val pinchable: ImageView
) {

    init {
        pinchable.doOnLayout { originContentRect }
        parent.setOnTouchListener { view, event ->
            val first = scaleGestureDetector.onTouchEvent(event)
            val second = translationHandler.onTouch(view, event)
            if (first && second) {
                true
            } else {
                view.performClick()
            }
        }
    }

    private val originContentRect by lazy {
        pinchable.run {
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
                if (event == null || (pinchable.scaleX ?: 1f) == 1f) return false

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
                        pinchable.run {
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
                pinchable.run {
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
                pinchable.run {
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
            pinchable.animateWithDetach()
                .translationXBy(translation.x)
                .translationYBy(translation.y)
                .apply { duration = CORRECT_LOCATION_ANIMATION_DURATION }
                .start()
        }
    }

    private fun getContentViewTranslation(): PointF {
        return pinchable.run {
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
        private const val MAX_SCALE_FACTOR = 5f
        private const val MIN_SCALE_FACTOR = 1f
        private const val CORRECT_LOCATION_ANIMATION_DURATION = 300L
    }
}