package com.example.ink4youapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.ink4youapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUpTabBar();
    }

    private fun setUpTabBar() {

        binding.bottomNavBar.setItemSelected(R.id.nav_explore);

        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.nav_explore -> {
                    binding.tvTextMain.text = "Explorar"
                    binding.bottomNavBar.dismissBadge(R.id.nav_explore)
                }
                R.id.nav_artist -> binding.tvTextMain.text = "Tatuadores"
                R.id.nav_tattoo -> binding.tvTextMain.text = "Tatuagens"
                R.id.nav_profile -> {
                    binding.tvTextMain.text = "Perfil"
                    binding.bottomNavBar.showBadge(R.id.nav_explore)
                }
            }
        }
    }
}