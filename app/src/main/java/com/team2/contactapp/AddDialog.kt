package com.team2.contactapp

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
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
    private var _binding: DialogAddBinding? = null
    private val binding get() = _binding!!
    private var nameRange = false
    private var phoneNumberType = false
    private var emailType = false

    interface AddDialogInterface {
        fun eventClicked(eventNumber: Int)
        fun onSaveButtonClicked(user: User)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DialogAddBinding.inflate(layoutInflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        //초기값
        var delay = 0
        var delayText = "none"
        noneImageView.isSelected = true

        nameInputEditText.addTextChangedListener { s: Editable? ->
            val maxLine = 6
            nameRange = if (s.isNullOrBlank()) {
                nameInputLayout.error = "공백은 입력 불가합니다."
                false
            } else {
                nameInputLayout.error = ""
                true
            }
        }
        phoneInputEditText.addTextChangedListener { s: Editable? ->
            if (!s.toString().matches("[0-9]+".toRegex())) {
                phoneNumberInputLayout.error = "숫자만 입력하세요"
                phoneNumberType = false
            } else {
                phoneNumberInputLayout.error = ""
                phoneNumberType = true
            }
        }
        emailInputEditText.addTextChangedListener { s: Editable? ->
            if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                emailInputLayout.error = "올바른 형식으로 입력하세요"
                emailType = false
            } else {
                emailInputLayout.error = ""
                emailType = true
            }
        }

        saveButton.setOnClickListener {
            val name = nameInputEditText.text.toString()
            val phoneNumber = phoneInputEditText.text.toString()
            val email = emailInputEditText.text.toString()
            val image = R.drawable.ic_launcher_foreground
            val memo = memoInputEditText.text.toString()
            val user: User

            if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || memo.isEmpty() || !nameRange || !phoneNumberType || !emailType) {
                Toast.makeText(requireContext(), "형식에 맞춰서 작성을 해주세요!!", Toast.LENGTH_SHORT).show()
            } else {
                user = User(name, image, phoneNumber, email, delayText, memo)
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
            delayText = "none"
        }

        fiveMinView.setOnClickListener {
            noneImageView.isSelected = false
            fiveMinView.isSelected = true
            tenMinImageView.isSelected = false
            thirtyMinImageView.isSelected = false
            delay = 10000
            delayText = "after 5min"
        }

        tenMinImageView.setOnClickListener {
            noneImageView.isSelected = false
            fiveMinView.isSelected = false
            tenMinImageView.isSelected = true
            thirtyMinImageView.isSelected = false
            delay = 600000
            delayText = "after 10min"
        }

        thirtyMinImageView.setOnClickListener {
            fiveMinView.isSelected = false
            noneImageView.isSelected = false
            tenMinImageView.isSelected = false
            thirtyMinImageView.isSelected = true
            delay = 1800000
            delayText = "after 30min"
        }
    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogFragmentResize(this, 0.9f, 0.8f)
    }

    private fun Context.dialogFragmentResize(
        dialogFragment: DialogFragment,
        width: Float,
        height: Float,
    ) {
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
}