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
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.birday.R
import com.example.birday.databinding.FragmentEventItemBinding
import com.example.birday.domain.Event
import com.example.birday.presentation.viewmodels.EventItemViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
@RequiresApi(Build.VERSION_CODES.O)
class EventItemFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: EventItemViewModel

    private lateinit var binding: FragmentEventItemBinding

    private val args by navArgs<EventItemFragmentArgs>()

    private var screenMode: String = MODE_UNKNOWN
    private var eventId: Int = Event.UNDEFINED_ID

    private val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
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
        binding = FragmentEventItemBinding.bind(view)
        viewModel = ViewModelProvider(this)[EventItemViewModel::class.java]
        binding.bCancel.setOnClickListener {
            dismiss()
        }
        setDatePicker()
        launchRightMode()
    }
    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchEditMode() = with(binding){
        fillTextInfo()
        tvHeader.text = getString(R.string.edit_event)
        bInsertItem.text = "Update event"
        icon.setImageResource(R.drawable.baseline_edit_note_24)
        bInsertItem.setOnClickListener {
            if(validateInput()){
                val date = LocalDate.parse(etSelectDate.text.toString(), dateFormatter)
                viewModel.editItem(
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    date
                )
                findNavController().popBackStack(R.id.eventListFragment, false)
            }
        }
    }

    private fun launchAddMode() = with(binding){
        bInsertItem.text = "Insert event"
        bInsertItem.setOnClickListener {
            if (validateInput()){
                val date = LocalDate.parse(etSelectDate.text.toString(), dateFormatter)
                viewModel.addItem(
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    date
                )
                requireActivity().onBackPressed()
            }
        }
    }

   private fun validateInput(): Boolean = with(binding){
       val dateStr = etSelectDate.text.toString()
       val firstName = etFirstName.text.toString()
       val lastName = etLastName.text.toString()
       if(firstName.isNotEmpty() && lastName.isNotEmpty() && dateStr.isNotEmpty()) return true
       Toast.makeText(requireContext(),"Incorrect input", Toast.LENGTH_SHORT).show()
       return false
   }

    private fun fillTextInfo() {
        viewModel.getItemById(eventId)
        viewModel.event.observe(viewLifecycleOwner) {
            binding.apply {
                etFirstName.setText(it.firstName)
                etLastName.setText(it.lastName)
                etSelectDate.setText(it.date.format(dateFormatter))
            }
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
                binding.etSelectDate.setText(dateStr)
            }, year, month, day)

        binding.etSelectDate.setOnClickListener {
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
        screenMode = getScreenMode()
        eventId = getEventId()

        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $screenMode")
        }

        if (screenMode == MODE_EDIT) {
            if (eventId == Event.UNDEFINED_ID) {
                throw RuntimeException("Param event id is undefined")
            }
        }
    }

    private fun getEventId(): Int = args.eventId
    private fun getScreenMode(): String = args.screenMode

    companion object {
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""
    }
}