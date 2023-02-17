package com.example.birday.presentation.fragments

import android.icu.util.ChineseCalendar
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.birday.R
import com.example.birday.domain.Event
import com.example.birday.presentation.activity.MainActivity
import com.example.birday.presentation.viewmodels.EventInfoViewModel
import java.lang.Math.abs
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class EventInfoFragment : Fragment() {

    private lateinit var viewModel: EventInfoViewModel

    private lateinit var tvName: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvNextAge: TextView
    private lateinit var tvDaysLeft: TextView
    private lateinit var tvZodiacSign: TextView
    private lateinit var tvChineseSign: TextView

    private lateinit var buttonEdit: Button
    private lateinit var buttonDelete: Button
    private lateinit var buttonAddNotes: Button

    private var eventId: Int = Event.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_event_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[EventInfoViewModel::class.java]
        initViews(view)
        fillTextInfo()
        setClickListeners()
    }

    private fun fillTextInfo() {
        viewModel.getEventById(eventId)
        val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
        viewModel.event.observe(viewLifecycleOwner) {
            tvName.text = "Details - " + it.firstName
            tvNextAge.text = (it.getAge() + 1).toString()
            tvDaysLeft.text = "${abs(it.daysLeft())} days left"

            val dayName = it.getDayName()
            val date = it.date.format(dateFormatter)
            tvDate.text = "$dayName, $date"

            tvZodiacSign.text = it.getZodiacSign()
            tvChineseSign.text = it.getChineseSign()
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(EVENT_ID)) {
            throw RuntimeException("Param event id is absent")
        }
        eventId = args.getInt(EVENT_ID, Event.UNDEFINED_ID)
    }

    private fun initViews(view: View) {
        tvName = view.findViewById(R.id.tv_name)
        tvDate = view.findViewById(R.id.tv_date)
        tvNextAge = view.findViewById(R.id.tv_next_age)
        tvDaysLeft = view.findViewById(R.id.tv_days_left)
        tvZodiacSign = view.findViewById(R.id.tv_zodiac_sign)
        tvChineseSign = view.findViewById(R.id.tv_chinese_sign)
        buttonEdit = view.findViewById(R.id.button_edit)
        buttonDelete = view.findViewById(R.id.button_delete)
        buttonAddNotes = view.findViewById(R.id.button_add_notes)
    }

    private fun setClickListeners() {
        buttonEdit.setOnClickListener {
            launchFragment(EventItemFragment.newInstanceEditEvent(eventId))
        }

        buttonDelete.setOnClickListener {
            viewModel.apply {
                event.observe(viewLifecycleOwner) {
                    deleteEvent(it)
                }
            }
            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            launchPageFragment(EventListFragment.newInstance()) // - тэг обновляется, но нет анимации
            //requireActivity().onBackPressed() - будет анимация удаления, но не будет обновляться тэг
            //TODO: чото сделать
        }

        buttonAddNotes.setOnClickListener {
            //TODO: addNotes function
            saySoon()
        }
    }

    private fun launchFragment(fragment: Fragment) {
        requireActivity().apply {
            supportFragmentManager.beginTransaction()
                .replace(R.id.place_holder, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun saySoon(){
        Toast.makeText(requireContext(),"Скоро...",Toast.LENGTH_SHORT).show()
        val mp = MediaPlayer.create(requireContext(),R.raw.soon)
        mp.start()
    }

    private fun launchPageFragment(fragment: Fragment) {
        requireActivity().apply {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.page_holder, fragment)
                .commit()
        }
    }

    companion object {
        const val EVENT_ID = "event_id"

        fun newInstanceShowEvent(eventId: Int): EventInfoFragment {
            return EventInfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(EVENT_ID, eventId)
                }
            }
        }
    }
}
