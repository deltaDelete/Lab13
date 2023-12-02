package ru.deltadelete.lab13.ui.waifuim_fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.deltadelete.lab13.R
import ru.deltadelete.lab13.adapter.ImageAdapter
import ru.deltadelete.lab13.databinding.FragmentWaifuImBinding

class WaifuImFragment : Fragment() {

    companion object {
        fun newInstance() = WaifuImFragment()
    }

    private lateinit var binding: FragmentWaifuImBinding
    private val viewModel: WaifuImViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaifuImBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        viewModel.items.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = ImageAdapter(it.toMutableList())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}