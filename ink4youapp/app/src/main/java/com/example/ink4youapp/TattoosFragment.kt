package com.example.ink4youapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ink4youapp.adapters.EstilosTatuagensMenuDtoAdapter
import com.example.ink4youapp.adapters.TatuagemCardDtoAdapter
import com.example.ink4youapp.adapters.TatuagemSimpleDtoAdapter
import com.example.ink4youapp.models.EstilosTatuagensDtoModel
import com.example.ink4youapp.models.TatuagemDtoImageModel
import com.example.ink4youapp.models.fakeTatuagens
import java.util.ArrayList

class TattoosFragment : Fragment() {

    private var tattooList = ArrayList<TatuagemDtoImageModel>()
    private var estilosList = ArrayList<EstilosTatuagensDtoModel>()
    private lateinit var rvTatuagens : RecyclerView
    private lateinit var  adapter : TatuagemSimpleDtoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

    ): View? {
        var view = inflater.inflate(R.layout.fragment_tattoos, container, false)

        rvTatuagens = view.findViewById(R.id.tattoosRecyclerView)
        rvTatuagens.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = TatuagemSimpleDtoAdapter(this.requireContext(), tattooList)
        rvTatuagens.adapter = adapter

        estilosMock();

        val estilosTattoosRecycleViewMenu = view.findViewById<RecyclerView>(R.id.estilosTattoosRecycleViewMenu);
        estilosTattoosRecycleViewMenu.adapter = EstilosTatuagensMenuDtoAdapter(estilosList);
        estilosTattoosRecycleViewMenu.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);

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

    private fun estilosMock() {
        estilosList.add(EstilosTatuagensDtoModel("Old School"));
        estilosList.add(EstilosTatuagensDtoModel("Black Work"));
        estilosList.add(EstilosTatuagensDtoModel("Aquarela"));
        estilosList.add(EstilosTatuagensDtoModel("Pontilismo"));
        estilosList.add(EstilosTatuagensDtoModel("Oriental"));
    }


}