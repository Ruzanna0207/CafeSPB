package com.cafe.cafespb.shop_bucket.domain_shop_bucket

import android.content.Context
import com.cafe.core.data_classes.Dishes

//описание useCases
interface ShopBucketRepository {

    suspend fun getLocationAsync(context: Context): String

    fun getDate(): String

    fun addDishToShopBucket(shoppingList: MutableList<Dishes>, dishes: Dishes): MutableList<Dishes>
}