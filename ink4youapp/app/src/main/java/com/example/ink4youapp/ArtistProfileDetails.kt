package com.example.ink4youapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.ink4youapp.adapters.TatuagemSimpleDtoAdapter
import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.models.Tatuagem
import com.example.ink4youapp.models.TatuagemDTO
import com.example.ink4youapp.models.TatuagemDtoImageModel
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.services.TatuadorService
import com.example.ink4youapp.services.TatuagemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ArtistProfileDetails : AppCompatActivity() {
    private val retrofit = Rest.getInstance();
    private var idTattooArtist: Int = 0;
    private var tattooList = ArrayList<TatuagemDtoImageModel>()
    private lateinit var rvTatuagens : RecyclerView
    private lateinit var  adapter : TatuagemSimpleDtoAdapter

    private var tattooInstaList = ArrayList<TatuagemDtoImageModel>();
    private lateinit var rvTatuagensInsta : RecyclerView
    private lateinit var  adapterInsta : TatuagemSimpleDtoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_profile_details)

        var it = intent;

        idTattooArtist = it.getIntExtra("idTatuador", 0);
//        println("idTatuador" + idTattooArtist.toString());

        if (idTattooArtist != 0)
            getTattooArtist(idTattooArtist);

        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish();
        }
    }

    fun getTattooArtist(id: Int) {
        val tattoo = retrofit.create(TatuadorService::class.java);

        tattoo.getTattooArtist(id).enqueue(object: Callback<Tatuador> {
            override fun onResponse(call: Call<Tatuador>, response: Response<Tatuador>) {
                if (response.isSuccessful) {
                    println("foi")
                    println(response.body());
                    response.body()?.let {
                        showTattooArtistDetails(it);
                        getTattoosImageDto();
                    }
                } else {
                    println("n foi")
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<Tatuador>, t: Throwable) {
                t.message?.let { println(it) }
            }
        });
    }

    fun showTattooArtistDetails(tattooArtist: Tatuador) {
        with(tattooArtist) {
            findViewById<TextView>(R.id.tv_name).text = nome;
            findViewById<TextView>(R.id.tv_username_insta).text = conta_instagram;
            findViewById<TextView>(R.id.tv_about).text = sobre;
            findViewById<TextView>(R.id.tv_logradouro).text = logradouro;
            findViewById<TextView>(R.id.tv_uf).text = " - ${uf}";
        }
        Glide.with(this)
            .load(tattooArtist.foto_perfil)
            .centerCrop()
            .into(findViewById(R.id.iv_profile))
    }

    fun getTattoosImageDto() {
        val tattoo = retrofit.create(TatuagemService::class.java);

        tattoo.getTattoosByTattooArtist(idTattooArtist).enqueue(object: Callback<List<Tatuagem>> {
            override fun onResponse(
                call: Call<List<Tatuagem>>,
                response: Response<List<Tatuagem>>
            ) {
                if (response.isSuccessful) {
                    println("foi")
                    println(response.body());

                    if (response.body() != null) {
                        response.body()!!.toList().forEach { tattoo ->
//                            println("elemento da vez ----- " + tattoo);
                            tattooList.add(TatuagemDtoImageModel(tattoo.id_tatuagem?:0, tattoo.src_imagem?:"", tattoo.titulo));
                            rvTatuagens = findViewById(R.id.tattoosRecyclerView)
                            rvTatuagens.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                            adapter = TatuagemSimpleDtoAdapter(baseContext, tattooList)
                            rvTatuagens.adapter = adapter
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

            override fun onFailure(call: Call<List<Tatuagem>>, t: Throwable) {
                t.message?.let { println("erro ---- " + it) }
            }
        });
    }
}