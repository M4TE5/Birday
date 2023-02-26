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
import com.example.birday.databinding.FragmentEventListBinding
import com.example.birday.domain.Event
import com.example.birday.presentation.EventListAdapter
import com.example.birday.presentation.viewmodels.MainViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class EventListFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
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

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        fillBannerInfo()
        setupRecyclerView()
        viewModel.eventList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        setupClickListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillBannerInfo() {
        binding.banner.apply {
            viewModel.firstEvent.observe(viewLifecycleOwner) {
                tvHeader.text = "${it.firstName} ${it.lastName}"
                Log.d("MyLog", it.firstName)
                val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
                val dateStr =
                    "${it.getDayName()}, ${it.getNextCelebrationDate().format(dateFormatter)}."
                tvInfo.text = "$dateStr ${Math.abs(it.daysLeft())} days left"

                tvCount.text = "Years: ${it.getAge()}"
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
}