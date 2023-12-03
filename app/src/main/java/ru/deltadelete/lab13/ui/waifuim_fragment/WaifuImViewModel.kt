package ru.deltadelete.lab13.ui.waifuim_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await
import ru.deltadelete.lab13.api.RetrofitClient
import ru.deltadelete.lab13.api.WaifuPicsApi
import ru.deltadelete.lab13.api.models.Image

class WaifuImViewModel : ViewModel() {
    val items = MutableLiveData<List<Image>>(emptyList())

    private val api = RetrofitClient.waifuRepo

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadImages()
        }
    }

    private suspend fun loadImages() {
        val list = api.search(many = true).await()
        items.postValue(list.images)
    }
}