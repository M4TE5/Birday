package com.example.birday.presentation.activity

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.example.birday.R
import com.example.birday.presentation.fragments.EventItemFragment
import com.example.birday.presentation.fragments.EventListFragment
import com.example.birday.presentation.fragments.BannerFragment
import com.example.birday.presentation.fragments.FavoritesEventsFragment
import com.example.birday.presentation.viewmodels.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setOnBannerSwitchStateListener()
        setOnNavBarItemClickListeners()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    private fun setOnBannerSwitchStateListener(){
        val buttonHideBanner = findViewById<CardView>(R.id.button_hide_banner)
        val banner = findViewById<FragmentContainerView>(R.id.banner_holder)
        buttonHideBanner.setOnClickListener {
            if(banner.visibility == View.GONE) banner.visibility = View.VISIBLE
            else banner.visibility = View.GONE
        }
    }
    private fun setOnNavBarItemClickListeners() {
        val buttonAdd = findViewById<FloatingActionButton>(R.id.b_add)
        buttonAdd.setOnClickListener {
            launchAddFragment(EventItemFragment.newInstanceAddEvent())
        }

        val bNav = findViewById<BottomNavigationView>(R.id.bNav)
        bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.main_list -> {
                    launchPageFragment(EventListFragment.newInstance())
                }
                R.id.favorite_list -> {
                    launchPageFragment(FavoritesEventsFragment.newInstance())
                }
                R.id.settings -> {
                    saySoon()
                    //TODO: launch settings fragment
                }
            }
            true
        }
    }

    private fun saySoon(){
        Toast.makeText(this,"Скоро...",Toast.LENGTH_SHORT).show()
        val mp = MediaPlayer.create(this,R.raw.soon)
        mp.start()
    }

    private fun launchPageFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.page_holder, fragment)
            .commit()
    }

    private fun launchAddFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.place_holder, fragment)
            .addToBackStack(null)
            .commit()
    }
}
