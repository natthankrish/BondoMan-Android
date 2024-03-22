package com.example.bondoman.lib

import com.example.bondoman.entities.Transaction

interface ITransactionGraphAdapter<T> {
    fun generateGraph(transactions: List<Transaction>, graph: T)
}