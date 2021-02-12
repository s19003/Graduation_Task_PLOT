package com.example.graduationtaskplot.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationtaskplot.R

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var countText: TextView? = null
    var dateText: TextView? = null

    init {
        countText = itemView.findViewById(R.id.result_count_text)
        dateText = itemView.findViewById(R.id.date_text)
    }
}