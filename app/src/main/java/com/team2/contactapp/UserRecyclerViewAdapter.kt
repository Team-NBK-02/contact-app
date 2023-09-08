package com.team2.contactapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team2.contactapp.databinding.ItemRecylerViewGridType1Binding
import com.team2.contactapp.databinding.ItemRecylerViewGridType2Binding
import com.team2.contactapp.databinding.ItemRecylerViewLinearType1Binding
import com.team2.contactapp.databinding.ItemRecylerViewLinearType2Binding

class UserRecyclerViewAdapter(userList: List<User>,val currentType:Int, val clickListener: OnClickEventListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnClickEventListener {
        fun onItemClick(user: User)
    }

    interface CustomViewHolder {
        var mUser: User?
        val swipeView: View
        fun bind(user: User, pos: Int)
    }

    val userArrayList = ArrayList<User>().apply {
        addAll(userList)
    }

    fun copyOf(currentType: Int) : UserRecyclerViewAdapter = UserRecyclerViewAdapter(userArrayList,currentType,clickListener)

    inner class ViewHolderLinearType1(private val binding: ItemRecylerViewLinearType1Binding) :
        RecyclerView.ViewHolder(binding.root), CustomViewHolder {
        override var mUser: User? = null
        override val swipeView: View = binding.swipeItemLayout

        override fun bind(user: User, pos: Int) = with(binding) {
            mUser = user
            profileImageView.setImageResource(user.imgRes)
            nameTextView.text = user.name
            root.setOnClickListener { clickListener.onItemClick(user) }

            favoriteImageView.isSelected = user.isLike
            favoriteImageView.setOnClickListener {

            }
        }
    }

    inner class ViewHolderLinearType2(private val binding: ItemRecylerViewLinearType2Binding) :
        RecyclerView.ViewHolder(binding.root), CustomViewHolder {
        override var mUser: User? = null
        override val swipeView: View = binding.swipeItemLayout
        override fun bind(user: User, pos: Int) = with(binding) {
            mUser = user
            profileImageView.setImageResource(user.imgRes)
            nameTextView.text = user.name
            root.setOnClickListener { clickListener.onItemClick(user) }

            favoriteImageView.isSelected = user.isLike
            favoriteImageView.setOnClickListener {

            }
        }
    }

    inner class ViewHolderGridType1(private val binding: ItemRecylerViewGridType1Binding) :
        RecyclerView.ViewHolder(binding.root), CustomViewHolder {
        override var mUser: User? = null
        override val swipeView: View = binding.swipeItemLayout

        override fun bind(user: User, pos: Int) = with(binding) {
            mUser = user
            profileImageView.setImageResource(user.imgRes)
            phoneNumberTextView.text = user.phoneNumber
            nameTextView.text = user.name
            root.setOnClickListener { clickListener.onItemClick(user) }

            favoriteImageView.isSelected = user.isLike
            favoriteImageView.setOnClickListener {

            }
        }
    }

    inner class ViewHolderGridType2(private val binding: ItemRecylerViewGridType2Binding) :
        RecyclerView.ViewHolder(binding.root), CustomViewHolder {
        override var mUser: User? = null
        override val swipeView: View = binding.swipeItemLayout
        override fun bind(user: User, pos: Int) = with(binding) {
            mUser = user
            profileImageView.setImageResource(user.imgRes)
            phoneNumberTextView.text = user.phoneNumber
            nameTextView.text = user.name
            root.setOnClickListener { clickListener.onItemClick(user) }

            favoriteImageView.isSelected = user.isLike
            favoriteImageView.setOnClickListener {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (currentType) {
            ListFragment.LINEAR_TYPE -> return position % 2
            ListFragment.GRID_TYPE -> {
                return if (position % 4 > 1) position % 2 else (position + 1) % 2
            }

            else -> return super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (currentType) {
            ListFragment.LINEAR_TYPE -> {
                when (viewType) {
                    TYPE1 -> return ViewHolderLinearType1(
                        ItemRecylerViewLinearType1Binding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                        )
                    )

                    TYPE2 -> return ViewHolderLinearType2(
                        ItemRecylerViewLinearType2Binding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                        )
                    )
                }
            }

            ListFragment.GRID_TYPE -> {
                when (viewType) {
                    TYPE1 -> return ViewHolderGridType1(
                        ItemRecylerViewGridType1Binding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                        )
                    )

                    TYPE2 -> return ViewHolderGridType2(
                        ItemRecylerViewGridType2Binding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                        )
                    )
                }
            }
        }

        return ViewHolderLinearType2(
            ItemRecylerViewLinearType2Binding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CustomViewHolder) holder.bind(user = userArrayList[position], pos = position)
    }

    companion object {
        private const val TAG = "UserRecyclerViewAdapter"
        private const val TYPE1 = 0
        private const val TYPE2 = 1
    }

}