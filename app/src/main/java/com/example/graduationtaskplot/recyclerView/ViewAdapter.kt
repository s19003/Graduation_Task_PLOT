package com.example.graduationtaskplot.recyclerView

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationtaskplot.R
import com.example.graduationtaskplot.realm.RealmData
import io.realm.RealmResults

class ViewAdapter(realmResults: RealmResults<RealmData>) : RecyclerView.Adapter<ViewHolder>() {
    private var rResults: RealmResults<RealmData> = realmResults

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val realmData = rResults[position]
        holder.countText?.text = String.format("${realmData?.count} 回")
        holder.dateText?.text = DateFormat.format("yyyy/MM/dd", realmData?.date)
    }

    override fun getItemCount(): Int {
        return rResults.size
    }
}