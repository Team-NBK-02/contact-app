package com.team2.contactapp

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.team2.contactapp.databinding.DialogAddBinding

class AddDialog(private val addDialogInterface: AddDialogInterface) : DialogFragment() {
    private var _binding : DialogAddBinding? = null
    private val binding get() = _binding!!
    var nameRange = false
    private var phoneNumberType = false
    var emailType = false


    interface AddDialogInterface{
        fun eventClicked(eventNumber: Int)
        fun onSaveButtonClicked(user: User)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogAddBinding.inflate(layoutInflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding){

        var delay = 0

        noneImageView.isSelected = true

        nameInputEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val maxLine = 6
                nameRange = if ((nameInputEditText?.length() ?: 0) > maxLine){
                    nameInputLayout.error = "최대 6자리만 입력 가능합니다."
                    false
                } else{
                    nameInputLayout.error = ""
                    true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        phoneInputEditText.addTextChangedListener{ s: Editable? ->
            if (!s.toString().matches("[0-9]+".toRegex())){
                phoneNumberInputLayout.error = "숫자만 입력하세요"
                phoneNumberType = false
            }
            else{
                phoneNumberInputLayout.error = ""
                phoneNumberType = true
            }
        }
        emailInputEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!Patterns.EMAIL_ADDRESS.matcher(emailInputEditText.text.toString()).matches()) {
                    emailInputLayout.error = "올바른 형식으로 입력하세요"
                    emailType = false
                }
                else{
                    emailInputLayout.error = ""
                    emailType = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })



        saveButton.setOnClickListener {
            val name = nameInputEditText.text.toString()
            val phoneNumber = phoneInputEditText.text.toString()
            val email = emailInputEditText.text.toString()
            val image = R.drawable.ic_launcher_foreground
            val memo = memoInputEditText.text.toString()
            val user: User

            if(name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || memo.isEmpty() || !nameRange || !phoneNumberType || !emailType){
                Toast.makeText(requireContext(), "형식에 맞춰서 작성을 해주세요!!", Toast.LENGTH_SHORT).show()
            }else{
                user = User(name, image, phoneNumber, email, "", memo)
                addDialogInterface.eventClicked(delay)
                addDialogInterface.onSaveButtonClicked(user)
                dismiss()

            }
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
        noneImageView.setOnClickListener {
            noneImageView.isSelected = true
            fiveMinView.isSelected = false
            tenMinImageView.isSelected = false
            thirtyMinImageView.isSelected = false
            delay = 0
        }
        fiveMinView.setOnClickListener {
            noneImageView.isSelected = false
            fiveMinView.isSelected = true
            tenMinImageView.isSelected = false
            thirtyMinImageView.isSelected = false
            delay = 10000
        }
        tenMinImageView.setOnClickListener {
            noneImageView.isSelected = false
            fiveMinView.isSelected = false
            tenMinImageView.isSelected = true
            thirtyMinImageView.isSelected = false
            delay = 600000
        }
        thirtyMinImageView.setOnClickListener {
            fiveMinView.isSelected = false
            noneImageView.isSelected = false
            tenMinImageView.isSelected = false
            thirtyMinImageView.isSelected = true
            delay = 1800000
        }
    }



    override fun onResume() {
        super.onResume()
        requireContext().dialogFragmentResize(this, 0.9f, 0.8f)
    }

    private fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT < 30) {

            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)

        } else {

            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialogFragment.dialog?.window

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    fun doBackgroundWork() {

    }

}