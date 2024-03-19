package com.example.bondoman.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.R
import com.example.bondoman.entities.Transaction
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransactionListAdapter : ListAdapter<Transaction, TransactionListAdapter.TransactionViewHolder>(TransactionComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val date: TextView = itemView.findViewById(R.id.date)
        private val title: TextView = itemView.findViewById(R.id.name)
        private val location: TextView = itemView.findViewById(R.id.location)
        private val type: TextView = itemView.findViewById(R.id.type)
        private val price: TextView = itemView.findViewById(R.id.price)

        fun bind(current: Transaction?) {
            val dateString = current?.date.toString()
            val inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
            val localDate = LocalDate.parse(dateString, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            date.text = localDate.format(outputFormatter)
            title.text = current?.title.toString()
            location.text = current?.location.toString()
            type.text = current?.category.toString().uppercase()
            price.text = "IDR " + current?.amount.toString()
        }

        companion object {
            fun create(parent: ViewGroup): TransactionViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_transaction, parent, false)
                return TransactionViewHolder(view)
            }
        }
    }

    class TransactionComparator : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return true
            //return oldItem.word == newItem.word
        }
    }
}