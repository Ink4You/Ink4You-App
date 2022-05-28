package com.example.ink4youapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import com.vicmikhailau.maskededittext.MaskedEditText
import java.io.ByteArrayOutputStream

class EditUserProfile : AppCompatActivity() {
    private lateinit var et_name: EditText
    private lateinit var et_cpf: MaskedEditText
    private lateinit var et_birth_date: MaskedEditText
    private lateinit var et_telephone: MaskedEditText
    private lateinit var et_zip_code: MaskedEditText
    private lateinit var et_email: EditText
    private lateinit var et_password: EditText

    private lateinit var IVPreviewImage: ImageView

    private var SELECT_PICTURE: Int = 200
    private var byteArrayImage: ByteArray? = null
    private var base64Image: String? = null
    private var imageLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)

        IVPreviewImage = findViewById(R.id.iv_profile)

        et_name = findViewById(R.id.et_name)
        et_birth_date = findViewById(R.id.et_birth_date)
        et_cpf = findViewById(R.id.et_cpf)
        et_zip_code = findViewById(R.id.et_zip_code)
        et_telephone = findViewById(R.id.et_telephone)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)

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

}