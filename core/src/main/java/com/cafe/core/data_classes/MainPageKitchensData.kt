package com.cafe.core.data_classes

import com.google.gson.annotations.SerializedName


data class MainPageKitchensData(

    @SerializedName("сategories") var categories: ArrayList<Categories> = arrayListOf()

)
