package com.example.bondoman.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bondoman.database.room.TransactionDatabase
import com.example.bondoman.entities.Transaction
import com.example.bondoman.repositories.room.RoomTransactionRepository
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class TransactionsViewModel(private val repository: RoomTransactionRepository) : ViewModel() {
    val allTransaction: LiveData<List<Transaction>> = repository.allTransaction.asLiveData()

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }
}