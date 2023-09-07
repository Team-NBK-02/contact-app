package com.team2.contactapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import com.google.android.material.tabs.TabLayoutMediator
import com.team2.contactapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewPagerAdapter by lazy {
        ViewPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        setSupportActionBar(mainToolbar)
        mainToolbar.title = "SpartaContacts"
        mainViewpager.adapter = viewPagerAdapter

        // TabLayout x ViewPager2
        TabLayoutMediator(mainTab, mainViewpager) { tab, position ->
            tab.text = viewPagerAdapter.getTitle(position)
        }.attach()

        floatingActionButton.setOnClickListener {
            val addDialog = AddDialog(object : AddDialog.AddDialogInterface{
                override fun onSaveButtonClicked() {

                }
            })
            addDialog.show(this@MainActivity.supportFragmentManager,"ConfirmDialog")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recycler_view,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.linear -> {
                viewPagerAdapter.changeLayoutManager(ListFragment.LINEAR_TYPE)
            }
            R.id.grid -> {
                viewPagerAdapter.changeLayoutManager(ListFragment.GRID_TYPE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val currentFragment = viewPagerAdapter.getFragment(binding.mainTab.selectedTabPosition)
        if (currentFragment is ContactFragment && currentFragment.childFragmentManager.backStackEntryCount >= 1) {
            currentFragment.childFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }
}