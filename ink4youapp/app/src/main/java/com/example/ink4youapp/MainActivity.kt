package com.example.ink4youapp

import android.animation.ObjectAnimator
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
import com.example.ink4youapp.models.Tatuador
import com.example.ink4youapp.rest.Rest
import com.example.ink4youapp.services.TatuadorService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        val tattooArtist = retrofit.create(TatuadorService::class.java)

        tattooArtist.auth(email, password).enqueue(object: Callback<Tatuador> {
            override fun onResponse(call: Call<Tatuador>, response: Response<Tatuador>) {
                if (response.isSuccessful) {
                    val artistName = response.body()?.nome
                    print(response.body()?.nome)
                    Toast.makeText(baseContext, artistName, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(baseContext, "Deu ruim, mas nem tanto", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Tatuador>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

}