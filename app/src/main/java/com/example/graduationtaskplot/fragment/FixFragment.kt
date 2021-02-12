package com.example.graduationtaskplot.fragment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.graduationtaskplot.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FixFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fix_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // ボタンの非表示
        activity?.findViewById<FloatingActionButton>(R.id.count_button)?.apply {
            visibility = View.INVISIBLE
        }
    }
}