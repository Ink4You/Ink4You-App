package com.example.ink4youapp.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ink4youapp.R
import com.example.ink4youapp.models.Tatuagem
import com.example.ink4youapp.models.TatuagemDTO
import com.example.ink4youapp.models.TatuagemDtoImageModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class TatuagemSimpleEditDtoAdapter(
    private val context: Context,
    private var tatuagensList: List<Tatuagem>
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

        Glide.with(holder.itemView)
            .load(tattooList.src_imagem)
            .into(holder.ivTatuagens)
        holder.tvTatuagens.setText(tattooList.titulo)

        holder.itemView.setOnClickListener {
            bottomSheetDialog.show();

            val tattooId = (context as Activity).findViewById<EditText>(R.id.et_tattoo_id)
            val tattooImage = (context as Activity).findViewById<EditText>(R.id.et_tattoo_image)
            val tattooTitle = (context as Activity).findViewById<EditText>(R.id.et_tattoo_title)
            val tattooLocal = (context as Activity).findViewById<EditText>(R.id.et_tattoo_local)
            val tattooDesc = (context as Activity).findViewById<EditText>(R.id.et_tattoo_description)

            val btnCreate = (context as Activity).findViewById<Button>(R.id.btn_create)
            val btnEdit = (context as Activity).findViewById<Button>(R.id.btn_update)
            val tattooImageCircle = (context as Activity).findViewById<ImageView>(R.id.iv_profile);

            bottomSheetDialog.findViewById<LinearLayout>(R.id.ll_edit)?.setOnClickListener {
                Glide.with(tattooImageCircle.context)
                    .load(tattooList.src_imagem)
                    .centerCrop()
                    .into(tattooImageCircle)

                tattooId.setText(tattooList.id_tatuagem!!.toString())
                tattooImage.setText(tattooList.src_imagem)
                tattooTitle.setText(tattooList.titulo)
                tattooLocal.setText(tattooList.local_tatuagem)
                tattooDesc.setText(tattooList.descricao)

                btnCreate.visibility = GONE
                btnEdit.visibility = VISIBLE
            }
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