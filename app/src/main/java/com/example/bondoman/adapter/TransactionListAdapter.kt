package com.example.bondoman.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.R
import com.example.bondoman.activities.EditTransaction
import com.example.bondoman.entities.Transaction
import com.example.bondoman.viewModels.TransactionsViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransactionListAdapter(private val transactions : TransactionsViewModel, private val startEditIntent: (Intent) -> Unit) : ListAdapter<Transaction, TransactionListAdapter.TransactionViewHolder>(TransactionComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder.create(parent, transactions, startEditIntent)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, startEditIntent)
    }

    class TransactionViewHolder(itemView: View, transactions: TransactionsViewModel, startEditIntent: (Intent) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val date: TextView = itemView.findViewById(R.id.date)
        private val title: TextView = itemView.findViewById(R.id.name)
        private val location: TextView = itemView.findViewById(R.id.location)
        private val type: TextView = itemView.findViewById(R.id.type)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val card: CardView = itemView.findViewById(R.id.transaction_card)

        fun bind(current: Transaction?, startEditIntent: (Intent) -> Unit) {
            val dateString = current?.date.toString()
            val inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
            val localDate = LocalDate.parse(dateString, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            date.text = localDate.format(outputFormatter)
            title.text = current?.title.toString()
            location.text = current?.location.toString()
            type.text = current?.category.toString().uppercase()
            price.text = "IDR " + current?.amount.toString()

            card.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(itemView.context, EditTransaction::class.java)
                    intent.putExtra("id", current?.id.toString())
                    intent.putExtra("date", localDate.format(outputFormatter))
                    intent.putExtra("type", current?.category)
                    intent.putExtra("title", current?.title)
                    intent.putExtra("amount", current?.amount)
                    intent.putExtra("location", current?.location)
                    startEditIntent(intent)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup, transactions : TransactionsViewModel, startEditIntent: (Intent) -> Unit): TransactionViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_transaction, parent, false)

                return TransactionViewHolder(view, transactions, startEditIntent)
            }
        }
    }

    class TransactionComparator : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }
    }
}