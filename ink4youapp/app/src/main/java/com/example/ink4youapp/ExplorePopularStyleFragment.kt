package com.example.ink4youapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ExplorePopularStyleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_explore_popular_style, container, false)

        val bundle = arguments
        if (bundle != null) {
            val style = bundle.getString("style")
            view.findViewById<TextView>(R.id.tv_style).text = style;
        }

        return view;
    }
}