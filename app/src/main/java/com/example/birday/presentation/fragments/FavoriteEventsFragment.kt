package com.example.birday.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.birday.R
import com.example.birday.data.Dependencies
import com.example.birday.databinding.FragmentFavoriteEventsBinding
import com.example.birday.presentation.FavoriteEventListAdapter
import com.example.birday.presentation.viewmodels.EventListViewModel

@RequiresApi(Build.VERSION_CODES.O)
class FavoriteEventsFragment : Fragment() {

    private val viewModel by lazy { EventListViewModel(Dependencies.eventListRepository) }
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
        setupRecyclerView()
        fillBannerInfo()
        viewModel.eventList.observe(viewLifecycleOwner) {
            val list = it.filter { item -> item.favorite }
            if (list.isNotEmpty()) {
                adapter.submitList(it.filter { item -> item.favorite })
                showNoFavoritesMessage(false)
            }
            else showNoFavoritesMessage(true)
        }

        binding.buttonHideBanner.setOnClickListener {
            if (binding.banner.bannerLayout.visibility == View.VISIBLE) hideBanner()
            else showBanner()
        }

        adapter.onEventClickListener = {
            val direction = FavoriteEventsFragmentDirections.actionFavoriteEventsFragmentToNotesFragment(it.id)
            findNavController().navigate(direction)
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
        viewModel.eventList.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                tvInfo.visibility = View.VISIBLE
                tvInfo.text = "Random staff" //TODO: Random statistic facts
                tvCount.text = "Events: ${it.size}"
            }
            else{
                tvInfo.text = "No stats to show!"
                tvCount.text = "You don't have enough events to generate stats. Add some and come back later!"
            }
        }
    }

    private fun showNoFavoritesMessage(show: Boolean){
        binding.apply {
            if (show){
                imEmoji.visibility = View.VISIBLE
                tvNoFavoritesMessage.visibility = View.VISIBLE
            }
            else {
                imEmoji.visibility = View.INVISIBLE
                tvNoFavoritesMessage.visibility = View.INVISIBLE
            }
        }
    }
}