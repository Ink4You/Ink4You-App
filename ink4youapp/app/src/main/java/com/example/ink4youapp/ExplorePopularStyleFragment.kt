package com.example.ink4youapp

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class ExplorePopularStyleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_explore_popular_style, container, false)

        val bundle = arguments
        if (bundle != null) {
            val title = bundle.getString("title")
            val imageUrl = bundle.getString("imageUrl")
            val ivStyle = view.findViewById<ImageView>(R.id.iv_style);
            view.findViewById<TextView>(R.id.tv_style).text = title;

            Glide.with(view)
                .load(imageUrl)
                .centerCrop()
                .into(ivStyle)
        }

        return view;
    }
}