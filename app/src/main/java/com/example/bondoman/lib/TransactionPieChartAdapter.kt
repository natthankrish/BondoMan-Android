package com.example.bondoman.lib

import android.graphics.Color
import com.example.bondoman.entities.Transaction
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class TransactionPieChartAdapter: ITransactionGraphAdapter<PieChart> {
    override fun generateGraph(transactions: List<Transaction>, graph: PieChart) {
        val transactionSums = HashMap<String, Float>()
        for (transaction in transactions) {
            val category = transaction.category
            val currentSum = transactionSums[category] ?: 0f
            transactionSums[category] = currentSum + transaction.amount
        }

        val entries = transactionSums.map { (category, sum) ->
            PieEntry(sum, category)
        }
        val dataSet = PieDataSet(entries, "")
        val colors = arrayListOf(
            Color.BLUE,
            Color.RED
        )
        dataSet.colors = colors

        val pieData = PieData(dataSet)
        pieData.setValueTextSize(16f)
        pieData.setValueTextColor(Color.WHITE)
        graph.data = pieData

        graph.description.isEnabled = false
        graph.setDrawEntryLabels(false)
        graph.setUsePercentValues(true)
        graph.transparentCircleRadius = 0f
        graph.animateY(1400, Easing.EaseInOutQuad)

        val legend = graph.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.textSize = 14f
        legend.xEntrySpace = 7f
        legend.yEntrySpace = 0f
        legend.yOffset = 0f

        graph.invalidate()
    }
}