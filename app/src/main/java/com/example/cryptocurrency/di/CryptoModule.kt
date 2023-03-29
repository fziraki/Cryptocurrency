package com.example.cryptocurrency.di

import com.example.cryptocurrency.data.local.CryptoDatabase
import com.example.cryptocurrency.data.remote.CryptoApi
import com.example.cryptocurrency.data.repository.CryptoRepositoryImpl
import com.example.cryptocurrency.domain.repository.CryptoRepository
import com.example.cryptocurrency.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object CryptoModule {

    @Provides
    @ViewModelScoped
    fun provideCryptoApi(retrofit: Retrofit): CryptoApi {
        return retrofit.create(CryptoApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideCryptoRepository(cryptoApi: CryptoApi, db: CryptoDatabase): CryptoRepository {
        return CryptoRepositoryImpl(cryptoApi, db.cryptoDao, db.pinnedCryptoDao, db.likedCryptoDao)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCryptoListUseCase(cryptoRepository: CryptoRepository): GetCryptoListUseCase {
        return GetCryptoListUseCase(cryptoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateCryptoPinUseCase(cryptoRepository: CryptoRepository): UpdateCryptoPinUseCase {
        return UpdateCryptoPinUseCase(cryptoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateCryptoLikeUseCase(cryptoRepository: CryptoRepository): UpdateCryptoLikeUseCase {
        return UpdateCryptoLikeUseCase(cryptoRepository)
    }

}