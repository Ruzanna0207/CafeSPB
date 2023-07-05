package com.cafe.cafespb.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cafe.cafespb.categories.data_categories.CategoriesRepositoryImpl
import com.cafe.core.data_classes.Dishes
import kotlinx.coroutines.launch


class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CategoriesRepositoryImpl()

    private val _currentCategory = MutableLiveData<List<Dishes>>()
    val currentCategory: LiveData<List<Dishes>> = _currentCategory

    private val _currentTags = MutableLiveData<List<String>>()
    val currentTags: LiveData<List<String>> = _currentTags

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    //фун-я для загрузки инфор-и для фрагмента категории блюд
    fun loadCategory() {
        viewModelScope.launch {
            try {
                val category = repository.getCategoriesDishes()
                _currentCategory.value = category.dishes
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки категорий"
            }
        }
    }

    //фун-я для загрузки инфор-и по тегам
    fun loadTags() {
        viewModelScope.launch {
            try {
                val tags = repository.getTags(repository.getCategoriesDishes())
                _currentTags.value = tags
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки категорий"
            }
        }
    }
}