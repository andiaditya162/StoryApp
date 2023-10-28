package com.dityapra.mystoryapp.data.remote.retrofit


import com.dityapra.mystoryapp.data.remote.response.AllStoriesResponse
import com.dityapra.mystoryapp.data.remote.response.ApiResponse
import com.dityapra.mystoryapp.data.remote.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pass: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") pass: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStories(
        @Header("Authorization") token: String,
        @Part("description") des: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ApiResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<AllStoriesResponse>
}