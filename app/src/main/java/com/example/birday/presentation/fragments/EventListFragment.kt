package com.example.birday.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birday.R
import com.example.birday.presentation.EventListAdapter
import com.example.birday.presentation.activity.MainActivity
import com.example.birday.presentation.viewmodels.MainViewModel


class EventListFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: EventListAdapter

    private lateinit var rvEventList: RecyclerView

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
        launchBannerFragment(BannerFragment.newInstanceEventInfo())
        setupRecyclerView()
        viewModel.eventList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        setupEventClickListeners()
    }

    private fun setupRecyclerView(){
        adapter = EventListAdapter()
        rvEventList.adapter = adapter
        rvEventList.layoutManager = LinearLayoutManager(context)
    }

    private fun setupEventClickListeners(){
        adapter.onEventClickListener = {
            val fragment = EventInfoFragment.newInstanceShowEvent(it.id)
            launchFragment(fragment)
        }
        adapter.onCheckBoxChangeListener = { it, checked ->
            it.favorite = checked
        }
    }

    private fun initViews(view: View){
        rvEventList = view.findViewById(R.id.rv_event_list)
    }

    private fun launchFragment(fragment: Fragment){
        requireActivity().apply {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.place_holder, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
    private fun launchBannerFragment(fragment: Fragment){
        requireActivity().apply {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.banner_holder, fragment)
                .commit()
        }
    }
    companion object {
        fun newInstance() = EventListFragment()
    }
}