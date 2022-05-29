package com.example.ink4youapp

import android.content.Context
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
import com.example.ink4youapp.adapters.TatuagemSimpleEditDtoAdapter
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
    private var tattooList = ArrayList<Tatuagem>()
    private lateinit var rvTatuagens: RecyclerView
    private lateinit var adapter: TatuagemSimpleEditDtoAdapter

    private var tattooArtistId: Int? = null

    private lateinit var et_tattoo_id: EditText
    private lateinit var et_tattoo_image: EditText
    private lateinit var et_tattoo_title: EditText
    private lateinit var et_tattoo_local: EditText
    private lateinit var et_tattoo_description: EditText

    private lateinit var stylesList: Array<Estilo>
    private lateinit var spinnerStyles: Spinner

    private var skipImageValidation: Boolean = false
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
        et_tattoo_id = findViewById(R.id.et_tattoo_id)
        et_tattoo_image = findViewById(R.id.et_tattoo_image)
        et_tattoo_title = findViewById(R.id.et_tattoo_title)
        et_tattoo_local = findViewById(R.id.et_tattoo_local)
        et_tattoo_description = findViewById(R.id.et_tattoo_description)
        spinnerStyles = findViewById(R.id.spinner_styles)

        rvTatuagens = findViewById(R.id.tattoosRecyclerView)
        rvTatuagens.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = TatuagemSimpleEditDtoAdapter(this, tattooList)

        populateTattooStylesSpinner()
        getTattoos()
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

                    et_tattoo_image.setText("")
                    skipImageValidation = true
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTattoo(view: View) {
        if (et_tattoo_image.text.toString() != "") { skipImageValidation = true }
        if (!isValidFields(view)) { return }

        val tattoo = retrofit.create(TatuagemService::class.java)
        if (et_tattoo_image.text.toString() == "") {
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

        } else {
            imageLink = et_tattoo_image.text.toString()
        }

        Handler().postDelayed({
            val tattooId = et_tattoo_id.text.toString().toInt()
            val postData = Tatuagem(
                null,
                et_tattoo_title.text.toString(),
                et_tattoo_local.text.toString(),
                et_tattoo_description.text.toString(),
                imageLink,
                tattooArtistId,
                getStyleId()
            )

            tattoo.updateTattoo(tattooId, postData).enqueue(object : Callback<Tatuagem> {
                override fun onResponse(call: Call<Tatuagem>, response: Response<Tatuagem>) {
                    if (response.isSuccessful) {
                        SnackBar.showSnackBar(view, "success", "Tatuagem atualizada com sucesso!")
                        clearFields()
                        getTattoos()
                    } else {
                        SnackBar.showSnackBar(view, "error", "Erro ao atualizar tatuagem :(")
                    }
                }

                override fun onFailure(call: Call<Tatuagem>, t: Throwable) {
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

        var stylesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, styleStringList);
        stylesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStyles.setAdapter(stylesAdapter)
    }

    fun isValidFields(view: View): Boolean {
        println(skipImageValidation)

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

        } else if (!skipImageValidation) {
            SnackBar.showSnackBar(view, "error", "Selecione uma imagem!")
            return false

        }

        return true
    }

    private fun getTattoos() {
        val tattoo = retrofit.create(TatuagemService::class.java)

        tattoo.getTattoosByTattooArtist(tattooArtistId!!).enqueue(object : Callback<List<Tatuagem>> {
            override fun onResponse(
                call: Call<List<Tatuagem>>,
                response: Response<List<Tatuagem>>
            ) {
                if (response.isSuccessful) {
                    tattooList.clear()
                    response.body()?.forEach { tattooList.add(it) }
                    rvTatuagens.adapter = adapter
                } else {
                    println("erro leve")
                }
            }

            override fun onFailure(call: Call<List<Tatuagem>>, t: Throwable) {
                println("erro")
            }

        })

//        tattooList.add(Tatuagem(1, "teste", "braco", "sla", "www", 1, 1))
//        tattooList.add(Tatuagem(1, "teste", "braco", "sla", "www", 1, 1))
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
        et_tattoo_id.setText("")
        et_tattoo_image.setText("")
        et_tattoo_title.setText("")
        et_tattoo_local.setText("")
        et_tattoo_description.setText("")
    }
}