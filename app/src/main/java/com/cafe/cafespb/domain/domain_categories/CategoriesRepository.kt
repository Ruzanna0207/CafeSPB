package com.cafe.cafespb.domain.domain_categories

import com.cafe.core.data_classes.CategoryDishes

//описание useCases
interface CategoriesRepository {

    suspend fun getCategoriesDishes(): CategoryDishes

    suspend fun getTags(dishes: CategoryDishes): List<String>

}