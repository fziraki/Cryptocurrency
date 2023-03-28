package com.example.cryptocurrency.domain.use_case

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnLikeCryptoUseCase @Inject constructor(
    private val cryptoRepository: CryptoRepository
) {
    operator fun invoke(cryptoId: Int): Flow<Resource<Int>> {
        return cryptoRepository.unLikeCrypto(cryptoId)
    }
}