package com.example.bondoman.lib

import com.example.bondoman.entities.Transaction

interface ITransactionFIleAdapter {
    fun save(transactions: Array<Transaction>, fileName: String)
}