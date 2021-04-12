package com.example.kotlinrxmvvm.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DogsAPIService {

    private val baseURL = "https://raw.githubusercontent.com"
    private val api = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(DogsAPI::class.java)

    fun getDogs(): Single <List<DogBreed>>{
        return api.getDogs()
    }
}