package com.cafe.cafespb.view_models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cafe.cafespb.data.data_main_page.KitchensRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repositoryMain = KitchensRepositoryImpl()

    private var _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private var _city = MutableLiveData<String>()
    val city: LiveData<String> = _city

    private var _currentKitchens = MutableLiveData<List<com.cafe.core.data_classes.Categories>>()
    val currentKitchens: LiveData<List<com.cafe.core.data_classes.Categories>> = _currentKitchens

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    //фун-я для загрузки инфор-и из репозитория для реализации в главном фрагменте
    fun loadKitchens() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val newList = repositoryMain.getKitchensDetails().categories

                withContext(Dispatchers.Main) {
                    _currentKitchens.value = newList
                    Log.i("list", currentKitchens.value.toString())
                }
            } catch (e: Exception) {
                _error.postValue("Ошибка загрузки")
            }
        }
    }

    fun loadDate() {
        val date = repositoryMain.getDate()
        _date.value = date
    }

    fun loadCity(context: Context) {
        viewModelScope.launch {
            val location = repositoryMain.getLocationAsync(context)
            _city.value = location
            Log.i("loc", location)
        }
    }
}
