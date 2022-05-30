package com.example.ink4youapp

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.adapters.TatuadorCardCarouselDtoAdapter
import com.example.ink4youapp.adapters.TatuadorCardDtoAdapter
import com.example.ink4youapp.adapters.TatuagemCardDtoAdapter
import com.example.ink4youapp.models.*
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.services.EstiloService
import com.example.ink4youapp.services.TatuadorService
import com.example.ink4youapp.services.TatuagemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExploreFragment : Fragment() {

//    val fakeStyles = arrayOf("Estilo1", "Estilo2", "Estilo3", "Estilo4");
    private val retrofit = Rest.getInstance();
    private var artistsList: MutableList<TatuadorDTO> = mutableListOf();
    private var popularSylesList: MutableList<Estilo> = mutableListOf();
    private var tattoosList: MutableList<TatuagemDTO> = mutableListOf();
    val amountDefault = 4;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_explore, container, false)

        getTattoos();

        Handler().postDelayed({
            val recyclerViewNewTattoos = view.findViewById<RecyclerView>(R.id.recyclerViewNewTattoos);
            recyclerViewNewTattoos.adapter = TatuagemCardDtoAdapter(tattoosList);
            recyclerViewNewTattoos.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);
        }, 5000)

        getPopularStyles();

        Handler().postDelayed({
            popularStyles();
        }, 5000)

        getTattooArtist()

        Handler().postDelayed({
            val recyclerViewNewTattoosArtists = view.findViewById<RecyclerView>(R.id.recyclerViewNewTattoosArtists);
            recyclerViewNewTattoosArtists.adapter = TatuadorCardCarouselDtoAdapter(artistsList);
            recyclerViewNewTattoosArtists.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);
        }, 5000)

        return view;
    }

    fun popularStyles() {
        val fmStylesIds = arrayOf(R.id.fm_first_style, R.id.fm_second_style, R.id.fm_third_style, R.id.fm_fourth_style);

        var i = 0;
        while (i < fmStylesIds.size) {
            setPopularStyle(fmStylesIds[i], popularSylesList[i].titulo, popularSylesList[i].img_estilo);
            i++;
        }
        i = 0;
    }

    fun setPopularStyle(frame: Int, text: String, imageUrl: String) {
        var ft = childFragmentManager.beginTransaction();
        var styleFragment : Fragment = ExplorePopularStyleFragment();

        var bundle = Bundle();
        bundle.putString("title", text)
        bundle.putString("imageUrl", imageUrl)
        styleFragment.setArguments(bundle);

        ft.add(frame, styleFragment, styleFragment.javaClass.name)
        ft.commitAllowingStateLoss();
    }

    fun getTattooArtist() {
        val tattoo = retrofit.create(TatuadorService::class.java);

        tattoo.getTattooArtistsByQttd(amountDefault).enqueue(object: Callback<List<TatuadorDTO>> {
            override fun onResponse(
                call: Call<List<TatuadorDTO>>,
                response: Response<List<TatuadorDTO>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        artistsList = response.body()!!.toMutableList();
                    } else {
                        println(response)
                    }

                } else {
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<List<TatuadorDTO>>, t: Throwable) {
                t.message?.let { println("erro: " + it) }
            }
        });
    }

    fun getTattoos() {
        val tattoo = retrofit.create(TatuagemService::class.java);

        tattoo.getTattoosByQttd(amountDefault).enqueue(object: Callback<List<TatuagemDTO>> {
            override fun onResponse(
                call: Call<List<TatuagemDTO>>,
                response: Response<List<TatuagemDTO>>
            ) {
                if (response.isSuccessful) {
                    println("foi")
                    println(response.body());

                    if (response.body() != null) {
                        tattoosList = response.body()!!.toMutableList();
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

    fun getPopularStyles() {
        val tattoo = retrofit.create(EstiloService::class.java);

        tattoo.getTattoosPopularStyles().enqueue(object: Callback<List<Estilo>> {
            override fun onResponse(
                call: Call<List<Estilo>>,
                response: Response<List<Estilo>>
            ) {
                if (response.isSuccessful) {
                    println("foi")
                    println(response.body());

                    if (response.body() != null) {
                        popularSylesList = response.body()!!.toMutableList();
                    } else {
                        println(response)
                    }

                } else {
                    println("n foi")
                    println(response)
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<List<Estilo>>, t: Throwable) {
                t.message?.let { println("erro ---- " + it) }
            }
        });
    }
}