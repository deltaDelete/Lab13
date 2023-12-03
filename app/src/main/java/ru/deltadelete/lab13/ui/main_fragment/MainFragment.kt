package ru.deltadelete.lab13.ui.main_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import ru.deltadelete.lab13.R
import ru.deltadelete.lab13.adapter.ImageUrlAdapter
import ru.deltadelete.lab13.api.WaifuPicsApi
import ru.deltadelete.lab13.databinding.FragmentMainBinding
import ru.deltadelete.lab13.utils.addOnScrolled


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private val categories = WaifuPicsApi.Categories.entries
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
            if (dy < 50) return@addOnScrolled
            val linearLayoutManager = view.layoutManager as LinearLayoutManager
            val lastCompletelyVisibleItemPosition =
                linearLayoutManager.findLastCompletelyVisibleItemPosition()
            if (lastCompletelyVisibleItemPosition == RecyclerView.NO_POSITION) return@addOnScrolled

            val adapter = view.adapter as ImageUrlAdapter
            if (lastCompletelyVisibleItemPosition + 1 >= adapter.size - 1) {
                Snackbar.make(
                    requireView(),
                    R.string.loading_more,
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.loadMore()
            }
        }

        binding.recyclerView.adapter = ImageUrlAdapter(mutableListOf())
        viewModel.items.observe(viewLifecycleOwner) {
            val adapter = binding.recyclerView.adapter as ImageUrlAdapter
            adapter.replaceAll(it)
        }
        viewModel.moreItems.observe(viewLifecycleOwner) {
            val adapter = binding.recyclerView.adapter as ImageUrlAdapter
            adapter.addAll(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (it) View.INVISIBLE else View.VISIBLE
        }

        categories.forEach {
            binding.chipGrop.addView(Chip(context).apply {
                isCheckable = true
                checkedIcon = AppCompatResources.getDrawable(context, R.drawable.baseline_check_24)
                isCheckedIconVisible = true
                if (it == WaifuPicsApi.Categories.waifu) {
                    isChecked = true
                }
                text = it.name
            })
        }
        binding.chipGrop.setOnCheckedChangeListener { group, checkedIds ->
            if (checkedIds < 0) return@setOnCheckedChangeListener
            val chip = group.findViewById<Chip>(checkedIds)
            chip?.let {
                categories.find { cat ->
                    cat.name == it.text
                }?.let {
                    viewModel.selectedCategory.postValue(it)
                }
            }
        }

        requireActivity().addMenuProvider(MenuProvider(), viewLifecycleOwner)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    inner class MenuProvider : androidx.core.view.MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_debug, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_info -> {
                    Snackbar.make(
                        requireView(),
                        (binding.recyclerView.adapter as ImageUrlAdapter).size.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                    true
                }

                else -> false
            }
        }

    }
}

