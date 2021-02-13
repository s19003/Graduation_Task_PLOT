package com.example.graduationtaskplot.fragment

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
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
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

        // チャート設定
        chart?.apply {
            // Y軸
            axisLeft.axisMaximum = 100f
            axisLeft.axisMinimum = 0f
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
        barDataSet.color = Color.BLUE

        val set = mutableListOf<IBarDataSet>()
        set.add(barDataSet)

        chart?.data = BarData(set)
        chart?.invalidate()

        // 今日の回数
        var current = SimpleDateFormat("yy-MM-dd")
        var date = current.format(Date())
        var day = realm.where(RealmData::class.java).sort("date", Sort.DESCENDING).findFirst()

        if (date.equals(day?.day)) {
            activity?.findViewById<TextView>(R.id.text_today_count)?.text = day?.count.toString()
        }
    }

    override fun onPause() {
        super.onPause()
        realm = Realm.getDefaultInstance()
    }
}