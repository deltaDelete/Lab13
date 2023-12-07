package ru.deltadelete.lab13.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import ru.deltadelete.lab13.R
import ru.deltadelete.lab13.api.retrofit.models.Image
import ru.deltadelete.lab13.databinding.ImageItemBinding


class ImageAdapter(
    private val dataSet: MutableList<Image>
) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    companion object {
        val factory = DrawableCrossFadeFactory
            .Builder().setCrossFadeEnabled(true).build()
    }

    inner class ViewHolder(
        private val binding: ImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {
            val radius = (binding.card.radius - binding.container.paddingStart).toInt()
            if (item.url.endsWith("gif")) {
                Glide.with(binding.root)
                    .asGif()
                    .load(item.url)
                    .transform(RoundedCorners(radius))
                    .into(binding.imageView)
            } else {
                Glide.with(binding.root)
                    .load(item.url)
                    .transform(RoundedCorners(radius))
                    .into(binding.imageView)
            }

            binding.buttonLink.setOnClickListener {
                val clipman =
                    it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("image", item.url)
                clipman.setPrimaryClip(clip)
                Toast.makeText(binding.root.context, R.string.copied, Toast.LENGTH_LONG).show()
            }

            binding.buttonCopy.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ImageItemBinding>(inflater, R.layout.image_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @Synchronized
    fun add(item: Image) {
        dataSet.add(item)
        notifyItemInserted(dataSet.lastIndex)
    }


    @Synchronized
    fun remove(item: Image) {
        val position = dataSet.indexOf(item)
        if (dataSet.remove(item)) {
            notifyItemRemoved(position)
        }
    }

    @Synchronized
    fun addAll(vararg items: Image) {
        val before = dataSet.lastIndex
        dataSet.addAll(items)
        notifyItemRangeInserted(before, dataSet.lastIndex)
    }

    @Synchronized
    fun addAll(items: List<Image>) {
        val before = dataSet.lastIndex
        dataSet.addAll(items)
        notifyItemRangeInserted(before, dataSet.lastIndex)
    }

    @Synchronized
    fun removeAll(vararg items: Image) {
        dataSet.removeAll(items.toSet())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    @Synchronized
    @SuppressLint("NotifyDataSetChanged")
    fun replaceAll(items: List<Image>) {
        dataSet.clear()
        dataSet.addAll(items)
        notifyDataSetChanged()
    }

    val size: Int
        get() = dataSet.size
}