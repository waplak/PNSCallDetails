package com.pns.lk.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.pns.lk.R
import com.pns.lk.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.main_fragment)
        setupActionBarWithNavController(navController)
        checkAndRequestPermissions()

        val sharedPreferences = getSharedPreferences("PNS_PREF", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        if(!sharedPreferences.contains("durationIndex")) {
            myEdit.putInt("durationIndex", 2)

        }
        if(!sharedPreferences.contains("replyMsg")) {
            myEdit.putString("replyMsg", "I will call you later")
        }
        myEdit.commit()

//        Utility.readAlert(this)
//        Utility.readMissedCall(this)
//        Utility.readIncomeCall(this)
//        Utility.readOutgoingCall(this)
        setupSmoothBottomMenu()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.another_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.another_item_1 -> {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu_bottom)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissionReadMessage =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
        val permissionReadContacts =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        val permissionPhoneCall =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
        val permissionReadCallLogs =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (permissionReadMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS)
        }
        if (permissionReadContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS)
        }
        if (permissionPhoneCall != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE)
        }
        if (permissionReadCallLogs != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALL_LOG)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                1
            )
            return false
        }
        return true
    }
}
