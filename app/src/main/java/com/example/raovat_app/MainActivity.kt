package com.example.raovat_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.raovat_app.databinding.ActivityMainBinding
import com.example.raovat_app.fragments.AccountFragment
import com.example.raovat_app.fragments.CartFragment
import com.example.raovat_app.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navCart -> {
                    replaceFragment(CartFragment())
                    true
                }
                else -> {
                    replaceFragment(AccountFragment())
                    true
                }
            }
        }
    }
    @SuppressLint("CommitTransaction")
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_Frame, fragment)
            .commit()
    }
}