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
import ru.deltadelete.lab13.R
import ru.deltadelete.lab13.databinding.ImageItemBinding


class ImageUrlAdapter(
    private val dataSet: MutableList<String>
) :
    RecyclerView.Adapter<ImageUrlAdapter.ViewHolder>() {
    // TODO: пофиксить говнину после загрузки на второй страницы
    inner class ViewHolder(
        private val binding: ImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            val radius = (binding.card.radius - binding.container.paddingStart).toInt()
            if (item.endsWith("gif")) {
                Glide.with(binding.root)
                    .asGif()
                    .load(item)
                    .transform(RoundedCorners(radius))
                    .into(binding.imageView)
            } else {
                Glide.with(binding.root)
                    .load(item)
                    .transform(RoundedCorners(radius))
                    .into(binding.imageView)
            }

            binding.buttonLink.setOnClickListener {
                val clipman =
                    it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("image", item)
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

    fun add(item: String) {
        dataSet.add(item)
    }


    fun remove(item: String) {
        dataSet.remove(item)
    }

    fun addAll(vararg items: String) {
        val start = dataSet.size
        dataSet.addAll(items)
        notifyItemRangeInserted(start, items.size)
    }

    fun addAll(items: List<String>) {
        val start = dataSet.size
        dataSet.addAll(items)
        notifyItemRangeInserted(start, items.size)
    }

    fun removeAll(vararg items: String) {
        dataSet.removeAll(items.toSet())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun replaceAll(items: List<String>) {
        dataSet.clear()
        dataSet.addAll(items)
        notifyDataSetChanged()
    }

    val size: Int
        get() = dataSet.size

}