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
            /*var itemTouchHelper: ItemTouchHelper? = null
            val itemTouchCallback = object : ItemTouchHelper.Callback() {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                ): Int {
                    Log.d(TAG, "getMovementFlags: ")
                    return makeMovementFlags(0, ItemTouchHelper.RIGHT)
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction:Int) {
                    Log.d(TAG, "onSwiped: ")
                    val pos = viewHolder.adapterPosition
                    val phoneNumber = userRecyclerViewAdapter.userArrayList[pos].phoneNumber
                    val callUriSwipedPerson = Uri.parse("tel:$phoneNumber")
                    val callIntent = Intent(Intent.ACTION_CALL, callUriSwipedPerson)
                    //userRecyclerViewAdapter.notifyItemChanged(pos)
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // 권한이 부여되지 않았으므로 권한을 요청합니다.
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
                    } else {
                        startActivity(callIntent)
                    }
                    *//*getDefaultUIUtil().clearView((viewHolder as UserRecyclerViewAdapter.CustomViewHolder).swipeView)
                    userRecyclerViewAdapter.notifyItemChanged(pos)*//*
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    //Log.d(TAG, "onChildDraw: $actionState")
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && viewHolder is UserRecyclerViewAdapter.CustomViewHolder){
                        val view = viewHolder.swipeView
                        getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive)
                        //userRecyclerViewAdapter.notifyItemChanged(viewHolder.adapterPosition)
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    Log.d(TAG, "clearView: ")
                    super.clearView(recyclerView, viewHolder)
                }

                override fun isItemViewSwipeEnabled(): Boolean {
                    return true
                }
                override fun isLongPressDragEnabled(): Boolean {
                    return false
                }
            }

            itemTouchHelper = ItemTouchHelper(itemTouchCallback)
            itemTouchHelper.attachToRecyclerView(this@apply)*/

        }
    }

    override fun onResume() {
        super.onResume()
        //userRecyclerViewAdapter.notifyDataSetChanged()
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

    companion object {
        const val LINEAR_TYPE = 0
        const val GRID_TYPE = 1
        private const val TAG = "ListFragment"
        fun newInstance() = ListFragment()
    }
}