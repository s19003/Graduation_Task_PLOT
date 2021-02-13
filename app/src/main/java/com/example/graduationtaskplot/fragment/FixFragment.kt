package com.example.graduationtaskplot.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationtaskplot.R
import com.example.graduationtaskplot.realm.RealmData
import com.example.graduationtaskplot.recyclerView.ViewAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.Sort

class FixFragment : Fragment() {
    // Realmデータベース
    private lateinit var realm: Realm

    // RecyclerView
    private lateinit var adapter: ViewAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        realm = Realm.getDefaultInstance()
        return inflater.inflate(R.layout.fix_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // カウントボタン非表示
        var button: FloatingActionButton? = activity?.findViewById(R.id.count_button)
        button?.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()

        // RecyclerView
        var recyclerView = activity?.findViewById<RecyclerView>(R.id.recyclerView)

        val realmResults = realm.where(RealmData::class.java)
            .findAll()
            .sort("id", Sort.DESCENDING)
        layoutManager = LinearLayoutManager(this.context)
        recyclerView?.layoutManager = layoutManager

        adapter = ViewAdapter(realmResults)
        recyclerView?.adapter = this.adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}