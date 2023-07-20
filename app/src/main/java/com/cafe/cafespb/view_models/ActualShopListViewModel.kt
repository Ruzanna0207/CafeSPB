package com.cafe.cafespb.view_models

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafe.cafespb.shop_bucket.data_shop_bucket.ShopBucketRepositoryImpl
import com.cafe.core.data_classes.Dishes
import kotlinx.coroutines.launch

class ActualShopListViewModel : ViewModel() {

    private val repository = ShopBucketRepositoryImpl()

    val sharedList: MutableLiveData<List<Dishes>> = MutableLiveData()

    private var _date = MutableLiveData<String>()
    val date: LiveData<String> = _date


    fun loadDate() {
        val date = repository.getDate()
        _date.value = date
    }

    //загрузка блюда в корзину
    fun addToShopList(dish: Dishes) {
        val currentBucket = sharedList.value?.toMutableList() ?: mutableListOf()
        val newList = repository.addDishToShopBucket(currentBucket, dish).toSet()
        sharedList.value = newList.toList()
        Log.i("vmShop", sharedList.value?.map { it.name }.toString())
    }

    //удвление из списка покупок
    fun removeShop(list: List<Dishes>, dish: Dishes) {
        val list2 = list.toMutableList()
        list2.remove(dish)
        sharedList.value = list2.toList()
    }
}