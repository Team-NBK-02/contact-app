package com.team2.contactapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = ArrayList<MainTabs>()

    init {
        fragments.add(MainTabs(ContactFragment.newInstance(),"Contact"))
        fragments.add(MainTabs(DetailFragment.newInstance(SampleData.userList[0]),"My Page"))
    }

    fun getTitle(position: Int): String {
        return fragments[position].title
    }


    override fun getItemCount(): Int {
        return fragments.size
    }

    /*fun submitTodo(todo: Todo) = with(fragments[0]) {
        if (fragment is TodoFragment) fragment.submitTodo(todo)
    }*/

    fun getFragment(position: Int) : Fragment = fragments[position].fragment

    override fun createFragment(position: Int): Fragment {
        return getFragment(position)
    }
}