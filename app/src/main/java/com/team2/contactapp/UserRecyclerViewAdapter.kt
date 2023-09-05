package com.team2.contactapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team2.contactapp.databinding.ItemRecylerViewLinearType1Binding
import com.team2.contactapp.databinding.ItemRecylerViewLinearType2Binding

class UserRecyclerViewAdapter(userList : List<User>,val clickListener: OnClickEventListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnClickEventListener {
        fun onItemClick(user: User)
    }
    val userArrayList = ArrayList<User>().apply {
        addAll(userList)
    }

    inner class ViewHolderLinearType1(private val binding: ItemRecylerViewLinearType1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) = with(binding) {
            profileImageView.setImageResource(user.imgRes)
            nameTextView.text = user.name
            root.setOnClickListener { clickListener.onItemClick(user) }
        }
    }

    inner class ViewHolderLinearType2(private val binding: ItemRecylerViewLinearType2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) = with(binding) {
            profileImageView.setImageResource(user.imgRes)
            nameTextView.text = user.name
            root.setOnClickListener { clickListener.onItemClick(user) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            LINEAR_TYPE1 -> ViewHolderLinearType1(
                ItemRecylerViewLinearType1Binding.inflate(
                    LayoutInflater.from(parent.context),parent,false
                )
            )

            LINEAR_TYPE2 -> ViewHolderLinearType2(
                ItemRecylerViewLinearType2Binding.inflate(
                    LayoutInflater.from(parent.context),parent,false
                )
            )

            else -> ViewHolderLinearType2(
                ItemRecylerViewLinearType2Binding.inflate(
                    LayoutInflater.from(parent.context),parent,false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolderLinearType1 -> holder.bind(user = userArrayList[position])
            is ViewHolderLinearType2 -> holder.bind(user = userArrayList[position])
        }
    }

    companion object {
        private const val LINEAR_TYPE1 = 0
        private const val LINEAR_TYPE2 = 1
    }

}