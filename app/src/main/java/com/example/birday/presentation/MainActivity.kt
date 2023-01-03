package com.example.birday.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: EventListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupRecyclerView()
        viewModel.eventList.observe(this){
            adapter.eventList = it
        }
    }

    private fun setupRecyclerView(){
        val rvEventList = findViewById<RecyclerView>(R.id.rv_event_list)
        adapter = EventListAdapter()
        rvEventList.adapter = adapter
    }
}
