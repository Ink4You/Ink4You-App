package com.example.ink4youapp

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ink4youapp.adapters.TatuagemSimpleDtoAdapter
import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.models.TatuagemDtoImageModel
import com.example.ink4youapp.models.Usuario
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.services.TatuadorService
import com.example.ink4youapp.services.UsuarioService
import com.example.ink4youapp.utils.SnackBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import kotlin.concurrent.timerTask
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var linear_login: LinearLayout
    private lateinit var linear_registry: LinearLayout

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private val retrofit = Rest.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        linear_login = findViewById(R.id.linear_login)
        linear_registry = findViewById(R.id.linear_registry)

        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
    }

    fun showRegistryOptions(view: View) {
        var animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        linear_login.startAnimation(animation)
        linear_login.visibility = View.GONE

        Handler().postDelayed({
            animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            linear_registry.startAnimation(animation)
            linear_registry.visibility = View.VISIBLE
        }, 300)
    }

    fun showLogin(view: View) {
        var animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        linear_registry.startAnimation(animation)
        linear_registry.visibility = View.GONE

        Handler().postDelayed({
            animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            linear_login.startAnimation(animation)
            linear_login.visibility = View.VISIBLE
        }, 300)
    }

    fun goToUserRegistry(view: View) {
        val userRegistry: Intent = Intent(baseContext, UserRegistry::class.java)
        startActivity(userRegistry)
    }

    fun goToTattooRegistry(view: View) {
        val tattooRegistry: Intent = Intent(baseContext, UserTattooRegistry::class.java)
        startActivity(tattooRegistry)
    }

    fun goToHome(view: View) {
        val homeActivity: Intent = Intent(baseContext, HomeActivity::class.java)
        startActivity(homeActivity)
    }

    fun authUser(view: View) {
        if (!isValidFields(etEmail, etPassword)) {
            return
        }

        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        val tattooUser = retrofit.create(UsuarioService::class.java)

        tattooUser.auth(email, password).enqueue(object: Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val prefs = getSharedPreferences("storage", 0)
                    val editor = prefs.edit()

                    editor.putString("userType", "sampleUser")
                    body?.id_usuario?.let { editor.putInt("id_usuario", it) }
                    editor.putString("nome", body?.nome)
                    editor.putString("cnpj", body?.cpf)
                    editor.putString("data_nascimento", body?.data_nascimento)
                    editor.putString("telefone", body?.telefone)
                    editor.putString("cep", body?.cep)
                    editor.putString("email", body?.email)
                    editor.putString("senha", body?.senha)

                    goToHome(view)
                } else {
                   authTattooUser(view)
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                t.message?.let { SnackBar.showSnackBar(view, "error", it) }
            }
        })
    }

    fun authTattooUser(view: View) {
        if (!isValidFields(etEmail, etPassword)) {
            return
        }

        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        val tattooArtist = retrofit.create(TatuadorService::class.java)

        tattooArtist.auth(email, password).enqueue(object: Callback<Tatuador> {
            override fun onResponse(call: Call<Tatuador>, response: Response<Tatuador>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val prefs = getSharedPreferences("storage", 0)
                    val editor = prefs.edit()

                    editor.putString("user_type", "tattooArtist")
                    body?.id_tatuador?.let { editor.putInt("id_tatuador", it) }
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
                    goToHome(view)
                } else {
                    SnackBar.showSnackBar(view, "error", "Email ou senha inválidos!")
                }
            }

            override fun onFailure(call: Call<Tatuador>, t: Throwable) {
                t.message?.let { SnackBar.showSnackBar(view, "error", it) }
            }
        })
    }

    fun isValidFields(etEmail: EditText, etPassword: EditText): Boolean {
        if (etEmail.text.toString().isEmpty()) {
            etEmail.error = "O campo não pode estar em branco!"
            return false

        } else if (etPassword.text.toString().isEmpty()) {
            etPassword.error = "O campo não pode estar em branco!"
            return false
        }

        return true
    }
}