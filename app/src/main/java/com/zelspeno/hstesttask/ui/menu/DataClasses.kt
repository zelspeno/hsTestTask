package com.zelspeno.hstesttask.ui.menu

data class BonusCard(
    val image: Int
)

data class Tag(
    val name: String,
    val onClick: Boolean
)

data class DishUI (
    val id: Long?,
    val name: String?,
    val price: String?,
    val ingredients: String?,
    val imageUrl: String?,
    val tags: List<String>?
)
