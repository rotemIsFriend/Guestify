package com.example.guestify.util

inline fun <T> SafeCall(action: () -> Resource<T>): Resource<T> {
    return try{
        action()
    }catch(e : Exception){
        Resource.Error(e.message ?: "Unkown Error Occured")
    }
}