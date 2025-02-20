package com.example.guestify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guestify.api.RetrofitInstance
import com.example.guestify.model.Quote
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuoteViewModel : ViewModel() {
    private val _quote = MutableLiveData<String>()
    val quote: LiveData<String> get() = _quote

    private val defaultQuote = "\"Love is composed of a single soul inhabiting two bodies.\" - Aristotle"

    fun fetchLoveQuote() {
        RetrofitInstance.api.getQuotes().enqueue(object : Callback<List<Quote>> {
            override fun onResponse(call: Call<List<Quote>>, response: Response<List<Quote>>) {
                if (response.isSuccessful && response.body() != null) {
                    val loveQuotes = response.body()!!.filter { it.q.contains("love", ignoreCase = true) }
                    _quote.value = loveQuotes.firstOrNull()?.formatQuote() ?: defaultQuote
                } else {
                    _quote.value = defaultQuote
                }
            }

            override fun onFailure(call: Call<List<Quote>>, t: Throwable) {
                _quote.value = defaultQuote
            }
        })
    }
}
