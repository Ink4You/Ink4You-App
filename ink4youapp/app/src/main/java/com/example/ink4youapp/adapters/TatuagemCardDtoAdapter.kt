package com.example.ink4youapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.R
import com.example.ink4youapp.models.TatuagemDTO

class TatuagemCardDtoAdapter(val tatuagens: MutableList<TatuagemDTO>) : RecyclerView.Adapter<TatuagemCardDtoAdapter.TatuagemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TatuagemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_tattoo_card, parent, false);
        return TatuagemViewHolder(view);
    }

    override fun getItemCount(): Int = tatuagens.size;

    override fun onBindViewHolder(holder: TatuagemViewHolder, position: Int) {
        holder.bind(tatuagens[position])
    }

    inner class TatuagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tatuagem: TatuagemDTO) {
            with(tatuagem) {
                itemView.findViewById<TextView>(R.id.tv_title).text = titulo.toString();
                itemView.findViewById<TextView>(R.id.tv_artist).text = nome;
            }
        }
    }
}