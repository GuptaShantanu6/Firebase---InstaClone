package com.example.instaclone

import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.instaclone.fragments.*

class MainActivity : AppCompatActivity() {

    private var selectedFragment : Fragment? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.nav_home -> {
                selectedFragment = HomeFragment()
                moveToFragment(selectedFragment as HomeFragment)
            }
            R.id.nav_search -> {
                selectedFragment = SearchFragment()
                moveToFragment(selectedFragment as SearchFragment)
            }
            R.id.nav_add_post ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Type of Post")
                builder.setPositiveButton("Image"){ _: DialogInterface, _: Int ->
                    startActivity(Intent(this@MainActivity,UploadPostActivity::class.java))
                }
                builder.setNegativeButton("Video"){ _: DialogInterface, _: Int ->
                    startActivity(Intent(this@MainActivity,UploadVideoActivity::class.java))
                }
                val alertDialog = builder.create()
                alertDialog.setOnCancelListener {
                    startActivity(Intent(this@MainActivity,MainActivity::class.java))
                }
                alertDialog.show()

            }
            R.id.nav_notifications -> {
                selectedFragment = NotificationsFragment()
                moveToFragment(selectedFragment as NotificationsFragment)
            }
            R.id.nav_profile -> {
                selectedFragment = ProfileFragment()
                moveToFragment(selectedFragment as ProfileFragment)
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        //To change the name of the appBar as per the current Fragment
//
//        navView.setupWithNavController(navController)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val nav_view : BottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        selectedFragment = HomeFragment()
        moveToFragment(selectedFragment as HomeFragment)
        
    }


    private fun moveToFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container,fragment)
            commit()
        }
    }
}