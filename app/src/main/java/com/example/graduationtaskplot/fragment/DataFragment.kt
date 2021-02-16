package com.example.graduationtaskplot.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.graduationtaskplot.R
import com.example.graduationtaskplot.realm.RealmData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.Sort

class DataFragment : Fragment() {
    // Realmデータベース
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.data_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // カウントボタンを非表示にする
        var button: FloatingActionButton? = activity?.findViewById(R.id.count_button)
        button?.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()

        // Realmデータベース
        realm = Realm.getDefaultInstance()

        val realms = realm.where(RealmData::class.java)

        // 合計値を求める
        val realmSum = realms.sum("count") ?: 0
        val sum = String.format("$realmSum 回")
        activity?.findViewById<TextView>(R.id.text_sum_count)?.text = sum

        // 測定日数を求める
        val realmTotal = realms.findAll().size
        val total = String.format("$realmTotal 日")
        activity?.findViewById<TextView>(R.id.text_total_count)?.text = total

        // 平均値を求める
        val realmAve = realms.average("count").toInt()
        val ave = String.format("$realmAve 回")
        activity?.findViewById<TextView>(R.id.text_ave_count)?.text = ave

        // 自己ベストを求める
        val realmBest = realms.sort("count", Sort.DESCENDING)

        var count01: Int? = 0
        var day01: String? = ""
        realmBest.findAll().firstOrNull()?.apply {
            count01 = count
            day01 = day
        }
        activity?.findViewById<TextView>(R.id.text_rank_1)?.text = String.format("$count01 回")
        activity?.findViewById<TextView>(R.id.text_rank1_day)?.text = day01

        var count02: Int? = 0
        var day02: String? = ""
        realmBest.findAll().getOrNull(1)?.apply {
            count02 = count
            day02 = day
        }
        activity?.findViewById<TextView>(R.id.text_rank_2)?.text = String.format("$count02 回")
        activity?.findViewById<TextView>(R.id.text_rank2_day)?.text = day02

        var count03: Int? = 0
        var day03: String? = ""
        realmBest.findAll().getOrNull(2)?.apply {
            count03 = count
            day03 = day
        }
        activity?.findViewById<TextView>(R.id.text_rank_3)?.text = String.format("$count03 回")
        activity?.findViewById<TextView>(R.id.text_rank3_day)?.text = day03
    }
}