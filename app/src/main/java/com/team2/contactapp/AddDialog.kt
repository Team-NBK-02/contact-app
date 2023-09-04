package com.team2.contactapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.team2.contactapp.databinding.DialogAddBinding

class AddDialog(private val addDialogInterface: AddDialogInterface) : DialogFragment() {
    private var _binding : DialogAddBinding? = null
    private val binding get() = _binding!!

    interface AddDialogInterface{
        fun onSaveButtonClicked(){

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogAddBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding){
        button.setOnClickListener {
            addDialogInterface.onSaveButtonClicked()
            dismiss()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}