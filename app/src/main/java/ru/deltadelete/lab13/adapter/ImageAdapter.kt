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
import ru.deltadelete.lab13.api.retrofit.models.Image
import ru.deltadelete.lab13.databinding.ImageItemBinding


class ImageAdapter(
    private val dataSet: MutableList<Image>
) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {
            val radius = binding.card.radius - binding.container.paddingStart / 2
            binding.imageView.load(
                data = item.url,
                builder = {
                    this.transformations(RoundedCornersTransformation(radius))
                }
            )
            // TODO: Glide

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

    fun add(item: Image) {
        dataSet.add(item)
    }


    fun remove(item: Image) {
        dataSet.remove(item)
    }

    fun addAll(vararg items: Image) {
        val before = dataSet.lastIndex
        dataSet.addAll(items)
        notifyItemRangeInserted(before, dataSet.lastIndex)
    }

    fun addAll(items: List<Image>) {
        val before = dataSet.lastIndex
        dataSet.addAll(items)
        notifyItemRangeInserted(before, dataSet.lastIndex)
    }

    fun removeAll(vararg items: Image) {
        dataSet.removeAll(items.toSet())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun replaceAll(items: List<Image>) {
        dataSet.clear()
        dataSet.addAll(items)
        notifyDataSetChanged()
    }

    val size: Int
        get() = dataSet.size
}