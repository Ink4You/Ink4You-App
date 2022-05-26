package com.example.ink4youapp.services

import com.akiniyalocts.imgurapiexample.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IngurService {
    @Multipart
    @POST("/3/upload/")
    fun uploadFile(
        @Header("Authorization") clientId: String,
        @Part image: MultipartBody.Part?,
        @Part("name") name: RequestBody? = null
    ): Call<UploadResponse>
}