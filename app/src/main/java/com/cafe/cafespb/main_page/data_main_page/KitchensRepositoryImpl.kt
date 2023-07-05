package com.cafe.cafespb.main_page.data_main_page

import android.content.Context
import com.cafe.cafespb.main_page.domain_main_page.KitchensApi
import com.cafe.cafespb.main_page.domain_main_page.KitchensRepository
import com.cafe.cafespb.location_helper_classes.LocationHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KitchensRepositoryImpl : KitchensRepository {

    //загрузка кухонь главной страницы
    override suspend fun getKitchensDetails(): com.cafe.core.data_classes.MainPageKitchensData {
        return KitchensApi.INSTANCE.getListKitchens()
    }

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
}