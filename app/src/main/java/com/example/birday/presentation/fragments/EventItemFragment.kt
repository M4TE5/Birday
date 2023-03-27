package com.example.birday.presentation.fragments

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.birday.R
import com.example.birday.data.Dependencies
import com.example.birday.databinding.FragmentEventItemBinding
import com.example.birday.domain.Event
import com.example.birday.presentation.viewmodels.EventItemViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
class EventItemFragment : BottomSheetDialogFragment() {

    private val viewModel by lazy { EventItemViewModel(Dependencies.eventListRepository) }
    private lateinit var binding: FragmentEventItemBinding
    private val args by navArgs<EventItemFragmentArgs>()

    private var screenMode: String = MODE_UNKNOWN
    private var eventId: Int = Event.UNDEFINED_ID
    private val dateFormatter = DateTimeFormatter.ofPattern(Event.DATE_FORMAT)
    private var imageUri: String? = null
    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return dialog
    }

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
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    imageUri = result.data?.data.toString()
                    Glide.with(requireContext())
                        .load(imageUri)
                        .circleCrop()
                        .into(binding.personImage)
                }
            }
        return layoutInflater.inflate(R.layout.fragment_event_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventItemBinding.bind(view)
        binding.bCancel.setOnClickListener {
            dismiss()
        }
        setUpSpinner()
        setDatePicker()
        setImagePicker()
        launchRightMode()
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchEditMode() = with(binding) {
        fillEventInfo()
        tvHeader.text = getString(R.string.edit_event)
        bInsertItem.text = "Update event"
        icon.setImageResource(R.drawable.baseline_edit_note_24)
        bInsertItem.setOnClickListener {
            if (validateInput()) {
                val date = LocalDate.parse(etSelectDate.text.toString(), dateFormatter)
                viewModel.editItem(
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    date,
                    tvEventType.text.toString(),
                    imageUri = imageUri
                )
                findNavController().popBackStack(R.id.eventListFragment, false)
            }
        }
    }

    private fun launchAddMode() = with(binding) {
        bInsertItem.text = "Insert event"
        bInsertItem.setOnClickListener {
            if (validateInput()) {
                val date = LocalDate.parse(etSelectDate.text.toString(), dateFormatter)
                viewModel.addItem(
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    date,
                    tvEventType.text.toString(),
                    imageUri = imageUri
                )
                requireActivity().onBackPressed()
            }
        }
    }

    private fun validateInput(): Boolean = with(binding) {
        val dateStr = etSelectDate.text.toString()
        val firstName = etFirstName.text.toString()
        val lastName = etLastName.text.toString()
        if (firstName.isNotEmpty() && lastName.isNotEmpty() && dateStr.isNotEmpty()) return true
        Toast.makeText(requireContext(), "Incorrect input", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun fillEventInfo() {
        viewModel.getItemById(eventId)
        viewModel.event.observe(viewLifecycleOwner) {
            imageUri = it.imageUri
            binding.apply {
                setEventImage(imageUri)
                etFirstName.setText(it.firstName)
                etLastName.setText(it.lastName)
                etSelectDate.setText(it.date.format(dateFormatter))
                tvEventType.setText(it.eventType,false)
            }
        }
    }

    private fun setEventImage(imageUri: String?) {
        Glide.with(requireContext())
            .load(imageUri ?: R.drawable.ic_baseline_person_24)
            .circleCrop()
            .into(binding.personImage)
    }

    private fun setDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val date = LocalDate.of(year, month + 1, dayOfMonth)
                val dateStr = date.format(dateFormatter)
                binding.etSelectDate.setText(dateStr)
            }, year, month, day
        )

        binding.etSelectDate.setOnClickListener {
            hideKeyboard()
            dialog.show()
        }
    }

    private fun setImagePicker() {
        binding.personImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcher?.launch(intent)
        }
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            requireActivity().apply {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
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

    private fun setUpSpinner() {
        val eventTypeList = getEventTypeList()
        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            requireContext(),
            R.layout.event_type_item,
            R.id.tv_event_type,
            eventTypeList
        )

        binding.apply {
            tvEventType.setAdapter(adapter)
            tvEventType.setText(eventTypeList.first(),false)
        }
    }

    private fun getEventTypeList(): ArrayList<String?> {
        return arrayListOf(
            "Birthday",
            "Anniversary",
            "Death Anniversary",
            "Name day",
            "Other"
        )
    }

    private fun getEventId(): Int = args.eventId
    private fun getScreenMode(): String = args.screenMode

    companion object {
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""
    }
}