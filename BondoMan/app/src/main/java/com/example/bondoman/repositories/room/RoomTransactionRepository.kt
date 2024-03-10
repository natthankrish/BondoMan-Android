package com.example.bondoman.repositories.room

import com.example.bondoman.entities.Transaction
import com.example.bondoman.repositories.ITransactionRepository

class RoomTransactionRepository(private val transactionDao: ITransactionDao):
    ITransactionRepository {
    override fun getAll(userEmail: String): Array<Transaction>
        = transactionDao.getAll(userEmail)

    override fun insert(transaction: Transaction)
        = transactionDao.insert(transaction)

    override fun update(transaction: Transaction)
        = transactionDao.update(transaction)

    override fun delete(transaction: Transaction)
        = transactionDao.delete(transaction)
}