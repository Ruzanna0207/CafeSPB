package com.cafe.cafespb.main_page.domain_main_page

import android.content.Context

//описание useCases
interface KitchensRepository {

    suspend fun getKitchensDetails(): com.cafe.core.data_classes.MainPageKitchensData

    suspend fun getLocationAsync(context: Context): String

    fun getDate(): String

}