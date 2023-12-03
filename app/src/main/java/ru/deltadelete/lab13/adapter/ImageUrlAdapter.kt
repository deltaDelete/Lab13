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
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.deltadelete.lab13.R
import ru.deltadelete.lab13.databinding.ImageItemBinding


class ImageUrlAdapter(
    private val dataSet: MutableList<String>
) :
    RecyclerView.Adapter<ImageUrlAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            val radius = binding.card.radius - binding.container.paddingStart / 2
            binding.imageView.load(
                data = item,
                builder = {
                    this.transformations(RoundedCornersTransformation(radius))
                }
            )

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