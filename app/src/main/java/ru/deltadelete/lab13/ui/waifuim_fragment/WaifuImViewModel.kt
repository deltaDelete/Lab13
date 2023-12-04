package ru.deltadelete.lab13.ui.waifuim_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.request.Tags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await
import ru.deltadelete.lab13.api.RetrofitClient
import ru.deltadelete.lab13.api.WaifuPicsApi
import ru.deltadelete.lab13.api.models.Image

class WaifuImViewModel : ViewModel() {
    val items = MutableLiveData<List<Image>>(emptyList())
    val moreItems = MutableLiveData<List<Image>>(emptyList())
    val includedTags = MutableLiveData<List<String>>(emptyList())
    val tags = MutableLiveData<List<String>>(emptyList())
    val loading = MutableLiveData(true)

    private val api = RetrofitClient.waifuRepo

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            loadImages()
            loadTags()
            loading.postValue(false)
        }

        includedTags.observeForever {
            viewModelScope.launch {
                loading.postValue(true)
                loadImages(it)
                loading.postValue(false)
            }
        }
    }

    private suspend fun loadImages() {
        val list = api.search(many = true).await()
        items.postValue(list.images)
    }

    private suspend fun loadImages(tags: List<String>) {
        val list = api.search(many = true, includedTags = tags).await()
        items.postValue(list.images)
    }

    private suspend fun loadTags() {
        val items = api.tags().await().versatile
        tags.postValue(items)
    }

    fun loadMore() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = api.search(many = true, excludedFiles = items.value!!.map { it.imageId }, includedTags = tags.value!!).await().images
            moreItems.postValue(list)
        }
    }
}