package com.zelspeno.hstesttask.source

import androidx.appcompat.content.res.AppCompatResources
import com.zelspeno.hstesttask.R
import com.zelspeno.hstesttask.ui.menu.BonusCard
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DeliveryApi {

    @GET("{hashPath}")
    fun getDishesList(
        @Path("hashPath") hashPath: String
    ): Call<DishesJSONObject>

}