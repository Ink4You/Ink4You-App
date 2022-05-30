package com.example.ink4youapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ink4youapp.ArtistProfileDetails
import com.example.ink4youapp.R
import com.example.ink4youapp.TattooDetails
import com.example.ink4youapp.models.TatuadorDTO

class TatuadorCardCarouselDtoAdapter(val tatuadores: MutableList<TatuadorDTO>) : RecyclerView.Adapter<TatuadorCardCarouselDtoAdapter.TatuadorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TatuadorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_carousel_tattoo_artist_card, parent, false);
        return TatuadorViewHolder(view);
    }

    override fun getItemCount(): Int = tatuadores.size;

    override fun onBindViewHolder(holder: TatuadorCardCarouselDtoAdapter.TatuadorViewHolder, position: Int) {
        holder.bind(tatuadores[position])
    }

    inner class TatuadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tatuador: TatuadorDTO) {
            with(tatuador) {
                var imageView = itemView.findViewById<ImageView>(R.id.iv_profile);
                itemView.findViewById<TextView>(R.id.tv_infos).text = "${nome} - ${uf}";
                itemView.findViewById<TextView>(R.id.tv_description).text = sobre;
                Glide.with(itemView)
                    .load(foto_perfil)
                    .centerCrop()
                    .into(imageView)

                itemView.setOnClickListener { view ->
                    val intent = Intent(view.context, ArtistProfileDetails::class.java)
                    if (id_tatuador != null){
//                        println("dados ------- " + id_tatuador)
                        intent.putExtra("idTatuador", id_tatuador);
                    }
                    ContextCompat.startActivity(view.context, intent, null);
                }
            }
        }
    }
}