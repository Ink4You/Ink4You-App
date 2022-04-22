package com.example.ink4youapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.rest.RestViaCep
import com.example.ink4youapp.services.TatuadorService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.ink4youapp.utils.SnackBar
import com.vicmikhailau.maskededittext.MaskedEditText

class UserTattooRegistry : AppCompatActivity() {

    private lateinit var et_name: EditText
    private lateinit var et_username: EditText
    private lateinit var et_cnpj: MaskedEditText
    private lateinit var et_birth_date: MaskedEditText
    private lateinit var et_telephone: MaskedEditText
    private lateinit var et_zip_code: MaskedEditText
    private lateinit var et_number_home: EditText

    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    private lateinit var et_confirm_password: EditText
    private lateinit var sw_import_photos_instagram: Switch
    private lateinit var cb_term_of_use: CheckBox

//    private lateinit var adress: String
//    private lateinit var uf: String

    private lateinit var linear_personal_date: LinearLayout
    private lateinit var linear_date_account: LinearLayout

    private val inkApi = Rest.getInstance()
    private val viaCepApi = RestViaCep.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_tattoo_registry)

        linear_personal_date = findViewById(R.id.linear_personal_date)
        linear_date_account = findViewById(R.id.linear_date_account)

        et_name = findViewById(R.id.et_name)
        et_username = findViewById(R.id.et_username)
        et_birth_date = findViewById(R.id.et_birth_date)
        et_cnpj = findViewById(R.id.et_cnpj)
        et_zip_code = findViewById(R.id.et_zip_code)
        et_number_home = findViewById(R.id.et_number_home)
        et_telephone = findViewById(R.id.et_telephone)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_confirm_password = findViewById(R.id.et_confirm_password)
        sw_import_photos_instagram = findViewById(R.id.sw_import_photos_instagram)
        cb_term_of_use = findViewById(R.id.cb_term_of_use)
    }

    fun goToSecondStep(view: View) {
        if (!isValidPersonalInfos(
                et_name,
                et_cnpj,
                et_birth_date,
                et_telephone,
                et_zip_code,
                et_number_home
            )
        ) {
            return
        }

        var animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        linear_personal_date.startAnimation(animation)
        linear_personal_date.visibility = View.GONE

        Handler().postDelayed({
            animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            linear_date_account.startAnimation(animation)
            linear_date_account.visibility = View.VISIBLE
        }, 300)
    }

    fun backToFirstStep(view: View) {
        var animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        linear_date_account.startAnimation(animation)
        linear_date_account.visibility = View.GONE

        Handler().postDelayed({
            animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            linear_personal_date.startAnimation(animation)
            linear_personal_date.visibility = View.VISIBLE
        }, 300)
    }

    fun backToLogin(view: View) {
        startActivity(Intent(baseContext, MainActivity::class.java))
    }

    fun goToHome(view: View) {
        startActivity(Intent(baseContext, HomeActivity::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createUserTattoo(view: View) {
        if (!isValidAccountInfos(
                et_email,
                et_password,
                et_confirm_password,
                et_username,
                sw_import_photos_instagram,
                cb_term_of_use
            )
        ) {
            return
        }

        val tattooArtist = inkApi.create(TatuadorService::class.java)
        // val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
        // val parseBirthDate = LocalDate.parse(et_birth_date.text.toString(), pattern);
        // getAdressFromViaCep(et_zip_code.text.toString())

        val postData = Tatuador(
            null,
            et_name.text.toString(),
            et_username.text.toString(),
            et_birth_date.text.toString(),
            et_cnpj.unMaskedText.toString(),
            et_zip_code.unMaskedText.toString(),
            null,
            et_number_home.text.toString(),
            et_telephone.unMaskedText.toString(),
            et_email.text.toString(),
            et_password.text.toString(),
            et_username.text.toString(),
            null,
            null,
            null,
            null
        )

        tattooArtist.createTattooArtist(postData).enqueue(object : Callback<Void> {
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
    }

//    fun getAdressFromViaCep(zipCode: String) {
//        // var status: Boolean
//        val tattooArtistAdress = viaCepApi.create(EnderecoService::class.java)
//
//        tattooArtistAdress.getAdressInfos(zipCode).enqueue(object : Callback<Endereco> {
//            override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
//                if (response.isSuccessful) {
//                    adress = response.body()?.logradouro.toString()
//                    uf = response.body()?.uf.toString()
//
//                    Toast.makeText(baseContext, "Endereço obtido com sucesso", Toast.LENGTH_LONG)
//                        .show()
//                    // status = true
//                } else {
//                    Toast.makeText(baseContext, "Erro ao obter endereço", Toast.LENGTH_LONG).show()
//                }
//            }
//
//            override fun onFailure(call: Call<Endereco>, t: Throwable) {
//                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
//            }
//        })
//
//        //return status
//    }

    fun isValidPersonalInfos(
        et_name: EditText,
        et_cnpj: MaskedEditText,
        et_birth_date: MaskedEditText,
        et_telephone: MaskedEditText,
        et_zip_code: MaskedEditText,
        et_number_home: EditText
    ): Boolean {

        val et_array =
            arrayOf(et_name, et_cnpj, et_birth_date, et_telephone, et_zip_code, et_number_home)

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

        if (et_cnpj.text.toString().length < 14) {
            et_cnpj.error = "Insira um CNPJ válido!"
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

    fun isValidAccountInfos(
        et_email: EditText,
        et_password: EditText,
        et_confirm_password: EditText,
        et_username: EditText,
        sw_import_photos_instagram: Switch,
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

        } else if (sw_import_photos_instagram.isChecked) {
            if (et_username.text.toString().isEmpty()) {
                et_username.error = "O campo não pode estar em branco!"
                return false
            }

        } else if (!cb_term_of_use.isChecked) {
            cb_term_of_use.error = "Aceite os termos de uso antes de prosseguir!"
            return false
        }

        return true
    }

    fun showOrHideUsernameInput(view: View) {
        if (sw_import_photos_instagram.isChecked) {
            et_username.visibility = View.VISIBLE
        } else {
            et_username.visibility = View.GONE
        }
    }
}