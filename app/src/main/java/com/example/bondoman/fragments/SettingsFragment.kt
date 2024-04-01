package com.example.bondoman.fragments


import android.content.Intent

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.activities.LoginActivity
import com.example.bondoman.database.TransactionDatabase
import com.example.bondoman.databinding.FragmentSettingsBinding
import com.example.bondoman.entities.Transaction
import com.example.bondoman.lib.ITransactionFileAdapter
import com.example.bondoman.lib.SecurePreferences
import com.example.bondoman.lib.TransactionDownloader
import com.example.bondoman.lib.TransactionExcelAdapter
import com.example.bondoman.repositories.AuthRepository
import com.example.bondoman.repositories.TransactionRepository
import com.example.bondoman.viewModels.TransactionViewModelFactory
import com.example.bondoman.viewModels.TransactionsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var securePreferences: SecurePreferences
    private val transactionViewModel: TransactionsViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository(
                TransactionDatabase.getInstance(
                    requireContext(),
                    CoroutineScope(
                        SupervisorJob()
                    )
                ).transactionDao(), securePreferences
            )
        )
    }
    private lateinit var authRepository: AuthRepository
    private lateinit var transactions: List<Transaction>
    private lateinit var transactionFileAdapter: ITransactionFileAdapter
    private lateinit var transactionDownloader: TransactionDownloader
    private val XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    private val XLS_MIME_TYPE = "application/vnd.ms-excel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        securePreferences = SecurePreferences(requireContext())
        transactionViewModel.allTransaction.observe(this) {
            transactions = it
        }
        transactionFileAdapter = TransactionExcelAdapter()
        transactionDownloader = TransactionDownloader()
        authRepository =  AuthRepository(SecurePreferences(requireContext()))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingAnimation.isVisible = false
        binding.saveButton.setOnClickListener {
            showSaveTransactionDialog()
        }
        binding.sendButton.setOnClickListener {
            handleSendButtonClick()
        }
        binding.randomizeButton.setOnClickListener {
            broadcastRandomizeTransaction()
        binding.logoutButton.setOnClickListener{
            lifecycleScope.launch {
                showLoading()
                logout()
                hideLoading()
            }
        }
    }

    private suspend fun logout() {
        val response = authRepository.logout()
        if (response.isSuccess) {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }else{
            withContext(Dispatchers.Main) {
                val layoutInflater = LayoutInflater.from(requireContext())
                val view = layoutInflater.inflate(R.layout.custom_toast, null)
                val textView = view.findViewById<TextView>(R.id.customToastText)
                textView.text = "Logout failed, please try again!"
                with (Toast(requireContext())) {
                    duration = Toast.LENGTH_LONG
                    setView(view)
                    show()
                }
            }
        }
    }
    private fun showLoading() {
        binding.loadingAnimation.isVisible = true
    }

    private fun hideLoading() {
        binding.loadingAnimation.isVisible = false
    }

    private fun createFileName(transactions: List<Transaction>, extension: String): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS", Locale.getDefault())
        val currentTime = dateFormat.format(Date())

        val userEmail = transactions.getOrNull(0)?.userEmail ?: "UnknownUser"
        val userName = userEmail.split("@").firstOrNull() ?: "UnknownUser"
        val fileName = "$currentTime $userName Transaction Summary"

        return "$fileName.$extension"
    }

    private fun showSnackbar(message: String) {
        Snackbar
            .make(binding.snackbarContainer, message, 5000)
            .setAction("OK") {}
            .show()

    }

    private fun composeEmail(addresses: Array<String>, subject: String, text: String, attachment: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
            intent.type = XLSX_MIME_TYPE
            intent.putExtra(Intent.EXTRA_EMAIL, addresses)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_STREAM, attachment)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun saveTransaction(extension: String) {
        this.lifecycleScope.launch {
            binding.saveButton.isClickable = false
            val context = requireContext()
            val fileName = createFileName(transactions, extension)
            val result = async(Dispatchers.IO) {
                return@async transactionDownloader.downloadTransactionAsFile(
                    context,
                    fileName,
                    transactions,
                    if (extension == "xlsx") XLSX_MIME_TYPE else XLS_MIME_TYPE,
                    transactionFileAdapter
                )
            }
            Log.d("SettingsFragment", "Loading started")
            showLoading()
            result.await()
            Log.d("SettingsFragment", "Loading finished")
            hideLoading()
            showSnackbar("Your transactions have been exported inside Download file")
            binding.saveButton.isClickable = true
        }
    }

    private fun showSaveTransactionDialog() {
        var extension: String = "xlsx"
        val choiceItems = arrayOf("xlsx", "xls")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle("Choose saved file format")
            .setPositiveButton("OK") { dialog, _ ->
                saveTransaction(extension)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setSingleChoiceItems(
                choiceItems, 0
            ) { dialog, which ->
                extension = choiceItems[which]
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun handleSendButtonClick() {
        val context = requireContext()
        val fileName = createFileName(transactions, "xlsx")
        val file = File(requireContext().externalCacheDir, fileName)
        val outputStream = FileOutputStream(file)

        outputStream.use {
            transactionFileAdapter.save(transactions, fileName, it)
        }
        composeEmail(
            arrayOf(securePreferences.getEmail()!!),
            "Bondoman Transaction Summary",
            "Here's your latest transaction summary",
            FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
        )
    }

    private fun broadcastRandomizeTransaction() {
        val randomizeIntent = Intent()
        randomizeIntent.action = "com.example.bondoman.RANDOMIZE_TRANSACTION"

        val title = titleChoices.random()
        val category = categoryChoices.random()
        val amount = Random.nextInt(1, 1001)

        randomizeIntent.putExtra("TITLE", title)
        randomizeIntent.putExtra("TYPE", category)
        randomizeIntent.putExtra("AMOUNT", amount)

        requireActivity().sendBroadcast(randomizeIntent)
    }

    companion object {
        private val titleChoices = arrayOf(
            "Grocery Mart Purchase",
            "Spring Wardrobe Update",
            "Restaurant Dinner",
            "Gas Station Refill",
            "New Gadgets",
            "Electricity Bill Payment",
            "Local Farmer's Market Haul",
            "Fitness Studio Membership Renewal",
            "Home Essentials Restock",
            "Coffee Shop Treat",
        )

        private val categoryChoices = arrayOf(
            0,
            1
        )
    }
}