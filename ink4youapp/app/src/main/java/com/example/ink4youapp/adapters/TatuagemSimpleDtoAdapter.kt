package com.example.ink4youapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ink4youapp.R
import com.example.ink4youapp.TattooDetails
import com.example.ink4youapp.models.TatuagemDTO
import com.example.ink4youapp.models.TatuagemDtoImageModel

class TatuagemSimpleDtoAdapter(
    private val context: Context,
    private var tatuagensList: List<TatuagemDtoImageModel>
) : RecyclerView.Adapter<TatuagemSimpleDtoAdapter.TatuagensViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TatuagensViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tattoo_item_container, parent, false)
        return TatuagensViewHolder(view)
    }

    override fun onBindViewHolder(holder: TatuagensViewHolder, position: Int) {
        val tattooList = tatuagensList[position]

        val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)

        Glide.with(holder.itemView)
            .load(tattooList.image)
            .apply(requestOptions)
            .into(holder.ivTatuagens)

        holder.tvTatuagens.setText(tattooList.title)

        holder.itemView.setOnClickListener {
//        Toast.makeText(context, ""+tattooList.title, Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, TattooDetails::class.java)
            if (tattooList.id_tatuagem != 0){
//                        println("dados ------- " + id_tatuagem + " -- " + id_tatuador)
                intent.putExtra("idTatuagem", tattooList.id_tatuagem);
            }
            ContextCompat.startActivity(holder.itemView.context, intent, null);
        }
    }

    override fun getItemCount(): Int {
        return tatuagensList.size
    }

    class TatuagensViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        val ivTatuagens = itemView.findViewById<ImageView>(R.id.iv_tattoo_item)
        val tvTatuagens = itemView.findViewById<TextView>(R.id.tv_tattoo_title)

    }
}