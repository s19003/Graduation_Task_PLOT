package com.example.graduationtaskplot.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.graduationtaskplot.CountActivity
import com.example.graduationtaskplot.R
import com.example.graduationtaskplot.realm.RealmData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        realm = Realm.getDefaultInstance()
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // カウントボタン表示
        var button: FloatingActionButton? = activity?.findViewById(R.id.count_button)
        button?.visibility = View.VISIBLE

        // グラフの処理
        val xLine = mutableListOf<Date>()

        val realmResults = realm.where(RealmData::class.java).findAll().sort("date")
        for (i in realmResults.indices) {
            xLine.add(realmResults[i]!!.date)
        }

        println(xLine)

        val yLine = mutableListOf<Float>()

        for (i in realmResults.indices) {
            yLine.add(realmResults[i]!!.count.toFloat())
        }

//        // Entry
//        var entryList = mutableListOf<BarEntry>()
//        for (i in xLine.indices) {
//            entryList.add(
//                BarEntry(xLine[i], yLine[i])
//            )
//        }
//
//        println(entryList)

//        // BarDataSetのリスト
//        val barDataSets = mutableListOf<IBarDataSet>()
//        // BarDataにデータ格納
//        val barDataSet = BarDataSet(entryList, "test")
//        barDataSet.color = Color.BLUE
//        // リストに格納
//        barDataSets.add(barDataSet)
//
//        // BarDataにBarDataSet格納
//        val barData = BarData(barDataSets)
//        // BarChartにBarData格納
//        val barChart = activity?.findViewById<BarChart>(R.id.barChart)
//        barChart?.data = barData
//        barChart?.xAxis?.apply {
//            isEnabled = true
//            textColor = Color.BLACK
//        }
//
//        barChart?.notifyDataSetChanged()
//        barChart?.invalidate()
    }
}