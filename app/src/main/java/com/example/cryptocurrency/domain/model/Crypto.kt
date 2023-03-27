package com.example.cryptocurrency.domain.model

import kotlin.math.roundToLong

data class Crypto(
    val id: Int,
    val name: String,
    val nameFa: String,
    val symbol: String,
    val priceInUsdt: String,
    val changePercent: String,
    val usdtVolume: String
){
    val priceInUsdtToShow = String.format("%.1f", priceInUsdt.toDouble())
    val changePercentToShow = String.format("%.1f", changePercent.toDouble())
    val usdtVolumeToShow = (usdtVolume.toDouble()/1000).roundToLong()
}