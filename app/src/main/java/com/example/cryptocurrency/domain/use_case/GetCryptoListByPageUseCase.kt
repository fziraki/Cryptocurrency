package com.example.cryptocurrency.domain.use_case

import com.example.cryptocurrency.common.Constants.PAGE_SIZE
import com.example.cryptocurrency.domain.model.Crypto
import com.example.cryptocurrency.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCryptoListByPageUseCase @Inject constructor(
    private val cryptoRepository: CryptoRepository
) {
    operator fun invoke(page: Int): Flow<Result<List<Crypto>>> {
        return cryptoRepository.getCryptoListByPage(page, PAGE_SIZE)
    }
}