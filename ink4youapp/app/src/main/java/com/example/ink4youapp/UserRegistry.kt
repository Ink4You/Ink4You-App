package com.example.ink4youapp

import android.app.ProgressDialog.show
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.ink4youapp.models.Usuario
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.services.UsuarioService
import com.example.ink4youapp.utils.SnackBar
import com.vicmikhailau.maskededittext.MaskedEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import androidx.appcompat.app.AlertDialog
import com.akiniyalocts.imgurapiexample.model.UploadResponse
import com.example.ink4youapp.rest.RestIngur
import com.example.ink4youapp.services.IngurService
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream
import java.util.*
import okhttp3.RequestBody

class UserRegistry : AppCompatActivity() {
    private val inkApi = Rest.getInstance()
    private val ingurApi = RestIngur.getInstance()

    private lateinit var linear_personal_infos: LinearLayout
    private lateinit var linear_account_infos: LinearLayout
    private lateinit var IVPreviewImage: ImageView

    private lateinit var et_name: EditText
    private lateinit var et_cpf: MaskedEditText
    private lateinit var et_birth_date: MaskedEditText
    private lateinit var et_telephone: MaskedEditText
    private lateinit var et_zip_code: MaskedEditText

    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    private lateinit var et_confirm_password: EditText
    private lateinit var cb_term_of_use: CheckBox

    private lateinit var builder : AlertDialog.Builder

    private var SELECT_PICTURE: Int = 200
    private var byteArrayImage: ByteArray? = null
    private var base64Image: String? = null
    private var imageLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registry)

        IVPreviewImage = findViewById(R.id.iv_profile);

        linear_personal_infos = findViewById(R.id.linear_personal_infos)
        linear_account_infos = findViewById(R.id.linear_account_infos)

        et_name = findViewById(R.id.et_name)
        et_birth_date = findViewById(R.id.et_birth_date)
        et_cpf = findViewById(R.id.et_cpf)
        et_zip_code = findViewById(R.id.et_zip_code)
        et_telephone = findViewById(R.id.et_telephone)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_confirm_password = findViewById(R.id.et_confirm_password)
        cb_term_of_use = findViewById(R.id.cb_term_of_use)

        builder = AlertDialog.Builder(this)
    }

    fun goToSecondStep (view: View){
        if (!isValidPersonalInfos(et_name, et_cpf, et_birth_date, et_telephone, et_zip_code)) {
            return
        }

        var animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        linear_personal_infos.startAnimation(animation)
        linear_personal_infos.visibility = View.GONE

        Handler().postDelayed({
            animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            linear_account_infos.startAnimation(animation)
            linear_account_infos.visibility = View.VISIBLE
        }, 300)
    }

    fun backToFristStep(view: View) {
        var animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        linear_account_infos.startAnimation(animation)
        linear_account_infos.visibility = View.GONE

        Handler().postDelayed({
            animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            linear_personal_infos.startAnimation(animation)
            linear_personal_infos.visibility = View.VISIBLE
        }, 300)
    }

    fun backToLogin(view: View) {
        startActivity(Intent(baseContext, MainActivity::class.java))
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
    fun createUser(view: View) {
        if (!isValidAccountInfos(et_email, et_password, et_confirm_password, cb_term_of_use)) {
            return
        }

        val user = inkApi.create(UsuarioService::class.java)
        val ingur = ingurApi.create(IngurService::class.java)

        val requestBody = RequestBody.create(MediaType.parse("image/*"), byteArrayImage)
        val part = MultipartBody.Part.createFormData("image", "image", requestBody)

        ingur.uploadFile("Client-ID 241dc1dec32c2d8", part).enqueue(object : Callback<UploadResponse> {
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

        Handler().postDelayed({
            val postData = Usuario(
                null,
                et_name.text.toString(),
                et_birth_date.text.toString(),
                et_cpf.unMaskedText.toString(),
                et_zip_code.unMaskedText.toString(),
                et_telephone.unMaskedText.toString(),
                et_email.text.toString(),
                et_password.text.toString(),
                imageLink,
                null
            )

            user.createUser(postData).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        SnackBar.showSnackBar(view, "success", "Cadastro realizado com sucesso!")

                        Handler().postDelayed({
                            startActivity(Intent(baseContext, HomeActivity::class.java))
                        }, 600)

                    } else {
                        SnackBar.showSnackBar(view, "error", "Erro ao realizar cadastro :(")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.message?.let { SnackBar.showSnackBar(view, "error", it) }
                }
            })
        }, 6000)
    }

    fun isValidPersonalInfos(
        et_name: EditText,
        et_cpf: MaskedEditText,
        et_birth_date: MaskedEditText,
        et_telephone: MaskedEditText,
        et_zip_code: MaskedEditText
    ): Boolean {

        val et_array =
            arrayOf(et_name, et_cpf, et_birth_date, et_telephone, et_zip_code)

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

        if (et_cpf.text.toString().length < 14) {
            et_cpf.error = "Insira um CPF válido!"
            return false
        }

        if (et_birth_date.text.toString().length < 10) {
            et_birth_date.error = "Insira uma data válida!"
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

    fun isValidAccountInfos(
        et_email: EditText,
        et_password: EditText,
        et_confirm_password: EditText,
        cb_term_of_use: CheckBox
    ): Boolean {

        val et_array =
            arrayOf(et_email, et_password, et_confirm_password)

        et_array.forEach {
            if (it.text.toString().isEmpty()) {
                it.error = "O campo não pode estar em branco!"
                return false
            }
        }

        if (et_password.text.toString() != et_confirm_password.text.toString()) {
            et_confirm_password.error = "As senhas devem ser iguais"
            return false
        }

        if (!cb_term_of_use.isChecked) {
            cb_term_of_use.error = "Aceite os termos de uso antes de prosseguir!"
            return false
        }

        return true
    }

    fun showTermsOfUse(view: View) {
        builder.setTitle(getString(R.string.term_of_use_label))
            .setMessage(getString(R.string.terms_of_use))
            .setCancelable(true)
            .setPositiveButton("OK"){ dialogInterface, it ->
                dialogInterface.cancel()
            }
            .show()
    }
}