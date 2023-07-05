package com.cafe.cafespb.shop_bucket.data_shop_bucket

import android.content.Context
import com.cafe.cafespb.shop_bucket.domain_shop_bucket.ShopBucketRepository
import com.cafe.cafespb.location_helper_classes.LocationHelper
import com.cafe.core.data_classes.Dishes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ShopBucketRepositoryImpl : ShopBucketRepository {

    //фун-я для опред-я местополож-я вызыв-ся на другом потоке дляч ускор-я работы
    override suspend fun getLocationAsync(context: Context): String {
        val locationHelper = LocationHelper(context)
        return suspendCoroutine { continuation ->
            locationHelper.getLocation { location ->
                val cityName = locationHelper.getCityName(location)
                continuation.resume(cityName)
            }
        }
    }

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