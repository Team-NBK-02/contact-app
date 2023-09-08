package com.team2.contactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team2.contactapp.databinding.FragmentListBinding


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var userRecyclerViewAdapter =
        UserRecyclerViewAdapter(
            SampleData.userList,
            LINEAR_TYPE,
            object : UserRecyclerViewAdapter.OnClickEventListener {
                override fun onItemClick(user: User) {
                    if (parentFragment is ContactFragment) {
                        (parentFragment as ContactFragment).moveDetailFragment(user)
                    }
                }
            })

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

            val context = this@ListFragment.context
            val itemTouchHelper = ItemTouchHelper(UserListItemHelper(context!!) { pos ->
                val phoneNumber = userRecyclerViewAdapter.userArrayList[pos].phoneNumber
                val callUriSwipedPerson = Uri.parse("tel:$phoneNumber")
                val callIntent = Intent(Intent.ACTION_CALL, callUriSwipedPerson)
                userRecyclerViewAdapter.notifyDataSetChanged()
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // 권한이 부여되지 않았으므로 권한을 요청합니다.
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE),
                        1
                    )
                } else {
                    context.startActivity(callIntent)
                }
            })
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    fun changeLayoutManager(type: Int) = with(binding) {
        if (userRecyclerViewAdapter.currentType == type) return@with
        when (type) {
            LINEAR_TYPE -> {
                userRecyclerView.layoutManager = LinearLayoutManager(this@ListFragment.context)
                userRecyclerViewAdapter = userRecyclerViewAdapter.copyOf(LINEAR_TYPE)
                userRecyclerView.adapter = userRecyclerViewAdapter
            }

            GRID_TYPE -> {
                userRecyclerView.layoutManager = GridLayoutManager(this@ListFragment.context, 2)
                userRecyclerViewAdapter = userRecyclerViewAdapter.copyOf(GRID_TYPE)
                userRecyclerView.adapter = userRecyclerViewAdapter
            }
        }
    }

    fun isInUserList(name: String?, phoneNumber: String?) : Boolean {
        return userRecyclerViewAdapter.isInUserList(name,phoneNumber)
    }
    fun addUser(user: User) {
        userRecyclerViewAdapter.addUser(user)
    }

    companion object {
        const val LINEAR_TYPE = 0
        const val GRID_TYPE = 1
        private const val TAG = "ListFragment"
        fun newInstance() = ListFragment()
    }
}