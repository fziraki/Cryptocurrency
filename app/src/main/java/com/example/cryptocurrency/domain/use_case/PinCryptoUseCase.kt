package com.example.cryptocurrency.domain.use_case

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.PinnedCryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PinCryptoUseCase @Inject constructor(
    private val pinnedCryptoRepository: PinnedCryptoRepository
) {
    operator fun invoke(crypto: Crypto): Flow<Resource<Long>> {
        return pinnedCryptoRepository.pinCrypto(crypto)
    }
}