package com.example.birday.presentation.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.birday.R
import com.example.birday.data.Dependencies
import com.example.birday.databinding.ActivityMainBinding
import com.example.birday.presentation.fragments.EventItemFragment

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        Dependencies.init(this)

        val navHost = supportFragmentManager.findFragmentById(R.id.page_holder) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bNav, navController)

        setOnButtonAddClickListener()
    }

    private fun setOnButtonAddClickListener(){
        binding.bAdd.setOnClickListener {
            findNavController(R.id.page_holder).navigate(
                R.id.eventItemFragment2,
                bundleOf("screenMode" to EventItemFragment.MODE_ADD)
            )
            binding.bAdd.apply {
                isEnabled = false
                Handler().postDelayed({
                    this.isEnabled = true
                }, 2000)
            }
        }
    }

}
