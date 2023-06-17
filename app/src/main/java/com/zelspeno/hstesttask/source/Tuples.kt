package com.zelspeno.hstesttask.source

import com.google.gson.annotations.SerializedName

data class DishesJSONObject(
    @SerializedName("dishes") val dishes: List<Dish>
)

data class Dish (
    val id: Long?,
    val name: String?,
    val price: Double?,
    val weight: Double?,
    val ingredients: List<String>?,
    @SerializedName("image_url") val imageUrl: String?,
    val tags: List<String>?
)