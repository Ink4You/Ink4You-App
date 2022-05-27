package com.example.ink4youapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.R
import com.example.ink4youapp.models.TatuadorDTO

class TatuadorCardDtoAdapter(val tatuadores: MutableList<TatuadorDTO>) : RecyclerView.Adapter<TatuadorCardDtoAdapter.TatuadorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TatuadorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_tattoo_artist_card, parent, false);
        return TatuadorViewHolder(view);
    }

    override fun getItemCount(): Int = tatuadores.size;

    override fun onBindViewHolder(holder: TatuadorCardDtoAdapter.TatuadorViewHolder, position: Int) {
        holder.bind(tatuadores[position])
    }

    inner class TatuadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tatuador: TatuadorDTO) {
            with(tatuador) {
                itemView.findViewById<TextView>(R.id.tv_name).text = nome;
                itemView.findViewById<TextView>(R.id.tv_description).text = sobre;
                itemView.findViewById<TextView>(R.id.tv_logradouro).text = logradouro;
                itemView.findViewById<TextView>(R.id.tv_cep_uf).text = "${cep} - ${uf}";
            }
        }
    }
}