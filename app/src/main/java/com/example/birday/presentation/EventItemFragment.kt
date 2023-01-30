package com.example.birday.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.birday.R
import com.example.birday.domain.Event

class EventItemFragment : Fragment() {

    private lateinit var viewModel: EventItemViewModel

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var buttonSave: Button
    private lateinit var tvHeader: TextView

    private var screenMode: String = MODE_UNKNOWN
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
        return layoutInflater.inflate(R.layout.fragment_event_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[EventItemViewModel::class.java]
        initViews(view)
        launchRightMode()
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchEditMode() {
        fillTextInfo()
        tvHeader.text = "Edit event"
        buttonSave.setOnClickListener {
            viewModel.editItem(etFirstName.text.toString(), etLastName.text.toString(), "edited date")
            requireActivity().onBackPressed()
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addItem(etFirstName.text.toString(), etLastName.text.toString(), "edited date")
            requireActivity().onBackPressed()
        }
    }

    private fun fillTextInfo() {
        viewModel.getItemById(eventId)
        viewModel.event.observe(viewLifecycleOwner){
            Log.d("MyLog","Name: " + it.name)
            etFirstName.setText(it.name)
            etLastName.setText(it.date)
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)

        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(EVENT_ID)) {
                throw RuntimeException("Param event id is absent")
            }
            eventId = args.getInt(EVENT_ID, Event.UNDEFINED_ID)
        }
    }

    private fun initViews(view: View) {
        etFirstName = view.findViewById(R.id.et_first_name)
        etLastName = view.findViewById(R.id.et_last_name)
        buttonSave = view.findViewById(R.id.b_save)
        tvHeader = view.findViewById(R.id.tv_header)
    }

    companion object {
        private const val SCREEN_MODE = "screen_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val EVENT_ID = "event_id"
        private const val MODE_UNKNOWN = ""

        fun newInstanceEditEvent(eventId: Int): EventItemFragment {
            return EventItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(EVENT_ID, eventId)
                    putString(SCREEN_MODE, MODE_EDIT)
                }
            }
        }

        fun newInstanceAddEvent(): EventItemFragment {
            return EventItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }
    }
}