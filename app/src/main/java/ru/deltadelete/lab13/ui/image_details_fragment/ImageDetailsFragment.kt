package ru.deltadelete.lab13.ui.image_details_fragment

import android.icu.text.DateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import ru.deltadelete.lab13.R
import ru.deltadelete.lab13.api.retrofit.models.Image
import ru.deltadelete.lab13.databinding.FragmentImageDetailsBinding

class ImageDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ImageDetailsFragment()

        const val IMAGE_ARGUMENT = "IMAGE"
    }

    private lateinit var binding: FragmentImageDetailsBinding
    private val viewModel: ImageDetailsViewModel by viewModels()
    private lateinit var image: Image

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val imgJson = arguments?.getString(IMAGE_ARGUMENT)
        val gson = Gson()
        image = gson.fromJson(imgJson, Image::class.java)
        binding = FragmentImageDetailsBinding.inflate(inflater, container, false)

        val radius = (binding.card.radius).toInt()
        if (image.url.endsWith("gif")) {
            Glide.with(binding.root)
                .asGif()
                .load(image.url)
                .transform(RoundedCorners(radius))
                .into(binding.imageView)
        } else {
            Glide.with(binding.root)
                .load(image.url)
                .transform(RoundedCorners(radius))
                .into(binding.imageView)
        }

        binding.dominantColor.text = getString(R.string.dominant_color, image.dominant_color)
        if (image.artist == null) {
            binding.artist.visibility = View.GONE
        } else {
            binding.artist.text = getString(R.string.author, image.artist?.name)
        }
        binding.link.text = getString(R.string.link, image.url)
        binding.source.text = getString(R.string.source, image.source)
        binding.width.text = getString(R.string.width, image.width)
        binding.height.text = getString(R.string.height, image.height)
        binding.byteSize.text = getString(R.string.byte_size, image.byte_size)
        binding.uploadedAt.text = getString(R.string.uploaded_at, image.uploaded_at.let {
            DateFormat.getDateInstance().format(it)
        })

        return binding.root
    }

}