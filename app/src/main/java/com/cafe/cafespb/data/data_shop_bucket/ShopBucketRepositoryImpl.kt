package com.cafe.cafespb.data.data_shop_bucket

import com.cafe.cafespb.domain.domain_shop_bucket.ShopBucketRepository
import com.cafe.core.data_classes.Dishes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ShopBucketRepositoryImpl : ShopBucketRepository {

    override fun getDate(): String {
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale("ru", "RU"))
        return dateFormat.format(currentDate)
    }

    //фун-я для загрузки блюд в корзину
    override fun addDishToShopBucket(shoppingList: MutableList<Dishes>, dishes: Dishes): MutableList<Dishes> {
        shoppingList.add(dishes)
        return shoppingList
    }
}