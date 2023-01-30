package com.example.birday.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R


class FavoritesEventsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: FavoriteEventListAdapter

    private lateinit var rvFavoriteEventList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_favorites_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initViews(view)
        setupRecyclerView()

        viewModel.eventList.observe(viewLifecycleOwner){
            adapter.submitList(it.filter { item -> item.favorite })
        }
    }

    private fun setupRecyclerView(){
        adapter = FavoriteEventListAdapter()
        rvFavoriteEventList.adapter = adapter
        rvFavoriteEventList.layoutManager = LinearLayoutManager(context)
    }

    private fun initViews(view: View){
        rvFavoriteEventList = view.findViewById(R.id.rv_favorite_event_list)
    }

    companion object {
        fun newInstance() = FavoritesEventsFragment()
    }
}