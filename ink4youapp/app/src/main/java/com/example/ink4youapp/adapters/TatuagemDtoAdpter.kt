package com.example.ink4youapp.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.R
import com.example.ink4youapp.models.TatuagemDTO
import java.io.InputStream
import java.net.URL

class TatuagemDtoAdpter(val tatuagens: MutableList<TatuagemDTO>) : RecyclerView.Adapter<TatuagemDtoAdpter.TatuagemViewHolder>() {

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