package com.zelspeno.hstesttask.room.menu


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MenuCacheRepository @Inject constructor(
    private val menuCacheDao: MenuCacheDao
) {

    suspend fun getDishesList(): Flow<List<DishCache>> {
        return flowOf(menuCacheDao.getDishesList())
    }

    suspend fun updateDishesList(entity: List<MenuCacheDBEntity>) {
        menuCacheDao.updateDishesList(entity)
    }

}