package com.example.bondoman.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.R
import com.example.bondoman.activities.EditTransaction
import com.example.bondoman.entities.Transaction


class RecyclerViewAdapter(private val dataSet: LiveData<List<Transaction>>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            val item : CardView = view.findViewById(R.id.transaction_card)
            item.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = dataSet.value?.get(position)
                    val intent = Intent(itemView.context, EditTransaction::class.java)
                    intent.putExtra("data", clickedItem?.date)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_transaction, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        viewHolder.item.text = dataSet[position]
    }

    override fun getItemCount() = dataSet.value?.size ?: 0

}

