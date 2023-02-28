package com.example.birday.presentation.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.NavigationUI
import com.example.birday.R
import com.example.birday.databinding.ActivityMainBinding
import com.example.birday.presentation.fragments.EventInfoFragmentDirections
import com.example.birday.presentation.fragments.EventItemFragment
import com.example.birday.presentation.viewmodels.EventItemViewModel
import com.example.birday.presentation.viewmodels.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var eventItemViewModel: EventItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        eventItemViewModel = ViewModelProvider(this)[EventItemViewModel::class.java]

        val navHost = supportFragmentManager.findFragmentById(R.id.page_holder) as NavHostFragment
        val navController = navHost.navController //fragmentContainer (page_holder)
        NavigationUI.setupWithNavController(binding.bNav, navController) //связали bNav и page_holder

        binding.bAdd.setOnClickListener {
            findNavController(R.id.page_holder).navigate(
                R.id.eventItemFragment2,
                bundleOf("screenMode" to EventItemFragment.MODE_ADD)
            )
        }
    }

}
