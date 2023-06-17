package com.zelspeno.hstesttask.source

import com.zelspeno.hstesttask.R
import com.zelspeno.hstesttask.ui.menu.BonusCard
import retrofit2.Call
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api: DeliveryApi) {

    fun getDishesJson(): Call<DishesJSONObject> {
        return api.getDishesList(Const.DISHES_URL)
    }

    fun getBonusesList(): List<BonusCard> {
        val res = mutableListOf<BonusCard>()
        res.add(BonusCard(R.drawable.bonus1))
        res.add(BonusCard(R.drawable.bonus2))
        res.add(BonusCard(R.drawable.bonus3))
        res.add(BonusCard(R.drawable.bonus4))
        res.add(BonusCard(R.drawable.bonus5))
        return res
    }

}