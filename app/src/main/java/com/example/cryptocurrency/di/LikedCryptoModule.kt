package com.example.cryptocurrency.di

import com.example.cryptocurrency.data.local.CryptoDatabase
import com.example.cryptocurrency.data.repository.LikedCryptoRepositoryImpl
import com.example.cryptocurrency.domain.repository.LikedCryptoRepository
import com.example.cryptocurrency.domain.use_case.GetLikedCryptoListUseCase
import com.example.cryptocurrency.domain.use_case.LikeCryptoUseCase
import com.example.cryptocurrency.domain.use_case.UnLikeCryptoUseCase
import com.example.cryptocurrency.domain.use_case.UpdateLikeCryptoPinFieldUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LikedCryptoModule {

    @Provides
    @ViewModelScoped
    fun provideLikedCryptoRepository(db: CryptoDatabase): LikedCryptoRepository {
        return LikedCryptoRepositoryImpl(db.likedCryptoDao)
    }

    @Provides
    @ViewModelScoped
    fun provideGetLikedCryptoListUseCase(likedCryptoRepository: LikedCryptoRepository): GetLikedCryptoListUseCase {
        return GetLikedCryptoListUseCase(likedCryptoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideLikeCryptoUseCase(likedCryptoRepository: LikedCryptoRepository): LikeCryptoUseCase {
        return LikeCryptoUseCase(likedCryptoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideUnLikeCryptoUseCase(likedCryptoRepository: LikedCryptoRepository): UnLikeCryptoUseCase {
        return UnLikeCryptoUseCase(likedCryptoRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateLikeCryptoPinFieldUseCase(likedCryptoRepository: LikedCryptoRepository): UpdateLikeCryptoPinFieldUseCase {
        return UpdateLikeCryptoPinFieldUseCase(likedCryptoRepository)
    }
}