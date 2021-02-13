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

        // 合計値を求める
        val realmSum = realm.where(RealmData::class.java).sum("count") ?: 0
        val sum = String.format("$realmSum 回")
        activity?.findViewById<TextView>(R.id.text_sum_count)?.text = sum

        // 最大値を求める
        val realmMax = realm.where(RealmData::class.java).max("count") ?: 0
        val max = String.format("$realmMax 回")
        activity?.findViewById<TextView>(R.id.text_max_count)?.text = max

        // 平均値を求める
        val realmAve = realm.where(RealmData::class.java).average("count").toInt()
        val ave = String.format("$realmAve 回")
        activity?.findViewById<TextView>(R.id.text_ave_count)?.text = ave
    }
}