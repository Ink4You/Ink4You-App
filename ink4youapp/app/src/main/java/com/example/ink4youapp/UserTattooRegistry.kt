package com.example.ink4youapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class UserTattooRegistry : AppCompatActivity() {

    private lateinit var et_name: EditText
    private lateinit var et_cnpj: EditText
    private lateinit var et_birth_date: EditText
    private lateinit var et_telephone: EditText
    private lateinit var et_zip_code: EditText
    private lateinit var et_number_home: EditText

    private lateinit var linear_personal_date: LinearLayout
    private lateinit var linear_date_account: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_tattoo_registry)

        linear_personal_date = findViewById(R.id.linear_personal_date)
        linear_date_account = findViewById(R.id.linear_date_account)
    }

    fun goToSecondStep(view: View) {
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

    }


    /*fun validarCampos(et_name: EditText, et_cnpj: EditText, et_birth_date: EditText,
                      et_telephone: EditText, et_zip_code: EditText, et_number_home: EditText):Boolean{
        if (et_name.text.toString().isEmpty() && et_cnpj.text.toString().isEmpty()
            && et_birth_date.text.toString().isEmpty() && et_telephone.text.toString().isEmpty()
            && et_zip_code.text.toString().isEmpty() && et_number_home.text.toString().isEmpty()){
            et_name.error = "O campo não pode estar em branco!"
            return false
        }
        return true
    }*/
}