package com.oscar.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.oscar.R
import com.oscar.data.model.User
import com.oscar.data.model.Votacao
import com.oscar.databinding.ActivityHomeBinding
import com.oscar.repository.DatabaseHelper
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
        user = DatabaseHelper().findUser()

        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else if (user!!.accessToken.isEmpty()){
            startActivity(Intent(this, LoginActivity::class.java))

        }

    }
}