package com.example.guestify.api

import com.example.guestify.model.Quote
import retrofit2.Call
import retrofit2.http.GET

interface QuoteApiService {
    @GET("api/quotes")
    fun getQuotes(): Call<List<Quote>>
}
