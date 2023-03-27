package com.example.cryptocurrency.di

import android.app.Application
import androidx.room.Room
import com.example.cryptocurrency.common.Constants.BASE_URL
import com.example.cryptocurrency.data.local.CryptoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCryptoDatabase(app: Application): CryptoDatabase {
        return Room.databaseBuilder(
            app, CryptoDatabase::class.java, "crypto_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(
        httpLogging: Interceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(httpLogging)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

}