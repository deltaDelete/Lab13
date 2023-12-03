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
    val moreItems = MutableLiveData<List<String>>(emptyList())
    val selectedCategory: MutableLiveData<WaifuPicsApi.Categories> =
        MutableLiveData(WaifuPicsApi.Categories.waifu)
    val loading = MutableLiveData(true)

    val flow: MutableStateFlow<String> = MutableStateFlow("")

    private val api: WaifuPicsApi = WaifuPicsApiImpl()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            loadImages()
            loading.postValue(false)
        }
        selectedCategory.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                loading.postValue(true)
                loadImages(it)
                loading.postValue(false)
            }
        }
    }

    private fun loadImages(category: WaifuPicsApi.Categories = WaifuPicsApi.Categories.waifu) {
        val list = api.getManyRandom(category = category)
        items.postValue(list)
    }

    fun loadMore() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = api.getManyRandom(category = selectedCategory.value ?: WaifuPicsApi.Categories.waifu)
            moreItems.postValue(list)
        }
    }
}