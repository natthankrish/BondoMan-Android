package com.example.bondoman.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "type") val category: String,
    @ColumnInfo(name = "amount") val amount: Float,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "userEmail") val userEmail: String
)
