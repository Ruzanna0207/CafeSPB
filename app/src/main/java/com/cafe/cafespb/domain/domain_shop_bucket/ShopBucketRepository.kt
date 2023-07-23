package com.cafe.cafespb.domain.domain_shop_bucket

import android.content.Context
import com.cafe.core.data_classes.Dishes

//описание useCases
interface ShopBucketRepository {

    fun getDate(): String

    fun addDishToShopBucket(shoppingList: MutableList<Dishes>, dishes: Dishes): MutableList<Dishes>
}