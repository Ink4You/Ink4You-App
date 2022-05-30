package com.example.ink4youapp

import android.os.Bundle
import android.os.Handler
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
import com.example.ink4youapp.models.TatuagemDTO
import com.example.ink4youapp.models.TatuagemDtoImageModel
import com.example.ink4youapp.models.fakeTatuagens
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.services.TatuagemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class TattoosFragment : Fragment() {

    private val retrofit = Rest.getInstance();
    private var tattooList = ArrayList<TatuagemDtoImageModel>()
    private var estilosList = ArrayList<EstilosTatuagensDtoModel>()
    private lateinit var rvTatuagens : RecyclerView;
    private lateinit var  adapter : TatuagemSimpleDtoAdapter;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

    ): View? {
        var view = inflater.inflate(R.layout.fragment_tattoos, container, false)

        getTattoosImageDto()

        Handler().postDelayed({
            rvTatuagens = view.findViewById(R.id.tattoosRecyclerView)
            rvTatuagens.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = TatuagemSimpleDtoAdapter(this.requireContext(), tattooList)
            rvTatuagens.adapter = adapter;
        }, 1500)

        estilosMock();

        val estilosTattoosRecycleViewMenu = view.findViewById<RecyclerView>(R.id.estilosTattoosRecycleViewMenu);
        estilosTattoosRecycleViewMenu.adapter = EstilosTatuagensMenuDtoAdapter(estilosList);
        estilosTattoosRecycleViewMenu.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);

        return view
    }

    fun getTattoosImageDto() {
        val tattoo = retrofit.create(TatuagemService::class.java);

        tattoo.getTattoos().enqueue(object: Callback<List<TatuagemDTO>> {
            override fun onResponse(
                call: Call<List<TatuagemDTO>>,
                response: Response<List<TatuagemDTO>>
            ) {
                if (response.isSuccessful) {
                    println("foi")
                    println(response.body());

                    if (response.body() != null) {
                        response.body()!!.toList().forEach { tattoo ->
//                            println("elemento da vez ----- " + tattoo);
                            tattooList.add(TatuagemDtoImageModel(tattoo.id_tatuagem?: 0,tattoo.src_imagem, tattoo.titulo?:"titulo"))
                        }
                    } else {
                        println(response)
                    }

                } else {
                    println("n foi")
                    println(response)
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<List<TatuagemDTO>>, t: Throwable) {
                t.message?.let { println("erro ---- " + it) }
            }
        });
    }

    private fun tatuagebsAssemble() {
        val tattoo1 = TatuagemDtoImageModel(1,"https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png", "tatuagem1")
        tattooList.add(tattoo1)

        val tattoo2 = TatuagemDtoImageModel(2,"https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png", "tatuagem1")
        tattooList.add(tattoo2)

        val tattoo3 = TatuagemDtoImageModel(3,"https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png", "tatuagem1")
        tattooList.add(tattoo3)

        val tattoo4 = TatuagemDtoImageModel(4,"https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png", "tatuagem1")
        tattooList.add(tattoo4)

        val tattoo5 = TatuagemDtoImageModel(5,"https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png", "tatuagem1")
        tattooList.add(tattoo5)

        val tattoo6 = TatuagemDtoImageModel(6,"https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png", "tatuagem1")
        tattooList.add(tattoo6)

        val tattoo7 = TatuagemDtoImageModel(7,"https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png", "tatuagem1")
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