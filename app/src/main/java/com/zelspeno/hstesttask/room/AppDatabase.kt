package com.zelspeno.hstesttask.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zelspeno.hstesttask.room.menu.MenuCacheDBEntity
import com.zelspeno.hstesttask.room.menu.MenuCacheDao

@Database(
    entities = [MenuCacheDBEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMenuCacheDao() : MenuCacheDao

}