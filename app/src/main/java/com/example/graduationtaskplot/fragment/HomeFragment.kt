package com.example.graduationtaskplot.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.graduationtaskplot.R
import com.example.graduationtaskplot.realm.RealmData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.Sort
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    // Realmデータベース
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Realmデータベース
        realm = Realm.getDefaultInstance()

        // カウントボタンを表示する
        var button: FloatingActionButton? = activity?.findViewById(R.id.count_button)
        button?.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()

        // チャート
        var chart = activity?.findViewById<BarChart>(R.id.bar_chart)

        // x軸(上)ラベルリスト
        val labels = arrayListOf<String>()
        labels.add("")
        val xLabels = realm.where(RealmData::class.java).sort("date", Sort.ASCENDING)
        for (i in xLabels.findAll().iterator()) {
            var simple = SimpleDateFormat("MM/dd")
            var x = simple.format(i.date)
            labels.add(x.toString())
        }

        // チャート設定
        chart?.apply {
            // 全体
            setDrawValueAboveBar(true) //
            description.isEnabled = false // グラフの説明を非表示にする
            isScaleYEnabled = false // Y軸への拡大を無効にする

            // X軸
            xAxis.valueFormatter = IndexAxisValueFormatter(labels) // X軸のラベルを指定する
            xAxis.position = XAxis.XAxisPosition.BOTTOM // ラベルを下に表示する
            xAxis.setDrawLabels(true) // ラベルをセットする
            xAxis.setDrawGridLines(false) // X軸のグリッド線を非表示にする
            xAxis.setDrawAxisLine(true) //

            // Y軸(左)
            axisLeft.axisMaximum = 100f // 表示上限
            axisLeft.axisMinimum = 0f // 表示下限

            // Y軸(右)
            axisRight.isEnabled = false // 表示有無
        }

        // データを格納する
        var entries = mutableListOf<BarEntry>()
        val realmResult = realm.where(RealmData::class.java).sort("date", Sort.ASCENDING)
        for (i in realmResult.findAll().iterator()) {
            var x = i.id.toFloat()
            var y = i.count.toFloat()
            entries.add(BarEntry(x, y))
        }

        val barDataSet = BarDataSet(entries, "スクワット回数")
        barDataSet.color = R.color.teal_700

        val set = mutableListOf<IBarDataSet>()
        set.add(barDataSet)

        chart?.data = BarData(set)
        chart?.invalidate()

        // 今日の回数
        var current = SimpleDateFormat("yy-MM-dd")
        var date = current.format(Date())
        var day = realm.where(RealmData::class.java).sort("date", Sort.DESCENDING).findFirst()

        if (date.equals(day?.day)) {
            activity?.findViewById<TextView>(R.id.text_today_count)?.apply {
                text = String.format("${day?.count.toString()} 回")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        realm = Realm.getDefaultInstance()
    }
}