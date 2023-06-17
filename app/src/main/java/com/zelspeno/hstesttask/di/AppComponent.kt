package com.zelspeno.hstesttask.di

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.zelspeno.hstesttask.source.Const
import com.zelspeno.hstesttask.source.DeliveryApi
import com.zelspeno.hstesttask.ui.menu.Coordinates
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesBaseRetrofit(client: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
        return retrofit.build()
    }

    @Provides
    @Singleton
    fun providesHttpClient() = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun providesDeliveryApi(retrofit: Retrofit) : DeliveryApi {
        return retrofit.create(DeliveryApi::class.java)
    }

}