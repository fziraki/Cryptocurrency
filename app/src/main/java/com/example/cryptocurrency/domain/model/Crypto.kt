package com.example.cryptocurrency.domain.model

import com.example.cryptocurrency.data.local.entity.LikedCryptoEntity
import com.example.cryptocurrency.data.local.entity.PinnedCryptoEntity

data class Crypto(
    val id: Int,
    val name: String,
    val symbol: String,
    val priceInUsdt: String,
    val changePercent: String
){
    val priceInUsdtToShow = String.format("%.1f", priceInUsdt.toDouble())
    val changePercentToShow = String.format("%.1f", changePercent.toDouble())
    var isPinned: Boolean = false
    var isLiked: Boolean = false

    fun toPinnedCryptoEntity(): PinnedCryptoEntity {
        return PinnedCryptoEntity(
            id = id,
            name = name,
            symbol = symbol,
            priceInUsdt = priceInUsdt,
            changePercent = changePercent
        )

    }

    fun toLikedCryptoEntity(): LikedCryptoEntity {
        return LikedCryptoEntity(
            id = id,
            name = name,
            symbol = symbol,
            priceInUsdt = priceInUsdt,
            changePercent = changePercent
        )

    }



}