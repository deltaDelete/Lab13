package ru.deltadelete.lab13.ui.waifuim_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.await
import ru.deltadelete.lab13.api.retrofit.RetrofitClient
import ru.deltadelete.lab13.api.retrofit.models.Image

class WaifuImViewModel : ViewModel() {
    private var images: MutableList<Image> = emptyList<Image>().toMutableList()
    val items = MutableStateFlow<ItemsCallback<Image>>(ItemsCallback.Empty())
    val includedTags = MutableLiveData<List<String>>(emptyList())
    val tags = MutableLiveData<List<String>>(emptyList())
    val loading = MutableLiveData(true)

    private val api = RetrofitClient.waifuRepo

    init {
        viewModelScope.launch {
            loading.postValue(true)
            loadImages()
            loadTags()
            loading.postValue(false)

            items.collect() {
                if (it is ItemsCallback.NewItems) {
                    if (it.clear) {
                        images.clear()
                    }
                    images.addAll(it.items)
                }
            }
        }

        viewModelScope.launch {
            includedTags.observeForever {
                viewModelScope.launch {
                    loading.postValue(true)
                    loadImages(it)
                    loading.postValue(false)
                }
            }
        }
    }

    private suspend fun loadImages() {
        val list = api.search(many = true).await()
        items.emit(ItemsCallback.NewItems(list.images, true))
    }

    private suspend fun loadImages(tags: List<String>) {
        val list = api.search(many = true, includedTags = tags).await()
        items.emit(ItemsCallback.NewItems(list.images, true))
    }

    private suspend fun loadTags() {
        val items = api.tags().await().versatile
        tags.postValue(items)
    }

    fun loadMore() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = api.search(
                many = true,
                excludedFiles = images.map { it.imageId },
                includedTags = tags.value!!
            ).await()
            items.emit(ItemsCallback.NewItems(list.images))
        }
    }
}

sealed class ItemsCallback<T> {
    class Empty<T> : ItemsCallback<T>()

    data class NewItems<T>(val items: List<T>, val clear: Boolean = false) : ItemsCallback<T>()
}