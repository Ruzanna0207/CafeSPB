package com.cafe.cafespb.categories.domain_categories

import com.cafe.core.data_classes.CategoryDishes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//загрузка данных при помощи retrofit
interface CategoriesApi {
    @GET("v3/aba7ecaa-0a70-453b-b62d-0e326c859b3b")
    suspend fun getCategories(): CategoryDishes

    companion object {
        val INSTANCE2: CategoriesApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoriesApi::class.java)
    }
}