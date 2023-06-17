package com.zelspeno.hstesttask.di


import android.content.Context
import androidx.room.Room
import com.zelspeno.hstesttask.room.AppDatabase
import com.zelspeno.hstesttask.source.Const
import com.zelspeno.hstesttask.source.DeliveryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule{

    @Provides
    @Singleton
    fun providesContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cache_database"
        ).build()

    @Provides
    @Singleton
    fun providesMenuCacheDao(database: AppDatabase) = database.getMenuCacheDao()
}

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