package com.example.cryptocurrency.di

import com.example.cryptocurrency.data.remote.CryptoApi
import com.example.cryptocurrency.data.repository.CryptoRepositoryImpl
import com.example.cryptocurrency.domain.repository.CryptoRepository
import com.example.cryptocurrency.domain.use_case.GetCryptoListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CryptoModule {

    @Provides
    @Singleton
    fun provideCryptoApi(retrofit: Retrofit): CryptoApi {
        return retrofit.create(CryptoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCryptoRepository(cryptoApi: CryptoApi): CryptoRepository {
        return CryptoRepositoryImpl(cryptoApi)
    }

    @Provides
    @Singleton
    fun provideGetCryptoListUseCase(cryptoRepository: CryptoRepository): GetCryptoListUseCase {
        return GetCryptoListUseCase(cryptoRepository)
    }
}