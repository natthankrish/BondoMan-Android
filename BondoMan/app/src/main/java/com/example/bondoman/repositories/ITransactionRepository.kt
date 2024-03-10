package com.example.bondoman.repositories

import com.example.bondoman.entities.Transaction


interface ITransactionRepository {
    fun getAll(userEmail: String): Array<Transaction>
    fun insert(transaction: Transaction)
    fun update(transaction: Transaction)
    fun delete(transaction: Transaction)
}