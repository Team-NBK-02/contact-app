package com.team2.contactapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
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
            var delay: Int = 0
            val addDialog = AddDialog(object : AddDialog.AddDialogInterface{

                override fun eventClicked(eventDelay: Int) {
                    delay = eventDelay
                }

                override fun onSaveButtonClicked(user: User) {
                    viewPagerAdapter.addUser(user)
                    if (delay != 0){
                        scheduleNotification(delay)
                    }

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
    private fun notification() { // 종모양을 누르면 알림 발생
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "one-channel"
            val channelName = "My channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "My Channel One Description"
                setShowBadge(true)
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(this, channelId)
        } else {
            builder = NotificationCompat.Builder(this)
        }
        builder.run {
            setSmallIcon(R.mipmap.ic_launcher)
            setWhen(System.currentTimeMillis())
            setContentTitle("키워드 알림")
            setContentText("설정한 키워드에 대한 알림이 도착했습니다!!")
        }

        manager.notify(11, builder.build())
    }
    private fun scheduleNotification(delayInMillis: Int) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            notification()
        }, delayInMillis.toLong())
    }
}