package com.cafe.core.data_classes

import com.google.gson.annotations.SerializedName

data class CategoryDishes(

    @SerializedName("dishes") var dishes : ArrayList<Dishes> = arrayListOf()

)