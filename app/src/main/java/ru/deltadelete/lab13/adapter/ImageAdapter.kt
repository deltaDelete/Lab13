package ru.deltadelete.lab13.adapter

import android.annotation.SuppressLint
import android.media.Image
import android.os.Build.VERSION.SDK_INT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.load
import coil.memory.MemoryCache
import coil.request.ImageRequest
import ru.deltadelete.lab13.R
import ru.deltadelete.lab13.databinding.ImageItemBinding

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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ImageItemBinding>(inflater, R.layout.image_item, parent, false)
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