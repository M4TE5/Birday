package com.example.birday.presentation.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.birday.R
import com.example.birday.domain.Event
import com.example.birday.presentation.viewmodels.EventItemViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
@RequiresApi(Build.VERSION_CODES.O)
class EventItemFragment : Fragment() {

    private lateinit var viewModel: EventItemViewModel

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etDate: EditText
    private lateinit var buttonSave: Button
    private lateinit var tvHeader: TextView
    private lateinit var icon: ImageView

    private var screenMode: String = MODE_UNKNOWN
    private var eventId: Int = Event.UNDEFINED_ID

    private val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
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
        setDatePicker()
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
        tvHeader.text = getString(R.string.edit_event)
        icon.setImageResource(R.drawable.baseline_edit_note_24)
        buttonSave.setOnClickListener {
            if(validateInput()){
                Log.d("MyLog","ищем $eventId")
                Log.d("MyLog","вот он ${viewModel.event.value?.firstName}")
                val date = LocalDate.parse(etDate.text.toString(), dateFormatter)
                viewModel.editItem(
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    date
                )
                requireActivity().onBackPressed()
            }
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            if (validateInput()){
                val date = LocalDate.parse(etDate.text.toString(), dateFormatter)
                viewModel.addItem(
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    date
                )
                requireActivity().onBackPressed()
            }
        }
    }

   private fun validateInput(): Boolean{
       val dateStr = etDate.text
       val firstName = etFirstName.text
       val lastName = etLastName.text
       if(firstName.isNotEmpty() && lastName.isNotEmpty() && dateStr.isNotEmpty()) return true
       Toast.makeText(requireContext(),"Incorrect input", Toast.LENGTH_SHORT).show()
       return false
   }

    private fun fillTextInfo() {
        viewModel.getItemById(eventId)
        viewModel.event.observe(viewLifecycleOwner) {
            etFirstName.setText(it.firstName)
            etLastName.setText(it.lastName)
            etDate.setText(it.date.format(dateFormatter))
        }
    }

    private fun setDatePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(),
            { _, year, month, dayOfMonth ->
                val date = LocalDate.of(year,month + 1, dayOfMonth)
                val dateStr = date.format(dateFormatter)
                etDate.setText(dateStr)
            }, year, month, day)

        etDate.setOnClickListener {
            hideKeyboard()
            dialog.show()
        }
    }
    private fun hideKeyboard(){
        val view = requireActivity().currentFocus
        if(view != null){
            requireActivity().apply {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken,0)
            }
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
        icon = view.findViewById(R.id.icon)
        etDate = view.findViewById(R.id.et_date)
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