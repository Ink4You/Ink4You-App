package com.example.ink4youapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.adapters.TatuadorDtoAdapter
import com.example.ink4youapp.adapters.TatuagemCardDtoAdapter
import com.example.ink4youapp.models.fakeTatuadores
import com.example.ink4youapp.models.fakeTatuagens

class ExploreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_explore, container, false)

        val recyclerViewNewTattoos = view.findViewById<RecyclerView>(R.id.recyclerViewNewTattoos);
        recyclerViewNewTattoos.adapter = TatuagemCardDtoAdapter(fakeTatuagens());
        recyclerViewNewTattoos.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);

        val recyclerViewNewTattoosArtists = view.findViewById<RecyclerView>(R.id.recyclerViewNewTattoosArtists);
        recyclerViewNewTattoosArtists.adapter = TatuadorDtoAdapter(fakeTatuadores());
        recyclerViewNewTattoosArtists.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);

        return view;
    }
}