package com.example.guestify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.guestify.databinding.DashboardBinding
import com.example.guestify.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
    }

}
