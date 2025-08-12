package com.example.demoapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = navHostFragment?.let { findNavController(it) }
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        // Mostrar nombre de usuario en el Drawer
        val headerView = navigationView.getHeaderView(0)
        val tvHeaderTitle = headerView.findViewById<TextView>(R.id.tvHeaderTitle)
        val username = prefs.getString("username", "Usuario")
        tvHeaderTitle.text = username

        navController?.let {
           NavigationUI.setupWithNavController(navigationView, it)
            if (isLoggedIn) {
                it.navigate(R.id.pokedexFragment)
            }
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_pokedex -> {
                    navController?.navigate(R.id.pokedexFragment)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_logout -> {
                    showLogoutDialog()
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar sesión")
            .setMessage("¿Seguro que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                val prefs = getSharedPreferences("session", MODE_PRIVATE)
                prefs.edit().clear().apply()
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                val navController = navHostFragment?.let { androidx.navigation.fragment.NavHostFragment.findNavController(it) }
                navController?.navigate(R.id.loginFragment)
            }
            .setNegativeButton("No", null)
            .show()
    }
}