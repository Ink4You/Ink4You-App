package com.example.ink4youapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

        val it = Intent();
        idTattoo = it.getIntExtra("idTatuagem", 49);
        println("id" + idTattoo.toString());
        idTattooArtist = it.getIntExtra("idTatuador", 68);
        GetTatuagem();
        getTattooArtist();
    }

    fun GetTatuagem() {
        val tattoo = retrofit.create(TatuagemService::class.java)

        tattoo.getTattoo(idTattoo).enqueue(object: Callback<Tatuagem> {
            override fun onResponse(call: Call<Tatuagem>, response: Response<Tatuagem>) {
                if (response.isSuccessful) {
                    println("foi")
                    println(response.body());
                    response.body()?.let { showTatooDetails(it) }
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

    fun getTattooArtist() {
        val tattoo = retrofit.create(TatuadorService::class.java);

        tattoo.getTattooArtist(idTattooArtist).enqueue(object: Callback<Tatuador> {
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

//            val raw: Bitmap;
//            val fotoArray: ByteArray = src_imagem.toByteArray(Charsets.UTF_8);
//            val foto = findViewById<ImageView>(R.id.iv_tattoo);
//            if (fotoArray != null) {
//                raw = BitmapFactory.decodeByteArray(fotoArray, 0, fotoArray.size)
//                foto.setImageBitmap(raw)
//            }
        }
    }

    fun showTattooArtistDetails(tattooArtist: Tatuador) {
        with(tattooArtist) {
            findViewById<TextView>(R.id.tv_name).text = nome;
            findViewById<TextView>(R.id.tv_infos).text = "${cep} - ${uf}";
            findViewById<TextView>(R.id.tv_about).text = sobre;
        }
    }
}