package com.cafe.cafespb.categories.data_categories

import com.cafe.cafespb.categories.domain_categories.CategoriesApi
import com.cafe.cafespb.categories.domain_categories.CategoriesRepository
import com.cafe.core.data_classes.CategoryDishes

class CategoriesRepositoryImpl : CategoriesRepository {

    //загрузка категорий
    override suspend fun getCategoriesDishes(): CategoryDishes {
        return CategoriesApi.INSTANCE2.getCategories()
    }

    //загрузка тегов для категорий
    override suspend fun getTags(dishes: CategoryDishes): List<String> {
        return dishes.dishes.flatMap { it.tegs }
    }
}