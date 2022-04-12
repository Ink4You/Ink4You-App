package com.example.ink4youapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat.startActivityForResult

class UserRegistry : AppCompatActivity() {

    private lateinit var linear_personal_infos: LinearLayout
    private lateinit var linear_account_infos: LinearLayout
    private lateinit var IVPreviewImage: ImageView
    var SELECT_PICTURE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registry)

        linear_personal_infos = findViewById(R.id.linear_personal_infos)
        linear_account_infos = findViewById(R.id.linear_account_infos)

        IVPreviewImage = findViewById(R.id.iv_profile);
    }

    fun goToSecondStep (view: View){
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
        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(selectedImageUri)
                }
            }
        }
    }
}