package com.example.ink4youapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.adapters.EstilosTatuagensMenuDtoAdapter
import com.example.ink4youapp.adapters.TatuadorCardDtoAdapter
import com.example.ink4youapp.models.EstilosTatuagensDtoModel
import com.example.ink4youapp.models.Tatuador
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
//    private var idTattooArtist: Int = 0;
    private lateinit var artistsList: List<Tatuador>;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_artists, container, false);

//        val it = Intent();
//        idTattooArtist = it.getIntExtra("idTatuador", 68);

        estilosMock();

        val estilosTattoosRecycleViewMenu = view.findViewById<RecyclerView>(R.id.estilosTattoosRecycleViewMenuTatuadoresScreen);
        estilosTattoosRecycleViewMenu.adapter = EstilosTatuagensMenuDtoAdapter(estilosList);
        estilosTattoosRecycleViewMenu.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);

        getTattooArtist()

        val TattooArtistsRecycleView = view.findViewById<RecyclerView>(R.id.rv_tattooArtists);
        TattooArtistsRecycleView.adapter = TatuadorCardDtoAdapter(fakeTatuadores());
        TattooArtistsRecycleView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);

        return view
    }

    fun getTattooArtist() {
        val tattoo = retrofit.create(TatuadorService::class.java);

        tattoo.getTattooArtists().enqueue(object: Callback<List<Tatuador>> {
            override fun onResponse(
                call: Call<List<Tatuador>>,
                response: Response<List<Tatuador>>
            ) {
                if (response.isSuccessful) {
                    println("foi")
                    println(response.body());

                    if (response.body() != null) {
                        artistsList = response.body()!!.toList();
                    }

                } else {
                    println("n foi")
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<List<Tatuador>>, t: Throwable) {
                t.message?.let { println(it) }
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