package com.example.ink4youapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ink4youapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding;
    private lateinit var fragmentManager: FragmentManager;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUpTabBar();

        initFragmentManager();

    }

    private fun setUpTabBar() {

        binding.bottomNavBar.setItemSelected(R.id.nav_explore);

        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.nav_explore -> {
//                    binding.bottomNavBar.dismissBadge(R.id.nav_explore)
                    alterFragment(ExploreFragment())
                }
                R.id.nav_artist -> {
                    alterFragment(ArtistsFragment())
                }
                R.id.nav_tattoo -> {
                    alterFragment(TattoosFragment())
                }
                R.id.nav_profile -> {
//                    binding.bottomNavBar.showBadge(R.id.nav_explore)
                    alterFragment(ProfileFragment())
                }
            }
        }
    }

    private fun initFragmentManager() {
        fragmentManager = getSupportFragmentManager();

        val initialFragment: Fragment = ExploreFragment();
        val ft: FragmentTransaction = fragmentManager.beginTransaction()

        ft.add(R.id.fragment_content, initialFragment)
        ft.commit()
    }

    private fun alterFragment(newFragment: Fragment) {
        val ft: FragmentTransaction = fragmentManager.beginTransaction()

        ft.replace(R.id.fragment_content, newFragment)
        ft.commit()
    }

    private fun removeFragment(){
        val fragment: Fragment? = fragmentManager.findFragmentById(R.id.fragment_content)
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        if (fragment != null) {
            ft.remove(fragment)
            ft.commit()
        }
    }
}