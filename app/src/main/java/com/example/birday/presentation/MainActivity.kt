package com.example.birday.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnNavBarItemClickListeners()
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
                    Toast.makeText(this,"Скоро...",Toast.LENGTH_SHORT).show()
                    //TODO: launch settings fragment
                }
            }
            true
        }
    }

    private fun launchPageFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.place_holder, fragment)
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
