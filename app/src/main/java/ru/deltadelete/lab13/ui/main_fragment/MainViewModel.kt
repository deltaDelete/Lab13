package ru.deltadelete.lab13.ui.main_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.deltadelete.lab13.api.WaifuPicsApi
import ru.deltadelete.lab13.api.WaifuPicsApiImpl

class MainViewModel : ViewModel() {
    val items = MutableLiveData<List<String>>(emptyList())
    private val selectedCategory: MutableLiveData<WaifuPicsApi.Categories> =
        MutableLiveData(WaifuPicsApi.Categories.waifu)

    fun getSelectedCategory(): LiveData<WaifuPicsApi.Categories> {
        return selectedCategory as LiveData<WaifuPicsApi.Categories>
    }

    private val api: WaifuPicsApi = WaifuPicsApiImpl()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadImages()
        }
        selectedCategory.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                loadImages(it)
            }
        }
    }

    private fun loadImages() {
        val list = api.getManyRandom()
        items.postValue(list)
    }

    private fun loadImages(category: WaifuPicsApi.Categories = WaifuPicsApi.Categories.waifu) {
        val list = api.getManyRandom(category = category)
        items.postValue(list)
    }
}