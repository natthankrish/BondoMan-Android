package com.example.bondoman.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: UUID,
    val title: String,
    val category: String,
    val amount: Float,
    val location: String,
    val userEmail: String
)
