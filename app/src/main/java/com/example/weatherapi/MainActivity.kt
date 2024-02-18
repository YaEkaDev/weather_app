package com.example.weatherapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapi.fragments.MainFragment
import com.example.weatherapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.clPlaceHolder, MainFragment.newInstance())
            .commit()

    }

}