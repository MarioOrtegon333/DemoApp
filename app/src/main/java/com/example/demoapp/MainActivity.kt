    package com.example.demoapp

    import android.content.Intent
    import android.os.Bundle
    import android.widget.TextView
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatActivity
    import androidx.drawerlayout.widget.DrawerLayout
    import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
    import androidx.navigation.ui.NavigationUI
    import com.example.demoapp.utils.SessionManager
    import com.google.android.material.navigation.NavigationView

    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)

            val isLoggedIn = SessionManager.isLoggedIn(this)

            // Si no está logueado, navega a LoginActivity y termina MainActivity
            if (isLoggedIn){

            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return
            }

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val navController = navHostFragment?.let { findNavController(it) }
            val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
            val navigationView = findViewById<NavigationView>(R.id.navigation_view)

            // Configurar ActionBarDrawerToggle
            val toggle = androidx.appcompat.app.ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

                // Mostrar nombre de usuario en el Drawer
            val headerView = navigationView.getHeaderView(0)
            val tvHeaderTitle = headerView.findViewById<TextView>(R.id.tvHeaderTitle)
            val username = com.example.demoapp.utils.SessionManager.getUsername(this) ?: "Usuario"
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
                            // Limpiar sesión y navegar a LoginActivity
                            com.example.demoapp.utils.SessionManager.clearSession(this)
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                            true
                    }
                    else -> false
                }
            }
        }
    }