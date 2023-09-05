package com.team2.contactapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.team2.contactapp.databinding.FragmentListBinding


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val userRecyclerViewAdapter by lazy {
        UserRecyclerViewAdapter(SampleData.userList,
            object : UserRecyclerViewAdapter.OnClickEventListener {
                override fun onItemClick(user: User) {
                    if (parentFragment is ContactFragment) {
                        (parentFragment as ContactFragment).moveDetailFragment(user)
                    }
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        userRecyclerView.apply {
            adapter = userRecyclerViewAdapter
            layoutManager = LinearLayoutManager(this@ListFragment.context)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance() = ListFragment()
    }
}