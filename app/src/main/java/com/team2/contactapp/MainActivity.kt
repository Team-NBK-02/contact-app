package com.team2.contactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.team2.contactapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var requsetLauncher: ActivityResultLauncher<Intent>
    private val viewPagerAdapter by lazy {
        ViewPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSettings()
        initViews()
    }

    private fun initSettings() {
        requsetLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val cursor = contentResolver.query(
                        it.data!!.data!!,
                        arrayOf(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ),
                        null,
                        null,
                        null,
                    )
                    Log.d(TAG, "initSettings: ${cursor?.count}")
                    if (cursor!!.moveToFirst()) {
                        val name = cursor?.getString(0) ?: ""
                        val phoneNumber = cursor?.getString(1) ?: ""
                        if (viewPagerAdapter.isInUserList(name, phoneNumber)) {
                            Toast.makeText(this, "해당 유저는 이미 존재합니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            val newUser = User(
                                name,
                                R.drawable.ic_launcher_foreground,
                                phoneNumber,
                                "",
                                "",
                                ""
                            )
                            viewPagerAdapter.addUser(newUser)
                        }
                    }

                }
            }
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
            val addDialog = AddDialog(object : AddDialog.AddDialogInterface {

                override fun eventClicked(eventDelay: Int) {
                    delay = eventDelay
                }

                override fun onSaveButtonClicked(user: User) {
                    viewPagerAdapter.addUser(user)
                    if (delay != 0) {
                        scheduleNotification(delay)
                    }

                }
            })
            addDialog.show(this@MainActivity.supportFragmentManager, "ConfirmDialog")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recycler_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.linear -> {
                viewPagerAdapter.changeLayoutManager(ListFragment.LINEAR_TYPE)
            }

            R.id.grid -> {
                viewPagerAdapter.changeLayoutManager(ListFragment.GRID_TYPE)
            }

            R.id.borrow -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "permission granted")
                    requsetLauncher.launch(
                        Intent(
                            Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                        )
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        1
                    )
                    Log.d(TAG, "permission denied")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: permission granted")
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission denied")
            }
        }
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

    companion object {
        private const val REQUEST_CODE_READ_CONTACTS = 1001
        private const val TAG = "MainActivity"
    }
}