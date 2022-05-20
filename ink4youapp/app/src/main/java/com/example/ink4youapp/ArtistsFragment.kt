package com.example.ink4youapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.adapters.EstilosTatuagensMenuDtoAdapter
import com.example.ink4youapp.models.EstilosTatuagensDtoModel
import java.util.ArrayList

class ArtistsFragment : Fragment() {

    private var estilosList = ArrayList<EstilosTatuagensDtoModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_artists, container, false)


        estilosMock();

        val estilosTattoosRecycleViewMenu = view.findViewById<RecyclerView>(R.id.estilosTattoosRecycleViewMenuTatuadoresScreen);
        estilosTattoosRecycleViewMenu.adapter = EstilosTatuagensMenuDtoAdapter(estilosList);
        estilosTattoosRecycleViewMenu.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);

        return view
    }

    private fun estilosMock() {
        estilosList.add(EstilosTatuagensDtoModel("Old School"));
        estilosList.add(EstilosTatuagensDtoModel("Black Work"));
        estilosList.add(EstilosTatuagensDtoModel("Aquarela"));
        estilosList.add(EstilosTatuagensDtoModel("Pontilismo"));
        estilosList.add(EstilosTatuagensDtoModel("Oriental"));
    }
}