package com.cafe.cafespb.domain.domain_main_page

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//загрузка данных при помощи retrofit
interface KitchensApi {
    @GET("v3/058729bd-1402-4578-88de-265481fd7d54")
    suspend fun getListKitchens(): com.cafe.core.data_classes.MainPageKitchensData

    companion object {
        val INSTANCE: KitchensApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KitchensApi::class.java)
    }
}