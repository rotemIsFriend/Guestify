package com.example.guestify.model

data class Quote(
    val q: String,
    val a: String
) {
    fun formatQuote(): String {
        return "\"$q\" - $a"
    }
}
