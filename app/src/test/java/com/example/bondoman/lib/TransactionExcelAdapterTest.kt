package com.example.bondoman.lib

import com.example.bondoman.entities.Transaction
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.util.Date

class TransactionExcelAdapterTest {
    @Test
    fun saveXlsx() {
        val directory = File("tests")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val fileName = "tests/test01.xlsx"
        val transactions = arrayOf(
            Transaction(1, "Beli seblak", "Pengeluaran", 33000.0f, "Seblak Jeletot Store", Date(), "test@email.com"),
            Transaction(2, "Jual baju", "Pemasukan", 50000.0f, "Tokopaedi", Date(), "test@email.com")
        )

        val adapter = TransactionExcelAdapter()
        adapter.save(transactions, fileName)

        val fis = FileInputStream(fileName)
        val workbook = XSSFWorkbook(fis)
        val sheet = workbook.getSheetAt(0)

        for ((index, transaction) in transactions.withIndex()) {
            val row = sheet.getRow(index + 1)
            val idCell = row.getCell(0)
            assert(idCell.numericCellValue.toInt() == transaction.id)
            val titleCell = row.getCell(1)
            assert(titleCell.stringCellValue == transaction.title)
            val categoryCell = row.getCell(2)
            assert(categoryCell.stringCellValue == transaction.category)
            val amountCell = row.getCell(3)
            assert(amountCell.numericCellValue.toFloat() == transaction.amount)
            val locationCell = row.getCell(4)
            assert(locationCell.stringCellValue == transaction.location)
        }

        fis.close()
    }

    @Test
    fun saveXls() {
        val directory = File("tests")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val fileName = "tests/test01.xls"
        val transactions = arrayOf(
            Transaction(1, "Beli seblak", "Pengeluaran", 33000.0f, "Seblak Jeletot Store", Date(), "test@email.com"),
            Transaction(2, "Jual baju", "Pemasukan", 50000.0f, "Tokopaedi", Date(), "test@email.com")
        )

        val adapter = TransactionExcelAdapter()
        adapter.save(transactions, fileName)

        val fis = FileInputStream(fileName)
        val workbook = HSSFWorkbook(fis)
        val sheet = workbook.getSheetAt(0)

        for ((index, transaction) in transactions.withIndex()) {
            val row = sheet.getRow(index + 1)
            val idCell = row.getCell(0)
            assert(idCell.numericCellValue.toInt() == transaction.id)
            val titleCell = row.getCell(1)
            assert(titleCell.stringCellValue == transaction.title)
            val categoryCell = row.getCell(2)
            assert(categoryCell.stringCellValue == transaction.category)
            val amountCell = row.getCell(3)
            assert(amountCell.numericCellValue.toFloat() == transaction.amount)
            val locationCell = row.getCell(4)
            assert(locationCell.stringCellValue == transaction.location)
        }

        fis.close()
    }


}