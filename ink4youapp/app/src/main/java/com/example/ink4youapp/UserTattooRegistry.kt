package com.example.ink4youapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.ink4youapp.models.Endereco
import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.rest.RestViaCep
import com.example.ink4youapp.services.EnderecoService
import com.example.ink4youapp.services.TatuadorService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserTattooRegistry : AppCompatActivity() {

    private lateinit var et_name: EditText
    private lateinit var et_username: EditText
    private lateinit var et_cnpj: EditText
    private lateinit var et_birth_date: EditText
    private lateinit var et_telephone: EditText
    private lateinit var et_zip_code: EditText
    private lateinit var et_number_home: EditText

    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    private lateinit var et_confirm_password: EditText
    private lateinit var sw_import_photos_instagram: Switch

    private lateinit var adress: String
    private lateinit var uf: String

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
                sw_import_photos_instagram
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
            null,
            et_cnpj.text.toString(),
            et_zip_code.text.toString(),
            adress,
            et_number_home.text.toString(),
            et_telephone.text.toString(),
            et_email.text.toString(),
            et_password.text.toString(),
            et_username.text.toString(),
            null,
            uf,
            null,
            null
        )

        tattooArtist.createTattooArtist(postData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(baseContext, "Cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(baseContext, response.code(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getAdressFromViaCep(zipCode: String) {
        // var status: Boolean
        val tattooArtistAdress = viaCepApi.create(EnderecoService::class.java)

        tattooArtistAdress.getAdressInfos(zipCode).enqueue(object : Callback<Endereco> {
            override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                if (response.isSuccessful) {
                    adress = response.body()?.logradouro.toString()
                    uf = response.body()?.uf.toString()

                    Toast.makeText(baseContext, "Endereço obtido com sucesso", Toast.LENGTH_LONG).show()
                    // status = true
                } else {
                    Toast.makeText(baseContext, "Erro ao obter endereço", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Endereco>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }
        })

        //return status
    }
}

fun isValidPersonalInfos(
    et_name: EditText,
    et_cnpj: EditText,
    et_birth_date: EditText,
    et_telephone: EditText,
    et_zip_code: EditText,
    et_number_home: EditText
): Boolean {

    if (et_name.text.toString().isEmpty()) {
        et_name.error = "O campo não pode estar em branco!"
        return false

    } else if (et_cnpj.text.toString().isEmpty()) {
        et_cnpj.error = "O campo não pode estar em branco!"
        return false

    } else if (et_birth_date.text.toString().isEmpty()) {
        // et_birth_date.error = "O campo não pode estar em branco!"
        // return false

    } else if (et_telephone.text.toString().isEmpty()) {
        et_telephone.error = "O campo não pode estar em branco!"
        return false

    } else if (et_zip_code.text.toString().isEmpty()) {
        et_zip_code.error = "O campo não pode estar em branco!"
        return false

    } else if (et_number_home.text.toString().isEmpty()) {
        et_number_home.error = "O campo não pode estar em branco!"
        return false
    }

    return true
}

fun isValidAccountInfos(
    et_email: EditText,
    et_password: EditText,
    et_confirm_password: EditText,
    et_username: EditText,
    sw_import_photos_instagram: Switch
): Boolean {

    if (et_email.text.toString().isEmpty()) {
        et_email.error = "O campo não pode estar em branco!"
        return false

    } else if (et_password.text.toString().isEmpty()) {
        et_password.error = "O campo não pode estar em branco!"
        return false

    } else if (et_confirm_password.text.toString().isEmpty()) {
        et_confirm_password.error = "O campo não pode estar em branco!"
        return false

    } else if (et_password.text.toString() != et_confirm_password.text.toString()) {
        et_confirm_password.error = "As senhas devem ser iguais"
        return false

    } else if (sw_import_photos_instagram.isChecked) {
        if (et_username.text.toString().isEmpty()) {
            et_username.error = "O campo não pode estar em branco!"
            return false
        }
    }

    return true
}