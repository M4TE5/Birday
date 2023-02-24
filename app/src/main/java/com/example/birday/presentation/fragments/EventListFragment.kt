package com.example.birday.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R
import com.example.birday.databinding.BannerBinding
import com.example.birday.domain.Event
import com.example.birday.presentation.EventListAdapter
import com.example.birday.presentation.viewmodels.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import java.time.format.DateTimeFormatter


class EventListFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: EventListAdapter

    private lateinit var rvEventList: RecyclerView
    private lateinit var buttonHideBanner: CardView
    private lateinit var banner: View

    //TODO: Make banner view binding #1
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
        return layoutInflater.inflate(R.layout.fragment_event_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initViews(view)
        initBannerViews(banner)
        fillBannerInfo()
        setupRecyclerView()
        viewModel.eventList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        setupClickListeners()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillBannerInfo() {
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

    private fun setupRecyclerView() {
        adapter = EventListAdapter()
        rvEventList.adapter = adapter
        rvEventList.layoutManager = LinearLayoutManager(context)
    }

    private fun setupClickListeners() {
        adapter.onEventClickListener = {
            val fragment = EventInfoFragment.newInstanceShowEvent(it.id)
            launchFragment(fragment)
        }

        adapter.onCheckBoxChangeListener = { it, checked ->
            it.favorite = checked
        }

        buttonHideBanner.setOnClickListener {
            if (banner.visibility == View.VISIBLE) banner.visibility = View.GONE
            else banner.visibility = View.VISIBLE
        }
    }

    private fun initBannerViews(view: View){
        tvHeader = view.findViewById(R.id.tv_header)
        tvInfo = view.findViewById(R.id.tv_info)
        tvCount = view.findViewById(R.id.tv_count)
        etSearch = view.findViewById(R.id.et_search)
        icon = view.findViewById(R.id.icon)
    }
    private fun initViews(view: View) {
        rvEventList = view.findViewById(R.id.rv_event_list)
        buttonHideBanner = view.findViewById(R.id.button_hide_banner)
        banner = view.findViewById(R.id.banner)
    }

    private fun launchFragment(fragment: Fragment) {
        requireActivity().apply {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.place_holder, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        fun newInstance() = EventListFragment()
    }
}