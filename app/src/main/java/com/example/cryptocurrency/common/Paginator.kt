package com.example.cryptocurrency.common

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}