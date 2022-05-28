package com.example.ink4youapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ink4youapp.R
import com.example.ink4youapp.models.TatuagemDTO
import com.example.ink4youapp.models.TatuagemDtoImageModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class TatuagemSimpleEditDtoAdapter(
    private val context: Context,
    private var tatuagensList: List<TatuagemDtoImageModel>
) : RecyclerView.Adapter<TatuagemSimpleEditDtoAdapter.TatuagensViewHolder>() {

    private lateinit var bottomSheetDialog: BottomSheetDialog;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TatuagensViewHolder {
        bottomSheetDialog = BottomSheetDialog(this.context)

        val view = LayoutInflater.from(parent.context).inflate(R.layout.tattoo_item_container, parent, false)

        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(bottomSheetView);

        return TatuagensViewHolder(view)
    }

    override fun onBindViewHolder(holder: TatuagensViewHolder, position: Int) {
        val tattooList = tatuagensList[position]


        holder.ivTatuagens.setImageResource(tattooList.image)
        holder.tvTatuagens.setText(tattooList.title)

        holder.itemView.setOnClickListener {
//            Toast.makeText(context, ""+tattooList.title, Toast.LENGTH_SHORT).show()
            bottomSheetDialog.show();

        }

        println("teste-------" + bottomSheetDialog.findViewById<LinearLayout>(R.id.ll_edit))
        bottomSheetDialog.findViewById<LinearLayout>(R.id.ll_edit)?.setOnClickListener {
            Toast.makeText(context, "Abrir editar", Toast.LENGTH_LONG).show()
        }

        bottomSheetDialog.findViewById<LinearLayout>(R.id.ll_delete)?.setOnClickListener {
            Toast.makeText(context, "Excluindo", Toast.LENGTH_LONG).show()
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