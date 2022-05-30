package com.example.ink4youapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ink4youapp.adapters.EstilosTatuagensMenuDtoAdapter
import com.example.ink4youapp.adapters.TatuagemSimpleDtoAdapter
import com.example.ink4youapp.adapters.TatuagemSimpleEditDtoAdapter
import com.example.ink4youapp.models.EstilosTatuagensDtoModel
import com.example.ink4youapp.models.Tatuagem
import com.example.ink4youapp.models.TatuagemDTO
import com.example.ink4youapp.models.TatuagemDtoImageModel
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.services.TatuagemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ProfileFragment : Fragment() {
    private val retrofit = Rest.getInstance();
    private lateinit var ll_insta_tattoo_list: LinearLayout
    private lateinit var ll_insta_box: LinearLayout
    private lateinit var tv_name: TextView
    private lateinit var tv_username_insta: TextView
    private lateinit var tv_about: TextView

    private lateinit var IVPreviewImage: ImageView

    private var idTattooArtist: Int? = null
    private var tattooList = ArrayList<TatuagemDtoImageModel>()
    private var tattooInstaList = ArrayList<TatuagemDtoImageModel>()
    private lateinit var rvTatuagens: RecyclerView
    private lateinit var adapter: TatuagemSimpleDtoAdapter

    private lateinit var rvTatuagensInsta: RecyclerView
    private lateinit var adapterInsta: TatuagemSimpleDtoAdapter

    private var allowRefresh = false

    override fun onResume() {
        super.onResume()
        //Initialize();
        if (allowRefresh) {
            allowRefresh = false
            setUserInfos()
            showOrHideInstagramList()
            getTattoosImageDto()
        }
    }

    override fun onPause() {
        super.onPause()
        if (!allowRefresh) allowRefresh = true
    }

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

            IVPreviewImage = view.findViewById(R.id.iv_profile)

            ll_insta_box = view.findViewById(R.id.ll_insta_box)
            ll_insta_tattoo_list = view.findViewById(R.id.ll_insta_tattoo_list)
            tv_name = view.findViewById(R.id.tv_name)
            tv_username_insta = view.findViewById(R.id.tv_username_insta)
            tv_about = view.findViewById(R.id.tv_about)

            Handler().postDelayed({
                rvTatuagens = view.findViewById(R.id.tattoosRecyclerView)
                rvTatuagens.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = TatuagemSimpleDtoAdapter(this.requireContext(), tattooList)
                rvTatuagens.adapter = adapter
            }, 5000)

            rvTatuagensInsta = view.findViewById(R.id.tattoosInstaRecyclerView)
            rvTatuagensInsta.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            adapterInsta = TatuagemSimpleDtoAdapter(this.requireContext(), tattooInstaList)
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

            val btnLogout = view.findViewById<ImageView>(R.id.iv_logout)
            btnLogout.setOnClickListener { view ->
                clearSharedPreferences()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }

            setUserInfos()
            showOrHideInstagramList()
            getTattoosImageDto()
            tatuagebsAssemble(tattooInstaList)

        } else {
            view = inflater.inflate(R.layout.activity_edit_user_profile, container, false)

            val btnEdit: ImageView = view.findViewById(R.id.BSelectImage)
            btnEdit.setOnClickListener {
                //imageChooser(it)
            }
        }

        return view
    }

    private fun setUserInfos() {
        val prefs = this.activity?.getSharedPreferences("storage", 0)

        idTattooArtist = prefs?.getInt("id_tatuador", 0)
        val profilePic = prefs?.getString("foto_perfil", "")
        val usernameInsta = prefs?.getString("username_insta", "")
        val name = prefs?.getString("nome", "")
        val about = prefs?.getString("sobre", "")

        Glide.with(IVPreviewImage.context)
            .load(profilePic)
            .centerCrop()
            .into(IVPreviewImage)

        tv_name.text = name
        tv_username_insta.text = usernameInsta
        tv_about.text = about
    }

    private fun showOrHideInstagramList() {
        val prefs = this.activity?.getSharedPreferences("storage", 0)
        val instagramUsername = prefs?.getString("username_insta", "")

        if (instagramUsername == "") {
            ll_insta_box.visibility = View.GONE
            ll_insta_tattoo_list.visibility = View.GONE

        } else {
            ll_insta_box.visibility = View.VISIBLE
            ll_insta_tattoo_list.visibility = View.VISIBLE
        }
    }

    fun getTattoosImageDto() {
        val tattoo = retrofit.create(TatuagemService::class.java);

        idTattooArtist?.let {
            tattoo.getTattoosByTattooArtist(it).enqueue(object : Callback<List<Tatuagem>> {
                override fun onResponse(
                    call: Call<List<Tatuagem>>,
                    response: Response<List<Tatuagem>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            response.body()!!.forEach { tattoo ->
                                tattoo.src_imagem?.let {
                                    TatuagemDtoImageModel(
                                        tattoo.id_tatuagem ?: 0,
                                        it, tattoo.titulo ?: "titulo"
                                    )
                                }?.let { tattooList.add(it) }
                            }
                        } else {
                            println(response.errorBody())
                        }
                    } else {
                        println(response.code())
                    }
                }

                override fun onFailure(call: Call<List<Tatuagem>>, t: Throwable) {
                    t.message?.let { println("error: " + it) }
                }
            })
        };
    }

    fun getInstagramTattoos() {
        val tattoo = retrofit.create(TatuagemService::class.java);

        tattoo.getInstaTattoos().enqueue(object : Callback<List<Tatuagem>> {
            override fun onResponse(
                call: Call<List<Tatuagem>>,
                response: Response<List<Tatuagem>>
            ) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<List<Tatuagem>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun tatuagebsAssemble(list: ArrayList<TatuagemDtoImageModel>) {
        val tattoo1 = TatuagemDtoImageModel(
            1,
            "https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png",
            "tatuagem1"
        )
        list.add(tattoo1)

        val tattoo2 = TatuagemDtoImageModel(
            2,
            "https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png",
            "tatuagem1"
        )
        list.add(tattoo2)

        val tattoo3 = TatuagemDtoImageModel(
            3,
            "https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png",
            "tatuagem1"
        )
        list.add(tattoo3)

        val tattoo4 = TatuagemDtoImageModel(
            4,
            "https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png",
            "tatuagem1"
        )
        list.add(tattoo4)

        val tattoo5 = TatuagemDtoImageModel(
            5,
            "https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png",
            "tatuagem1"
        )
        list.add(tattoo5)

        val tattoo6 = TatuagemDtoImageModel(
            6,
            "https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png",
            "tatuagem1"
        )
        list.add(tattoo6)

        val tattoo7 = TatuagemDtoImageModel(
            7,
            "https://user-images.githubusercontent.com/30958501/68553365-d03aa880-0463-11ea-9e98-96c6a10265e8.png",
            "tatuagem1"
        )
        list.add(tattoo7)

    }

    private fun clearSharedPreferences() {
        val prefs = this.activity?.getSharedPreferences("storage", 0)
        if (prefs != null) {
            prefs.edit().clear().commit()
        }
    }
}