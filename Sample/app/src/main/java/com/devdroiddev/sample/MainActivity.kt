package com.devdroiddev.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.devdroiddev.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        replace(MainFragment())
    }

    private fun replace(fragment: Fragment, name: String = "") {
        supportFragmentManager.commit {
            replace(R.id.main_container, fragment, name)
            if (name.isNotBlank()) addToBackStack(name)
        }
    }

}

