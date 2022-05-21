package com.example.ink4youapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.R
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
                itemView.findViewById<TextView>(R.id.tv_infos).text = "${nome} - ${uf}";
//                itemView.findViewById<TextView>(R.id.tv_styles).text = nome;
            }
        }
    }
}