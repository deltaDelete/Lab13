package ru.deltadelete.lab13.ui.main_fragment

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

    private val api: WaifuPicsApi = WaifuPicsApiImpl()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val list = api.getManyRandom()
            items.postValue(list)
        }
    }
}