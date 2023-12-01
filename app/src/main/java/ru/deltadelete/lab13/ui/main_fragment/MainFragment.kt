package ru.deltadelete.lab13.ui.main_fragment

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import ru.deltadelete.lab13.adapter.ImageAdapter
import ru.deltadelete.lab13.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        viewModel.items.observe(viewLifecycleOwner) {
//            (binding.recyclerView.adapter as ImageAdapter).clear()
//            (binding.recyclerView.adapter as ImageAdapter).addAll(it)
//            (binding.recyclerView.adapter as ImageAdapter).notifyDataSetChanged()
            binding.recyclerView.adapter = ImageAdapter(it.toMutableList())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun <T> LiveData<T>.observe(
        lifecycleOwner: LifecycleOwner,
        observer: (T) -> Unit
    ) {
        this.observe(lifecycleOwner, observer)
    }
}

