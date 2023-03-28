package com.example.cryptocurrency.di

import com.example.cryptocurrency.data.local.CryptoDatabase
import com.example.cryptocurrency.data.repository.PinnedCryptoRepositoryImpl
import com.example.cryptocurrency.domain.repository.PinnedCryptoRepository
import com.example.cryptocurrency.domain.use_case.GetPinnedCryptoListUseCase
import com.example.cryptocurrency.domain.use_case.PinCryptoUseCase
import com.example.cryptocurrency.domain.use_case.UnPinCryptoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PinnedCryptoModule {

    @Provides
    @ViewModelScoped
    fun providePinnedCryptoRepository(db: CryptoDatabase): PinnedCryptoRepository {
        return PinnedCryptoRepositoryImpl(db.pinnedCryptoDao)
    }

    @Provides
    @ViewModelScoped
    fun provideGetPinnedCryptoListUseCase(pinnedCryptoRepository: PinnedCryptoRepository): GetPinnedCryptoListUseCase {
        return GetPinnedCryptoListUseCase(pinnedCryptoRepository)
    }

    @Provides
    @ViewModelScoped
    fun providePinCryptoUseCase(pinnedCryptoRepository: PinnedCryptoRepository): PinCryptoUseCase {
        return PinCryptoUseCase(pinnedCryptoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideUnPinCryptoUseCase(pinnedCryptoRepository: PinnedCryptoRepository): UnPinCryptoUseCase {
        return UnPinCryptoUseCase(pinnedCryptoRepository)
    }
}