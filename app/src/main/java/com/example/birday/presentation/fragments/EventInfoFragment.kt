package com.example.birday.presentation.fragments

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.birday.R
import com.example.birday.data.Dependencies
import com.example.birday.databinding.FragmentEventInfoBinding
import com.example.birday.domain.Event
import com.example.birday.presentation.viewmodels.EventInfoViewModel
import java.lang.Math.abs
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class EventInfoFragment : Fragment() {

    private val viewModel by lazy { EventInfoViewModel(Dependencies.eventListRepository) }
    private lateinit var binding: FragmentEventInfoBinding
    private val args by navArgs<EventInfoFragmentArgs>()

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
        binding = FragmentEventInfoBinding.bind(view)
        fillTextInfo()
        setClickListeners()
    }

    private fun fillTextInfo() {
        viewModel.getEventById(eventId)
        val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
        viewModel.event.observe(viewLifecycleOwner) {
            if(it != null){
                binding.apply {
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
        }
    }


    private fun parseParams() {
        eventId = getEventId()
        if (eventId == Event.UNDEFINED_ID) throw java.lang.RuntimeException("event id is undefined")
    }

    private fun getEventId(): Int = args.eventId

    private fun setClickListeners() = with(binding) {
        buttonEdit.setOnClickListener {
            val direction = EventInfoFragmentDirections.actionEventInfoFragmentToEventItemFragment(
                EventItemFragment.MODE_EDIT,
                eventId
            )
            findNavController().navigate(direction)
            buttonEdit.apply {
                isEnabled = false
                Handler().postDelayed({
                    this.isEnabled = true
                }, 1500)
            }
        }

        buttonDelete.setOnClickListener {
            viewModel.deleteEvent(viewModel.event.value)
            //findNavController().popBackStack()
            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()

            requireActivity().onBackPressed()
            //TODO: no deleted animation
        }

        buttonAddNotes.setOnClickListener {
            val direction =
                EventInfoFragmentDirections.actionEventInfoFragmentToNotesFragment(eventId)
            findNavController().navigate(direction)
        }
    }

}
