package com.example.ink4youapp.adapters
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.R
import com.example.ink4youapp.models.EstilosTatuagensDtoModel

class EstilosTatuagensMenuDtoAdapter(
    private var estilosList: List<EstilosTatuagensDtoModel>
) : RecyclerView.Adapter<EstilosTatuagensMenuDtoAdapter.EstilosViewHolder>(){

    private lateinit var context :Context;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstilosViewHolder {
        context = parent.context;
        val view = LayoutInflater.from(context).inflate(R.layout.tattoo_estilos_button_menu, parent, false)
        return EstilosViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstilosViewHolder, position: Int) {
        val estiloList = estilosList[position]
        holder.bind(estiloList)
    }

    override fun getItemCount(): Int {
        return estilosList.size;
    }

    inner class EstilosViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        fun bind(estilo : EstilosTatuagensDtoModel) {
            with(estilo) {
                itemView.findViewById<Button>(R.id.bt_estiloMenu).text = estiloTatuagem
                    itemView.findViewById<Button>(R.id.bt_estiloMenu).setOnClickListener { view ->
                        val button: Button = itemView.findViewById<Button>(R.id.bt_estiloMenu);

                        if (!estilo.active){
                            button.background = ContextCompat.getDrawable(context, R.drawable.blue_gradient);
                            button.setTextColor(Color.WHITE);
                            estilo.active = true
                        } else {
                            estilo.active = false
                            button.background = ContextCompat.getDrawable(context, R.drawable.button_border);
                            button.setTextColor(view.resources.getColor(R.color.darker_gray_search_input));
                        }
                    }
            }
        }
    }

}