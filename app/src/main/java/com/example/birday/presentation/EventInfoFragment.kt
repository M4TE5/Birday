package com.example.birday.presentation

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
import java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)
class EventInfoFragment: Fragment() {

    private lateinit var viewModel: EventInfoViewModel

    private lateinit var tvName : TextView
    private lateinit var tvDate : TextView
    private lateinit var buttonEdit: Button
    private lateinit var buttonDelete: Button

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

    private fun fillTextInfo(){
        viewModel.getEventById(eventId)
        val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
        viewModel.event.observe(viewLifecycleOwner){
            tvName.text = "Details - " + it.firstName
            val dayName = it.date.dayOfWeek.name.lowercase().replaceFirstChar { char -> char.uppercase() }
            val date = it.date.format(dateFormatter)
            tvDate.text = "$dayName, $date"
        }
    }

    private fun parseParams(){
        val args = requireArguments()
        if (!args.containsKey(EVENT_ID)){
            throw RuntimeException("Param event id is absent")
        }
        eventId = args.getInt(EVENT_ID, Event.UNDEFINED_ID)
    }

    private fun initViews(view: View){
        tvName = view.findViewById(R.id.tv_name)
        tvDate = view.findViewById(R.id.tv_date)
        buttonEdit = view.findViewById(R.id.button_edit)
        buttonDelete = view.findViewById(R.id.button_delete)
    }

    private fun setClickListeners(){
        buttonEdit.setOnClickListener {
            launchFragment(EventItemFragment.newInstanceEditEvent(eventId))
        }

        buttonDelete.setOnClickListener {
            viewModel.apply{
                event.observe(viewLifecycleOwner){
                    deleteEvent(it)
                }
            }
            Toast.makeText(requireContext(),"Deleted",Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        }
    }

    private fun launchFragment(fragment: Fragment){
        requireActivity().apply {
            supportFragmentManager.beginTransaction()
                .replace(R.id.place_holder, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object{
        const val EVENT_ID = "event_id"

        fun newInstanceShowEvent(eventId: Int) : EventInfoFragment{
            return EventInfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(EVENT_ID, eventId)
                }
            }
        }
    }
}
