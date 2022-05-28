package com.example.ink4youapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.akiniyalocts.imgurapiexample.model.UploadResponse
import com.example.ink4youapp.adapters.TatuagemSimpleDtoAdapter
import com.example.ink4youapp.models.*
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.rest.RestIngur
import com.example.ink4youapp.services.EstiloService
import com.example.ink4youapp.services.IngurService
import com.example.ink4youapp.services.TatuagemService
import com.example.ink4youapp.services.UsuarioService
import com.example.ink4youapp.utils.SnackBar
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.ArrayList

class TattooManager : AppCompatActivity() {
    private var tattooList = ArrayList<TatuagemDtoImageModel>()
    private lateinit var rvTatuagens: RecyclerView
    private lateinit var adapter: TatuagemSimpleDtoAdapter

    private var tattooArtistId: Int? = null
    private lateinit var et_tattoo_title: EditText
    private lateinit var et_tattoo_local: EditText
    private lateinit var et_tattoo_description: EditText

    private lateinit var stylesList: Array<Estilo>
    private lateinit var spinnerStyles: Spinner

    private var selectedImageUri: Uri? = null
    private var IVPreviewImage: ImageView? = null
    private var SELECT_PICTURE: Int = 200
    private var byteArrayImage: ByteArray? = null
    private var base64Image: String? = null
    private var imageLink: String? = null

    private val retrofit = Rest.getInstance()
    private val ingurApi = RestIngur.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tattoo_manager)

        IVPreviewImage = findViewById(R.id.iv_profile);

        tattooArtistId = getTattooArtistId()
        et_tattoo_title = findViewById(R.id.et_tattoo_title)
        et_tattoo_local = findViewById(R.id.et_tattoo_local)
        et_tattoo_description = findViewById(R.id.et_tattoo_description)
        spinnerStyles = findViewById(R.id.spinner_styles)

        rvTatuagens = findViewById(R.id.tattoosRecyclerView)
        rvTatuagens.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = TatuagemSimpleDtoAdapter(baseContext, tattooList)
        rvTatuagens.adapter = adapter

        populateTattooStylesSpinner()
        tatuagebsAssemble()
    }

    fun imageChooser(view: View) {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data?.data
                if (selectedImageUri != null) {
                    IVPreviewImage?.setImageURI(selectedImageUri)
                    val imageStream = contentResolver.openInputStream(selectedImageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)

                    val saida = ByteArrayOutputStream()
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, saida)
                    val img = saida.toByteArray()

                    byteArrayImage = img
                    base64Image = Base64.encodeToString(img, Base64.DEFAULT)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createTattoo(view: View) {
        if (!isValidFields(view)) { return }

        val tattoo = retrofit.create(TatuagemService::class.java)
        val ingur = ingurApi.create(IngurService::class.java)

        val requestBody = RequestBody.create(MediaType.parse("image/*"), byteArrayImage)
        val part = MultipartBody.Part.createFormData("image", "image", requestBody)

        ingur.uploadFile("Client-ID 241dc1dec32c2d8", part)
            .enqueue(object : Callback<UploadResponse> {
                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {
                    if (response.isSuccessful) {
                        SnackBar.showSnackBar(view, "success", "Upload realizado com sucesso!")
                        imageLink = response.body()?.data?.link.toString()
                    } else {
                        SnackBar.showSnackBar(view, "error", "Erro ao realizar upload :(")
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    t.message?.let { SnackBar.showSnackBar(view, "error", it) }
                }
            })

        Handler().postDelayed({
            val postData = Tatuagem(
                null,
                et_tattoo_title.text.toString(),
                et_tattoo_local.text.toString(),
                et_tattoo_description.text.toString(),
                imageLink,
                tattooArtistId,
                getStyleId()
            )

            tattoo.createTattoo(postData).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        SnackBar.showSnackBar(view, "success", "Tatuagem criada com sucesso!")
                        clearFields()
                    } else {
                        SnackBar.showSnackBar(view, "error", "Erro ao criar tatuagem :(")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.message?.let { SnackBar.showSnackBar(view, "error", it) }
                }
            })
        }, 6000)
    }

    fun populateTattooStylesSpinner() {
        val tattooStyles = retrofit.create(EstiloService::class.java)
        var styleStringList = arrayListOf<String>("Selecione um estilo");

        tattooStyles.getTattoosStyles().enqueue(object : Callback<Array<Estilo>> {
            override fun onResponse(call: Call<Array<Estilo>>, response: Response<Array<Estilo>>) {
                if (response.isSuccessful) {
                    stylesList = response.body()!!
                    stylesList?.forEach { styleStringList.add(it.titulo) }
                } else {
                    println("Erro ao buscar os estilos, tentando novamente...")
                    populateTattooStylesSpinner()
                }
            }

            override fun onFailure(call: Call<Array<Estilo>>, t: Throwable) {
                t.message?.let { println(it) }
            }
        })

        var stylesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, styleStringList);
        stylesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStyles.setAdapter(stylesAdapter)
    }

    fun isValidFields(view: View): Boolean {
        if (et_tattoo_title.text.toString().isEmpty()) {
            et_tattoo_title.error = "O campo não pode estar em branco!"
            return false

        } else if (et_tattoo_local.text.toString().isEmpty()) {
            et_tattoo_local.error = "O campo não pode estar em branco!"
            return false

        } else if (et_tattoo_description.text.toString().isEmpty()) {
            et_tattoo_description.error = "O campo não pode estar em branco!"
            return false

        } else if (spinnerStyles.selectedItem.toString() == "Selecione um estilo") {
            SnackBar.showSnackBar(view, "error", "Selecione um estilo!")
            return false

        } else if (selectedImageUri == null) {
            SnackBar.showSnackBar(view, "error", "Selecione uma imagem!")
            return false
        }

        return true
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

    fun getTattooArtistId(): Int? {
        val prefs = getSharedPreferences("storage", 0)
        val tattooArtistId = prefs?.getInt("id_tatuador", 0)

        return tattooArtistId
    }

    fun getStyleId(): Int? {
        val selectedStyle = spinnerStyles.selectedItem.toString()
        val position = stylesList?.indexOfFirst { it.titulo == selectedStyle }
        val styleId = position?.let { stylesList?.get(it)?.id_estilo }

        return styleId
    }

    fun clearFields() {
        et_tattoo_title.setText("")
        et_tattoo_local.setText("")
        et_tattoo_description.setText("")
    }
}