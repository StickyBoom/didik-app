package com.agartha.didik

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.agartha.didik.databinding.ActivityMainBinding

/**
 * Activity utama aplikasi yang berfungsi sebagai container (wadah) bagi semua Fragment.
 * Menggunakan arsitektur "Single Activity" di mana perpindahan layar dilakukan melalui Fragment.
 */
class MainActivity : AppCompatActivity() {
    
    // Inisialisasi View Binding untuk mengakses layout activity_main
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Menyiapkan layout menggunakan View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()

        // Memeriksa apakah aplikasi baru saja dijalankan (bukan karena rotasi layar)
        if (savedInstanceState == null) {
            // Menampilkan SplashFragment sebagai layar pertama saat aplikasi dibuka
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SplashFragment())
                .commit()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(DashboardFragment())
                    true
                }
                R.id.nav_search -> {
                    loadFragment(SearchFragment())
                    true
                }
                R.id.nav_history -> {
                    loadFragment(HistoryFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfilePageFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    /**
     * Menampilkan atau menyembunyikan Bottom Navigation Bar.
     */
    fun showBottomNav(visible: Boolean) {
        binding.bottomNavigation.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
