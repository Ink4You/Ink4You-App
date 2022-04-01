package com.example.ink4youapp

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    private lateinit var linear_login: LinearLayout
    private lateinit var linear_registry: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        linear_login = findViewById(R.id.linear_login)
        linear_registry = findViewById(R.id.linear_registry)
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

}