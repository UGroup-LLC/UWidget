package com.kikoproject.uwidget.networking

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Server(address: String) {
    private val gsonBuild = GsonBuilder()
        .setLenient().create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(address)
        .addConverterFactory(GsonConverterFactory.create(gsonBuild))
        .build()

    val api: API = retrofit.create(API::class.java)
}