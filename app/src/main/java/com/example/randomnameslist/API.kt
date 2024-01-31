package com.example.randomnameslist

import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("api")
    fun getUsers(): Call<List<User>>
}