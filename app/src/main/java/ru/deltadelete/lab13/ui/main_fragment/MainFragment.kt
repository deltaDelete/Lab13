package ru.deltadelete.lab13.ui.main_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import ru.deltadelete.lab13.adapter.ImageUrlAdapter
import ru.deltadelete.lab13.databinding.FragmentMainBinding
import ru.deltadelete.lab13.utils.addOnScrolled

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

        binding.recyclerView.addOnScrolled { view, dx, dy ->
            if ((binding.recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == binding.recyclerView.adapter!!.itemCount - 1) {
                viewModel.loadMore()
                // TODO: Подгрузка по мере прокрутки 
            }
        }

        viewModel.items.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = ImageUrlAdapter(it.toMutableList())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

