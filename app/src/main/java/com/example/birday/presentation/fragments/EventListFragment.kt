package com.example.birday.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.birday.R
import com.example.birday.data.Dependencies
import com.example.birday.databinding.FragmentEventListBinding
import com.example.birday.domain.Event
import com.example.birday.presentation.EventListAdapter
import com.example.birday.presentation.viewmodels.EventListViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class EventListFragment : Fragment() {

    private val viewModel by lazy { EventListViewModel(Dependencies.eventListRepository) }
    private lateinit var adapter: EventListAdapter
    private lateinit var binding: FragmentEventListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_event_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventListBinding.bind(view)

        fillBannerInfo()
        setupRecyclerView()
        viewModel.eventList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                showNoFavoritesMessage(false)
            }
            else showNoFavoritesMessage(true)

            adapter.submitList(it)
        }
        setupClickListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillBannerInfo() {
        viewModel.eventList.observe(viewLifecycleOwner) { list ->
            binding.banner.apply {
                if (list.isNotEmpty()) {
                    val firstEvent = list.first()
                    tvHeader.text = "${firstEvent.firstName} ${firstEvent.lastName}"
                    val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
                    val dateStr = "${firstEvent.getNextCelebrationDate()
                            .dayOfWeek.toString()
                            .lowercase()
                            .replaceFirstChar { it.uppercase() }}, ${firstEvent
                                .getNextCelebrationDate()
                                .format(dateFormatter)
                            }."
                    tvInfo.text = "$dateStr ${Math.abs(firstEvent.daysLeft())} days left"

                    tvCount.text = "Years: ${firstEvent.getAge()}"
                } else {
                    tvHeader.text = "Next event"
                    tvInfo.text = "No events to show!"
                    tvCount.text = "You haven't entered an event yet. Add one and let Birday help you!"
                }
            }

        }
    }

    private fun setupRecyclerView() {
        adapter = EventListAdapter()
        binding.apply {
            rvEventList.adapter = adapter
            rvEventList.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupClickListeners() {
        adapter.onEventClickListener = {
            val direction = EventListFragmentDirections.actionEventListFragmentToEventInfoFragment(
                it.id
            )
            findNavController().navigate(direction)
        }

        adapter.onCheckBoxChangeListener = { it, checked ->
            it.favorite = checked
            viewModel.editEvent(it)
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
            AccelerateDecelerateInterpolator()
            duration = 300
        }
    }

    private fun showNoFavoritesMessage(show: Boolean){
        binding.apply {
            if (show){
                imEmoji.visibility = View.VISIBLE
                tvNoEventsMessage.visibility = View.VISIBLE
            }
            else {
                imEmoji.visibility = View.INVISIBLE
                tvNoEventsMessage.visibility = View.INVISIBLE
            }
        }
    }
}