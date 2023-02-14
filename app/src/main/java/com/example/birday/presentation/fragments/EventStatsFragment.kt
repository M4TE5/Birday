package com.example.birday.presentation.fragments

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.example.birday.R
import com.example.birday.domain.Event
import com.example.birday.presentation.viewmodels.BannerViewModel
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text
import java.lang.Math.abs
import java.time.format.DateTimeFormatter

class EventStatsFragment : Fragment() {

    private lateinit var viewModel: BannerViewModel

    private var screenMode: String = MODE_UNKNOWN

    private lateinit var tvHeader: TextView
    private lateinit var tvInfo: TextView
    private lateinit var tvCount: TextView
    private lateinit var etSearch: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_event_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BannerViewModel::class.java]
        initViews(view)
        launchRightMode()
    }

    private fun parseParams() {
        val args: Bundle = requireArguments()

        if (!args.containsKey(MODE)) throw java.lang.RuntimeException("param mode is absent")
        val mode = args.getString(MODE)

        if (mode != MODE_EVENT_INFO && mode != MODE_LIST_STATS)
            throw java.lang.RuntimeException("unknown mode $mode")
        screenMode = mode
    }

    private fun initViews(view: View) {
        tvHeader = view.findViewById(R.id.tv_header)
        tvInfo = view.findViewById(R.id.tv_info)
        tvCount = view.findViewById(R.id.tv_count)
        etSearch = view.findViewById(R.id.et_search)
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EVENT_INFO -> launchEventInfoMode()
            MODE_LIST_STATS -> launchListStatsMode()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun launchEventInfoMode() {
        Log.d("MyLog", "launched")
        viewModel.firstEvent.observe(viewLifecycleOwner){
            tvHeader.text = "${it.firstName} ${it.lastName}"

            val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
            val dateStr =
                "${it.getDayName()}, ${it.getNextCelebrationDate().format(dateFormatter)}."
            tvInfo.text = "$dateStr ${abs(it.daysLeft())} days left"

            tvCount.text = "Years: ${it.getAge()}"
        }
    }

    private fun launchListStatsMode() {
        tvHeader.text = "Statistics"
        tvInfo.text = "Random staff" //TODO: Random statistic facts
        tvCount.text = "Events: ${viewModel.getListSize()}"
    }

    companion object {
        private const val MODE = "mode"
        private const val MODE_EVENT_INFO = "mode_event_info"
        private const val MODE_LIST_STATS = "mode_list_stats"
        private const val MODE_UNKNOWN = ""

        fun newInstanceEventInfo() =
            EventStatsFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_EVENT_INFO)
                }
            }

        fun newInstanceListStats(): EventStatsFragment =
            EventStatsFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_LIST_STATS)
                }
            }
    }
}