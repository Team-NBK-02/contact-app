package com.team2.contactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.team2.contactapp.databinding.FragmentDetailBinding

private const val ARG_PARAM1 = "User"

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_PARAM1,User::class.java)
            } else {
                it.getParcelable(ARG_PARAM1) as User?
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userImageView.setImageResource(user!!.imgRes)
        binding.userNameTextView.text = user?.name
        binding.mobileTextView.text = user?.phoneNumber
        binding.emailTextView.text = user?.email
        binding.eventTextView.text = user?.event
        binding.memoTextView.text = user?.memo
        initViews()

        val context = this@DetailFragment.context
        binding.callImageButton.setOnClickListener {
            val phoneNumber = user?.phoneNumber
            val callUri = Uri.parse("tel:$phoneNumber")
            val callIntent = Intent(Intent.ACTION_CALL, callUri)
            if (context?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.CALL_PHONE) } != PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되지 않았으므로 권한을 요청합니다.
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
            } else {
                context.startActivity(callIntent)
            }
        }
        binding.messageImageButton.setOnClickListener {
            val phoneNumber = user?.phoneNumber
            val messageUri = Uri.parse("sms:$phoneNumber")
            val messageIntent = Intent(Intent.ACTION_SENDTO,messageUri)
            if (context?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.SEND_SMS) } != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS), 1)
            } else {
                context.startActivity(messageIntent)
            }
        }
    }

    private fun initViews() = with(binding) {

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(user: User) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,user)
                }
            }
    }
}