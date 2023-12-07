package ru.deltadelete.lab13.ui.waifuim_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.deltadelete.lab13.R
import ru.deltadelete.lab13.adapter.ImageAdapter
import ru.deltadelete.lab13.databinding.FragmentWaifuImBinding
import ru.deltadelete.lab13.ui.image_details_fragment.ImageDetailsFragment
import ru.deltadelete.lab13.utils.addOnScrolled

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

        initRecyclerView()
        initChipGroup()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.recyclerView.itemAnimator = null

        binding.recyclerView.addOnScrolled { view, dx, dy ->
            if (dy < 50) return@addOnScrolled
            val linearLayoutManager = view.layoutManager as LinearLayoutManager
            val lastCompletelyVisibleItemPosition =
                linearLayoutManager.findLastCompletelyVisibleItemPosition()
            if (lastCompletelyVisibleItemPosition == RecyclerView.NO_POSITION) return@addOnScrolled

            val adapter = view.adapter as ImageAdapter
            if (lastCompletelyVisibleItemPosition >= adapter.size - 1) {
                Snackbar.make(
                    requireView(),
                    R.string.loading_more,
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.loadMore()
            }
        }

        binding.recyclerView.adapter = ImageAdapter(mutableListOf()).apply {
            onItemClick { _, item, _ ->
                val args = Bundle().apply {
                    val gson = Gson()
                    this.putString(ImageDetailsFragment.IMAGE_ARGUMENT, gson.toJson(item))
                }
                NavHostFragment.findNavController(this@WaifuImFragment)
                    .navigate(R.id.action_WaifuImFragment_to_ImageDetailsFragment, args)
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.items.collect {
                val adapter = binding.recyclerView.adapter as ImageAdapter
                if (it is ItemsCallback.Empty) {
                    adapter.clear()
                }
                if (it is ItemsCallback.NewItems) {
                    if (it.clear) adapter.clear()
                    adapter.addAll(it.items)
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (it) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun initChipGroup() {
        viewModel.tags.observe(viewLifecycleOwner) {
            it.forEach {
                binding.chipGrop.addView(Chip(context).apply {
                    isCheckable = true
                    checkedIcon =
                        AppCompatResources.getDrawable(context, R.drawable.baseline_check_24)
                    isCheckedIconVisible = true
                    text = it
                })
            }
        }
        binding.chipGrop.setOnCheckedStateChangeListener { group: ChipGroup, checkedIds: List<Int> ->
            checkedIds
                .map {
                    if (id < 0) return@map null
                    group.findViewById<Chip>(it)
                }
                .filterNotNull()
                .map { it.text.toString() }
                .let {
                    viewModel.includedTags.postValue(it)
                }
        }
    }
}