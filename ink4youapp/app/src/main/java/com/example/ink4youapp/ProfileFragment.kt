package com.example.ink4youapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ink4youapp.adapters.EstilosTatuagensMenuDtoAdapter
import com.example.ink4youapp.adapters.TatuagemSimpleDtoAdapter
import com.example.ink4youapp.adapters.TatuagemSimpleEditDtoAdapter
import com.example.ink4youapp.models.EstilosTatuagensDtoModel
import com.example.ink4youapp.models.TatuagemDtoImageModel
import java.util.ArrayList

class ProfileFragment : Fragment() {
    private lateinit var tv_name: TextView
    private lateinit var tv_username_insta: TextView
    private lateinit var tv_about: TextView

    private var tattooList = ArrayList<TatuagemDtoImageModel>()
    private lateinit var rvTatuagens : RecyclerView
    private lateinit var  adapter : TatuagemSimpleEditDtoAdapter

    private lateinit var rvTatuagensInsta : RecyclerView
    private lateinit var  adapterInsta : TatuagemSimpleEditDtoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View? {
      
        var view: View
        val prefs = this.activity?.getSharedPreferences("storage", 0)
        val userType = prefs?.getString("user_type", "")

        if (userType.equals("tattooArtist")) {
            view = inflater.inflate(R.layout.fragment_profile, container, false)
            tv_name = view.findViewById(R.id.tv_name)
            tv_username_insta = view.findViewById(R.id.tv_username_insta)
            tv_about = view.findViewById(R.id.tv_about)

            rvTatuagens = view.findViewById(R.id.tattoosRecyclerView)
            rvTatuagens.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = TatuagemSimpleEditDtoAdapter(this.requireContext(), tattooList)
            rvTatuagens.adapter = adapter

            rvTatuagensInsta = view.findViewById(R.id.tattoosInstaRecyclerView)
            rvTatuagensInsta.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapterInsta = TatuagemSimpleEditDtoAdapter(this.requireContext(), tattooList)
            rvTatuagensInsta.adapter = adapterInsta

            val btn = view.findViewById<ImageButton>(R.id.btn_edit)
            btn.setOnClickListener { view ->
                val intent = Intent(activity, TattooManager::class.java)
                startActivity(intent)
            }

            val btnEditProfile = view.findViewById<Button>(R.id.btn_edit_profile)
            btnEditProfile.setOnClickListener { view ->
                val intent = Intent(activity, EditUserTattooProfile::class.java)
                startActivity(intent)
            }

            setUserInfos(view)
            tatuagebsAssemble()

        } else {
            view = inflater.inflate(R.layout.activity_edit_user_profile, container, false)
        }

        return view
    }

    private fun setUserInfos(view: View) {
        val prefs = this.activity?.getSharedPreferences("storage", 0)

        val name = prefs?.getString("nome", "")
        val instagramUsername = prefs?.getString("username_insta", "")
        val about = prefs?.getString("sobre", "")

        tv_name.text = name
        tv_username_insta.text = instagramUsername
        tv_about.text = about
    }

    private fun tatuagebsAssemble() {
        val tattoo1 = TatuagemDtoImageModel(R.drawable.tattoo1, "tatuagem1")
        tattooList.add(tattoo1)

        val tattoo2 = TatuagemDtoImageModel(R.drawable.tattoo2, "tatuagem1")
        tattooList.add(tattoo2)

        val tattoo3 = TatuagemDtoImageModel(R.drawable.tattoo3, "tatuagem1")
        tattooList.add(tattoo3)

        val tattoo4 = TatuagemDtoImageModel(R.drawable.tattoo1, "tatuagem1")
        tattooList.add(tattoo4)

        val tattoo5 = TatuagemDtoImageModel(R.drawable.tattoo2, "tatuagem1")
        tattooList.add(tattoo5)

        val tattoo6 = TatuagemDtoImageModel(R.drawable.tattoo1, "tatuagem1")
        tattooList.add(tattoo6)

        val tattoo7 = TatuagemDtoImageModel(R.drawable.tattoo2, "tatuagem1")
        tattooList.add(tattoo7)
    }
}