package com.example.ink4youapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.adapters.EstilosTatuagensMenuDtoAdapter
import com.example.ink4youapp.adapters.TatuadorCardDtoAdapter
import com.example.ink4youapp.models.EstilosTatuagensDtoModel
import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.models.TatuadorDTO
import com.example.ink4youapp.models.fakeTatuadores
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.services.TatuadorService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ArtistsFragment : Fragment() {

    private var estilosList = ArrayList<EstilosTatuagensDtoModel>();
    private val retrofit = Rest.getInstance();
    private var artistsList: MutableList<TatuadorDTO> = mutableListOf();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_artists, container, false);

        estilosMock();

        val estilosTattoosRecycleViewMenu = view.findViewById<RecyclerView>(R.id.estilosTattoosRecycleViewMenuTatuadoresScreen);
        estilosTattoosRecycleViewMenu.adapter = EstilosTatuagensMenuDtoAdapter(estilosList);
        estilosTattoosRecycleViewMenu.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);

        getTattooArtist()

        Handler().postDelayed({
            val TattooArtistsRecycleView = view.findViewById<RecyclerView>(R.id.rv_tattooArtists);
            TattooArtistsRecycleView.adapter = TatuadorCardDtoAdapter(artistsList);
            TattooArtistsRecycleView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
        }, 1500)

        return view
    }

    fun getTattooArtist() {
        val tattoo = retrofit.create(TatuadorService::class.java);

        tattoo.getTattooArtists().enqueue(object: Callback<List<TatuadorDTO>> {
            override fun onResponse(
                call: Call<List<TatuadorDTO>>,
                response: Response<List<TatuadorDTO>>
            ) {
                if (response.isSuccessful) {
                    println("foi")
                    println(response.body());

                    if (response.body() != null) {
                        artistsList = response.body()!!.toMutableList();
                    } else {
                        println()
                    }

                } else {
                    println("n foi")
                    println(response)
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<List<TatuadorDTO>>, t: Throwable) {
                t.message?.let { println("erro ---- " + it) }
            }
        });
    }

    private fun estilosMock() {
        estilosList.add(EstilosTatuagensDtoModel("Old School"));
        estilosList.add(EstilosTatuagensDtoModel("Black Work"));
        estilosList.add(EstilosTatuagensDtoModel("Aquarela"));
        estilosList.add(EstilosTatuagensDtoModel("Pontilismo"));
        estilosList.add(EstilosTatuagensDtoModel("Oriental"));
    }
}