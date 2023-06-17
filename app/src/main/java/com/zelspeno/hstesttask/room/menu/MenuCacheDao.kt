package com.zelspeno.hstesttask.room.menu

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MenuCacheDao {

    @Query("SELECT * FROM menu_cache")
    suspend fun getDishesList(): List<DishCache>

    @Upsert
    suspend fun updateDishesList(entity: List<MenuCacheDBEntity>)

}