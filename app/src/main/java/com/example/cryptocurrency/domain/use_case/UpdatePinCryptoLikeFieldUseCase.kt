package com.example.cryptocurrency.domain.use_case

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.PinnedCryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdatePinCryptoLikeFieldUseCase @Inject constructor(
    private val pinnedCryptoRepository: PinnedCryptoRepository
) {
    operator fun invoke(crypto: Crypto, isLiked: Boolean): Flow<Resource<Int>> {
        return pinnedCryptoRepository.updatePinnedCryptoLikeField(crypto.id, isLiked)
    }
}