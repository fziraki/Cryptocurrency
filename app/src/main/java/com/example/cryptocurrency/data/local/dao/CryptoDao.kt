package com.example.cryptocurrency.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptocurrency.data.local.entity.CryptoEntity

@Dao
interface CryptoDao {

    @Query("SELECT * FROM CryptoEntity")
    suspend fun getCryptos(): List<CryptoEntity>

    @Query("SELECT * FROM CryptoEntity ORDER BY isPinned DESC LIMIT :pageSize OFFSET :startingIndex")
    suspend fun getCryptosByPage(startingIndex: Int, pageSize: Int): List<CryptoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptos(cryptos: List<CryptoEntity>)

}