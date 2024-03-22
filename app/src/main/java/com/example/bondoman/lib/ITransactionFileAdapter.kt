package com.example.bondoman.lib

import com.example.bondoman.entities.Transaction
import java.io.OutputStream

interface ITransactionFileAdapter {
    fun save(transactions: List<Transaction>, fileName: String, outputStream: OutputStream)
}