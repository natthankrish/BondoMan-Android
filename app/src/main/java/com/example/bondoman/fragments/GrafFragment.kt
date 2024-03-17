package com.example.bondoman.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bondoman.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class GrafFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pieChart: PieChart = view.findViewById(R.id.pieChart)
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(18.5f, "Pemasukan"))
        entries.add(PieEntry(26.7f, "Pengeluaran"))

        val dataSet = PieDataSet(entries, "")
        val colors = arrayListOf<Int>(
            Color.BLUE,
            Color.RED
        )

        dataSet.colors = colors

        val pieData = PieData(dataSet)
        pieChart.data = pieData

        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setUsePercentValues(true)
        pieChart.transparentCircleRadius = 0f
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.invalidate()
    }
}