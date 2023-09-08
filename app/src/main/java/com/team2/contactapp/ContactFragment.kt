package com.team2.contactapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team2.contactapp.databinding.FragmentContactBinding

class ContactFragment : Fragment() {
    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    private val listFragment = ListFragment.newInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_contract, listFragment)
        transaction.commit()
    }

    fun moveDetailFragment(user: User) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.frame_contract, DetailFragment.newInstance(user))
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    fun changeLayoutManager(type: Int) {
        listFragment.changeLayoutManager(type)
    }

    fun isInUserList(name: String?, phoneNumber: String?) : Boolean {
        return listFragment.isInUserList(name,phoneNumber)
    }

    companion object {
        fun newInstance() = ContactFragment()
    }
}