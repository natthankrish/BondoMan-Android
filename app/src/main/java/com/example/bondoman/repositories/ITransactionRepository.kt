package com.example.bondoman.repositories

import com.example.bondoman.entities.Transaction


interface ITransactionRepository {
    suspend fun getAll(userEmail: String): Array<Transaction>
    suspend fun insert(transaction: Transaction)
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
    suspend fun deleteAll()
}