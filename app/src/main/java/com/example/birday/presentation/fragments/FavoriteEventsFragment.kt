package com.example.birday.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.birday.R
import com.example.birday.databinding.FragmentFavoriteEventsBinding
import com.example.birday.presentation.FavoriteEventListAdapter
import com.example.birday.presentation.viewmodels.MainViewModel

class FavoriteEventsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: FavoriteEventListAdapter

    private lateinit var binding: FragmentFavoriteEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_favorite_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteEventsBinding.bind(view)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        fillBannerInfo()
        setupRecyclerView()
        viewModel.eventList.observe(viewLifecycleOwner) {
            adapter.submitList(it.filter { item -> item.favorite })
        }
        binding.buttonHideBanner.setOnClickListener {
            if (binding.banner.bannerLayout.visibility == View.VISIBLE) hideBanner()
            else showBanner()
        }
    }

    private fun hideBanner() = with(binding) {
        banner.bannerLayout.animate().apply {
            scaleX(0.7f)
            scaleY(0f)
            translationY(-banner.bannerLayout.height.toFloat())
            AccelerateDecelerateInterpolator()
            duration = 300
        }
        banner.bannerLayout.visibility = View.GONE

        buttonHideBanner.animate().apply {
            rotation(180f)
            scaleX(1.1f)
            scaleY(1.1f)
            AccelerateDecelerateInterpolator()
            duration = 300
        }
    }

    private fun showBanner() = with(binding) {
        banner.bannerLayout.animate().apply {
            scaleX(1f)
            scaleY(1f)
            translationY(0f)
            AccelerateDecelerateInterpolator()
            duration = 300
        }
        banner.bannerLayout.visibility = View.VISIBLE

        buttonHideBanner.animate().apply {
            rotation(0f)
            scaleX(1f)
            scaleY(1f)
            AccelerateDecelerateInterpolator()
            duration = 300
        }
    }

    private fun setupRecyclerView() = with(binding) {
        adapter = FavoriteEventListAdapter()
        rvFavoriteEventList.adapter = adapter
        rvFavoriteEventList.layoutManager = LinearLayoutManager(context)
    }

    private fun fillBannerInfo() = with(binding.banner) {
        icon.setImageResource(R.drawable.candle)
        tvHeader.text = "Statistics"
        tvInfo.text = "Random staff" //TODO: Random statistic facts
        tvCount.text = "Events: ${viewModel.getListSize()}"
    }
}