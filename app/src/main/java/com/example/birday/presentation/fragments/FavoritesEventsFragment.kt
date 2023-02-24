package com.example.birday.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R
import com.example.birday.presentation.FavoriteEventListAdapter
import com.example.birday.presentation.viewmodels.MainViewModel
import com.google.android.material.textfield.TextInputEditText


class FavoritesEventsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: FavoriteEventListAdapter
    private lateinit var rvFavoriteEventList: RecyclerView
    private lateinit var banner: View
    private lateinit var buttonHideBanner: CardView

    //TODO: Make banner view binding #2
    private lateinit var tvHeader: TextView
    private lateinit var tvInfo: TextView
    private lateinit var tvCount: TextView
    private lateinit var etSearch: TextInputEditText
    private lateinit var icon: ImageView

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
        initBannerViews(banner)
        fillBannerInfo()
        setupRecyclerView()
        viewModel.eventList.observe(viewLifecycleOwner) {
            adapter.submitList(it.filter { item -> item.favorite })
        }
        buttonHideBanner.setOnClickListener {
            if (banner.visibility == View.VISIBLE) banner.visibility = View.GONE
            else banner.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        adapter = FavoriteEventListAdapter()
        rvFavoriteEventList.adapter = adapter
        rvFavoriteEventList.layoutManager = LinearLayoutManager(context)
    }

    private fun initViews(view: View) {
        rvFavoriteEventList = view.findViewById(R.id.rv_favorite_event_list)
        banner = view.findViewById(R.id.banner)
        buttonHideBanner = view.findViewById(R.id.button_hide_banner)
    }
    private fun initBannerViews(view: View){
        tvHeader = view.findViewById(R.id.tv_header)
        tvInfo = view.findViewById(R.id.tv_info)
        tvCount = view.findViewById(R.id.tv_count)
        etSearch = view.findViewById(R.id.et_search)
        icon = view.findViewById(R.id.icon)
    }

    private fun fillBannerInfo(){
        icon.setImageResource(R.drawable.candle)
        tvHeader.text = "Statistics"
        tvInfo.text = "Random staff" //TODO: Random statistic facts
        tvCount.text = "Events: ${viewModel.getListSize()}"
    }
    companion object {
        fun newInstance() = FavoritesEventsFragment()
    }
}