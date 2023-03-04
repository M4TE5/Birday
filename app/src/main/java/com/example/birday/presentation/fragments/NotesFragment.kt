package com.example.birday.presentation.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.birday.R
import com.example.birday.databinding.FragmentNotesBinding
import com.example.birday.domain.Event
import com.example.birday.presentation.viewmodels.EventItemViewModel

@RequiresApi(Build.VERSION_CODES.O)
class NotesFragment : DialogFragment() {


    private lateinit var viewModel: EventItemViewModel
    private lateinit var binding: FragmentNotesBinding

    private val args by navArgs<NotesFragmentArgs>()
    private var eventId: Int = Event.UNDEFINED_ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun getTheme(): Int = R.style.NotesStyle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotesBinding.bind(view)
        viewModel = ViewModelProvider(this)[EventItemViewModel::class.java]
        fillInfo()
        setOnClickListeners()
    }

    private fun fillInfo(){
        viewModel.getItemById(eventId)
        viewModel.event.observe(viewLifecycleOwner){
            binding.tvHeader.text = "Notes - ${it.firstName}"
            binding.etNotes.setText(it.notes)
        }
    }

    private fun parseParams(){
        eventId = getEventId()
        if (eventId == Event.UNDEFINED_ID) {
            throw RuntimeException("Param event id is undefined")
        }
    }

    private fun setOnClickListeners(){
        binding.apply {
            bCancel.setOnClickListener{
                dismiss()
            }

            bOk.setOnClickListener {
                val notes = etNotes.text.toString()
                viewModel.editNotes(notes)
                dismiss()
            }
        }
    }

    private fun getEventId() = args.eventId

}