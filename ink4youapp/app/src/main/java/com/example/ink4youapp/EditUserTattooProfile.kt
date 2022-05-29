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
import com.bumptech.glide.Glide
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import androidx.annotation.RequiresApi
import com.akiniyalocts.imgurapiexample.model.UploadResponse
import com.example.ink4youapp.models.Endereco
import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.rest.RestIngur
import com.example.ink4youapp.rest.RestViaCep
import com.example.ink4youapp.services.EnderecoService
import com.example.ink4youapp.services.IngurService
import com.example.ink4youapp.services.TatuadorService
import com.example.ink4youapp.utils.SnackBar
import com.vicmikhailau.maskededittext.MaskedEditText
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class EditUserTattooProfile : AppCompatActivity() {
    private val inkApi = Rest.getInstance()
    private val ingurApi = RestIngur.getInstance()
    private val viaCepApi = RestViaCep.getInstance()

    private lateinit var et_name: EditText
    private lateinit var et_about: EditText
    private lateinit var et_username: EditText
    private lateinit var et_cnpj: MaskedEditText
    private lateinit var et_birth_date: MaskedEditText
    private lateinit var et_telephone: MaskedEditText
    private lateinit var et_zip_code: MaskedEditText
    private lateinit var et_number_home: EditText

    private var adress: String? = null
    private var uf: String? = null

    private var tattooArtistId: Int? = null
    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    private lateinit var sw_import_photos_instagram: Switch

    private lateinit var linear_personal_date: LinearLayout
    private lateinit var linear_date_account: LinearLayout

    private lateinit var IVPreviewImage: ImageView
    private var SELECT_PICTURE: Int = 200
    private var byteArrayImage: ByteArray? = null
    private var base64Image: String? = null
    private var imageLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_tattoo_profile)

        IVPreviewImage = findViewById(R.id.iv_profile)

        et_name = findViewById(R.id.et_name)
        et_about = findViewById(R.id.et_about)
        et_username = findViewById(R.id.et_username)
        et_birth_date = findViewById(R.id.et_birth_date)
        et_cnpj = findViewById(R.id.et_cnpj)
        et_zip_code = findViewById(R.id.et_zip_code)
        et_number_home = findViewById(R.id.et_number_home)
        et_telephone = findViewById(R.id.et_telephone)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        sw_import_photos_instagram = findViewById(R.id.sw_import_photos_instagram)

        setTattooArtistInfos()
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
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    IVPreviewImage.setImageURI(selectedImageUri)
                    val imageStream = contentResolver.openInputStream(selectedImageUri)
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
    fun updateUserTattoo(view: View) {
        if (!isValidFields(view)) {
            return
        }

        val tattooArtist = inkApi.create(TatuadorService::class.java)
        val ingur = ingurApi.create(IngurService::class.java)

        getAdressFromViaCep(et_zip_code.text.toString(), view)

        if (byteArrayImage != null) {

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
                            imageLink = response.body()?.data?.link

                        } else {
                            SnackBar.showSnackBar(view, "error", "Erro ao realizar upload :(")
                        }
                    }

                    override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                        t.message?.let { SnackBar.showSnackBar(view, "error", it) }
                    }
                })
        }


        Handler().postDelayed({
            val postData = Tatuador(
                null,
                et_name.text.toString(),
                et_username.text.toString(),
                et_birth_date.text.toString(),
                et_cnpj.unMaskedText.toString(),
                et_zip_code.unMaskedText.toString(),
                adress,
                et_number_home.text.toString(),
                et_telephone.unMaskedText.toString(),
                et_email.text.toString(),
                et_password.text.toString(),
                et_username.text.toString(),
                imageLink,
                uf,
                null,
                et_about.text.toString()
            )

            tattooArtist.updateTattooArtist(postData, tattooArtistId).enqueue(object : Callback<Tatuador> {
                override fun onResponse(call: Call<Tatuador>, response: Response<Tatuador>) {
                    if (response.isSuccessful) {
                        SnackBar.showSnackBar(view, "success", "Perfil atualizado com sucesso!")
                        updateInfosInStorage(response)

                        Handler().postDelayed({
                            finish()
                        }, 2000)
                    } else {
                        SnackBar.showSnackBar(view, "error", "Erro ao atualizar perfil :(")
                    }
                }

                override fun onFailure(call: Call<Tatuador>, t: Throwable) {
                    t.message?.let { SnackBar.showSnackBar(view, "error", it) }
                }
            })
        }, 5000)
    }

    fun updateInfosInStorage(response: Response<Tatuador>){
        val body = response.body()
        val prefs = getSharedPreferences("storage", 0)
        val editor = prefs.edit()

        body?.id_tatuador?.let { editor.putInt("id_tatuador", it) }
        editor.putString("foto_perfil", body?.foto_perfil)
        editor.putString("nome", body?.nome)
        editor.putString("sobre", body?.sobre)
        editor.putString("cnpj", body?.cnpj)
        editor.putString("data_nascimento", body?.data_nascimento)
        editor.putString("telefone", body?.telefone)
        editor.putString("cep", body?.cep)
        editor.putString("numero_logradouro", body?.numero_logradouro)
        editor.putString("email", body?.email)
        editor.putString("senha", body?.senha)
        editor.putString("username_insta", body?.conta_instagram)
        editor.putString("sobre", body?.sobre)

        editor.apply()
    }

    fun getAdressFromViaCep(zipCode: String, view: View) {
        val tattooArtistAdress = viaCepApi.create(EnderecoService::class.java)

        tattooArtistAdress.getAdressInfos(zipCode).enqueue(object : Callback<Endereco> {
            override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                if (response.isSuccessful) {
                    adress = response.body()?.logradouro.toString()
                    uf = response.body()?.uf.toString()
                }
            }

            override fun onFailure(call: Call<Endereco>, t: Throwable) {
                t.message?.let { error(it) }
            }
        })
    }

    fun isValidFields(view: View): Boolean {
        val et_array =
            arrayOf(et_name, et_cnpj, et_birth_date, et_telephone, et_zip_code, et_number_home, et_email, et_password)

        et_array.forEach {
            if (it.text.toString().isEmpty()) {
                it.error = "O campo não pode estar em branco!"
                return false
            }
        }

        if (et_name.text.toString().length <= 3) {
            et_name.error = "Insira um nome válido!"
            return false
        }

        if (et_birth_date.text.toString().length < 10) {
            et_birth_date.error = "Insira uma data válida!"
            return false
        }

        if (et_cnpj.text.toString().length < 14) {
            et_cnpj.error = "Insira um CNPJ válido!"
            return false
        }

        if (et_telephone.text.toString().length < 15) {
            et_telephone.error = "Insira um telefone válido!"
            return false
        }

        if (et_zip_code.text.toString().length < 9) {
            et_zip_code.error = "Insira um CEP válido!"
            return false
        }

        return true
    }


    fun setTattooArtistInfos() {
        val prefs = getSharedPreferences("storage", 0)
        tattooArtistId = prefs.getInt("id_tatuador", 0)

        prefs?.getString("foto_perfil", "")?.let {
            imageLink = it
            Glide.with(IVPreviewImage.context)
                .load(it)
                .centerCrop()
                .into(IVPreviewImage)
        }
        prefs?.getString("nome", "")?.let { et_name.setText(it) }
        prefs?.getString("sobre", "")?.let { et_about.setText(it) }
        prefs?.getString("cnpj", "")?.let { et_cnpj.setText(it) }
        prefs?.getString("data_nascimento", "")?.let { et_birth_date.setText(it) }
        prefs?.getString("telefone", "")?.let { et_telephone.setText(it) }
        prefs?.getString("cep", "")?.let { et_zip_code.setText(it) }
        prefs?.getString("numero_logradouro", "")?.let { et_number_home.setText(it) }
        prefs?.getString("email", "")?.let { et_email.setText(it) }
        prefs?.getString("senha", "")?.let { et_password.setText(it) }
        prefs?.getString("username_insta", "")?.let {
            if (it != "") {
                sw_import_photos_instagram.isChecked = true
                et_username.visibility = View.VISIBLE
                et_username.setText(it)

            } else {
                sw_import_photos_instagram.isChecked = false
                et_username.visibility = View.GONE

            }
        }
    }

    fun showOrHideUsernameInput(view: View) {
        if (sw_import_photos_instagram.isChecked) {
            et_username.visibility = View.VISIBLE
        } else {
            et_username.visibility = View.GONE
            et_username.text = null
        }
    }

}