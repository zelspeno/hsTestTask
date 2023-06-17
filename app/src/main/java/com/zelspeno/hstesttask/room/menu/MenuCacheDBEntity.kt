package com.zelspeno.hstesttask.room.menu

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_cache")
data class MenuCacheDBEntity(
    @PrimaryKey val id: Long?,
    val name: String?,
    val price: Double?,
    val weight: Double?,
    val ingredients: String?,
    val imageUrl: String?,
    val tags: String?
)