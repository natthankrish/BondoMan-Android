package com.example.bondoman.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.R
import com.example.bondoman.activities.AddTransaction
import com.example.bondoman.activities.EditTransaction
import com.example.bondoman.adapter.TransactionListAdapter
import com.example.bondoman.database.TransactionDatabase
import com.example.bondoman.entities.Transaction
import com.example.bondoman.repositories.TransactionRepository
import com.example.bondoman.viewModels.TransactionViewModelFactory
import com.example.bondoman.viewModels.TransactionsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.Date

class TransaksiFragment : Fragment() {
    private lateinit var adapter: TransactionListAdapter
    private val newTransactionRequestCode = 1
    private val editTransactionRequestCode = 2
    private val wordViewModel: TransactionsViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository(
                TransactionDatabase.getInstance(requireContext(), CoroutineScope(
            SupervisorJob()
        )
        ).transactionDao())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaksi, container, false)


        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        adapter = TransactionListAdapter(wordViewModel, ::itemEditRequest)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        wordViewModel.allTransaction.observe(viewLifecycleOwner) { transactions ->
            transactions.let { adapter.submitList(it) }
        }

        val addButton: FloatingActionButton = view.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            startActivityForResult(Intent(requireContext(), AddTransaction::class.java), newTransactionRequestCode)
        }

        return view
    }

    fun itemEditRequest(intent: Intent) {
        startActivityForResult(intent, editTransactionRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        Log.i("Masuk", "Masuk")

        if (requestCode == newTransactionRequestCode && resultCode == Activity.RESULT_OK) {
            val title = intentData?.getStringExtra(AddTransaction.TITLE) ?: ""
            val amount = intentData?.getFloatExtra(AddTransaction.AMOUNT, 0.0f) ?: 0.0f
            val type = intentData?.getStringExtra(AddTransaction.TYPE) ?: ""
            val location = intentData?.getStringExtra(AddTransaction.LOCATION) ?: ""

            val transaction = Transaction(
                id = 0,
                title = title,
                category = type,
                amount = amount,
                location = location,
                date = Date(),
                userEmail = "testing"
            )
            wordViewModel.insert(transaction)
        } else if (requestCode == editTransactionRequestCode && resultCode == Activity.RESULT_OK) {
            val command = intentData?.getStringExtra("command") ?: ""
            val itemID = intentData?.getStringExtra("id")?.toInt() ?: 0
            Log.i("COMMAND", command.toString())
            Log.i("ITEM ID", itemID.toString())
            if (command == "delete") {
                wordViewModel.deleteById(itemID)
            } else {

            }


        }
    }
}