package com.example.ink4youapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.models.Tatuagem
import com.example.ink4youapp.models.tatuagem
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.services.TatuadorService
import com.example.ink4youapp.services.TatuagemService
import com.example.ink4youapp.utils.SnackBar
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TattooDetails : AppCompatActivity() {

    private var idTattoo: Int = 0;
    private var idTattooArtist: Int = 0;
    private val retrofit = Rest.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tattoo_details)

        var it = intent;
        idTattoo = it.getIntExtra("idTatuagem", 0);
//        println("idTattoo" + idTattoo.toString());

        idTattooArtist = it.getIntExtra("idTatuador", 0);
//        println("idTatuador" + idTattooArtist.toString());

//        Handler().postDelayed({
            GetTatuagem();

        if (idTattooArtist != 0)
            getTattooArtist(idTattooArtist);
//        }, 5000)
    }

    fun GetTatuagem() {
        val tattoo = retrofit.create(TatuagemService::class.java)

        tattoo.getTattoo(idTattoo).enqueue(object: Callback<Tatuagem> {
            override fun onResponse(call: Call<Tatuagem>, response: Response<Tatuagem>) {
                if (response.isSuccessful) {
                    println("foi")
                    println(response.body());
                    response.body()?.let {
                        showTatooDetails(it)
                        if (idTattooArtist == 0)
                            it.id_tatuador?.let { id -> getTattooArtist(id) };
                    }
                } else {
                    println("n foi")
                    println(response.body())
                }
            }

            override fun onFailure(call: Call<Tatuagem>, t: Throwable) {
                t.message?.let { println(it) }
            }
        });
    }

    fun getTattooArtist(id: Int) {
        val tattoo = retrofit.create(TatuadorService::class.java);

        tattoo.getTattooArtist(id).enqueue(object: Callback<Tatuador> {
            override fun onResponse(call: Call<Tatuador>, response: Response<Tatuador>) {
                if (response.isSuccessful) {
                    println("foi")
                    println(response.body());
                    response.body()?.let { showTattooArtistDetails(it) }
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

    fun showTatooDetails(tattoo: Tatuagem) {
        with(tattoo) {
            findViewById<TextView>(R.id.tv_title).text = titulo;
            findViewById<TextView>(R.id.tv_local).text = local_tatuagem;
            findViewById<TextView>(R.id.tv_description).text = if (descricao != null) descricao else "teste";
        }
        Glide.with(this)
            .load(tattoo.src_imagem)
            .centerCrop()
            .into(findViewById(R.id.iv_tattoo))

        findViewById<CardView>(R.id.cv_artist).setOnClickListener { view ->
            val intent = Intent(view.context, ArtistProfileDetails::class.java)
            if (idTattooArtist != null){
//                        println("dados ------- " + id_tatuador)
                intent.putExtra("idTatuador", idTattooArtist);
            }
            ContextCompat.startActivity(view.context, intent, null);
        }
    }

    fun showTattooArtistDetails(tattooArtist: Tatuador) {
        with(tattooArtist) {
            findViewById<TextView>(R.id.tv_name).text = nome;
            findViewById<TextView>(R.id.tv_infos).text = "${cep} - ${uf}";
            findViewById<TextView>(R.id.tv_about).text = sobre;
        }
        Glide.with(this)
            .load(tattooArtist.foto_perfil)
            .centerCrop()
            .into(findViewById(R.id.iv_tattoArtist))
    }
}