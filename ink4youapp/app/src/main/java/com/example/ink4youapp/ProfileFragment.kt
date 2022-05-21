package com.example.ink4youapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ink4youapp.adapters.EstilosTatuagensMenuDtoAdapter
import com.example.ink4youapp.adapters.TatuagemSimpleDtoAdapter
import com.example.ink4youapp.models.EstilosTatuagensDtoModel
import com.example.ink4youapp.models.TatuagemDtoImageModel
import java.util.ArrayList

class ProfileFragment : Fragment() {
    private var tattooList = ArrayList<TatuagemDtoImageModel>()
    private lateinit var rvTatuagens : RecyclerView
    private lateinit var  adapter : TatuagemSimpleDtoAdapter

    private lateinit var rvTatuagensInsta : RecyclerView
    private lateinit var  adapterInsta : TatuagemSimpleDtoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        rvTatuagens = view.findViewById(R.id.tattoosRecyclerView)
        rvTatuagens.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = TatuagemSimpleDtoAdapter(this.requireContext(), tattooList)
        rvTatuagens.adapter = adapter

        rvTatuagensInsta = view.findViewById(R.id.tattoosInstaRecyclerView)
        rvTatuagensInsta.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapterInsta = TatuagemSimpleDtoAdapter(this.requireContext(), tattooList)
        rvTatuagensInsta.adapter = adapterInsta

        tatuagebsAssemble()
        return view
    }

    private fun tatuagebsAssemble() {
        val tattoo1 = TatuagemDtoImageModel(R.drawable.tattoo1, "tatuagem1")
        tattooList.add(tattoo1)

        val tattoo2 = TatuagemDtoImageModel(R.drawable.tattoo2, "tatuagem1")
        tattooList.add(tattoo2)

        val tattoo3 = TatuagemDtoImageModel(R.drawable.tattoo3, "tatuagem1")
        tattooList.add(tattoo3)

        val tattoo4 = TatuagemDtoImageModel(R.drawable.tattoo1, "tatuagem1")
        tattooList.add(tattoo4)

        val tattoo5 = TatuagemDtoImageModel(R.drawable.tattoo2, "tatuagem1")
        tattooList.add(tattoo5)

        val tattoo6 = TatuagemDtoImageModel(R.drawable.tattoo1, "tatuagem1")
        tattooList.add(tattoo6)

        val tattoo7 = TatuagemDtoImageModel(R.drawable.tattoo2, "tatuagem1")
        tattooList.add(tattoo7)
    }
}