package com.example.bondoman.lib

import com.example.bondoman.entities.Transaction

interface ITransactionGraphGenerator {
    fun generateGraph(transactions: Array<Transaction>)
}