package com.example.bondoman.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.R
import com.example.bondoman.activities.EditTransaction


class RecyclerViewAdapter(private val dataSet: Array<String>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            val item : CardView = view.findViewById(R.id.transaction_card)
            item.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = dataSet[position]
                    val intent = Intent(itemView.context, EditTransaction::class.java)
                    intent.putExtra("data", clickedItem)
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

    override fun getItemCount() = dataSet.size

}

