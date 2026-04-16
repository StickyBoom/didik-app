package com.agartha.didik

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agartha.didik.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cek apakah ini pertama kali aplikasi dibuka
        if (savedInstanceState == null) {
            // Langsung munculin SplashFragment sebagai tampilan pertama
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SplashFragment())
                .commit()
        }
    }
}