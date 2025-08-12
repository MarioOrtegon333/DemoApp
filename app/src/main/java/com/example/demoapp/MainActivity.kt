    package com.example.demoapp

    import android.content.Intent
    import android.os.Bundle
    import android.widget.TextView
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatActivity
    import dagger.hilt.android.AndroidEntryPoint
    import androidx.drawerlayout.widget.DrawerLayout
    import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
    import androidx.navigation.ui.NavigationUI
    import com.example.demoapp.utils.SessionManager
    import com.google.android.material.navigation.NavigationView
    import androidx.appcompat.widget.Toolbar
    import androidx.appcompat.app.ActionBarDrawerToggle
    import androidx.navigation.NavController
    import androidx.core.view.GravityCompat
    import androidx.appcompat.R.drawable

    @AndroidEntryPoint
    class MainActivity : AppCompatActivity() {

        private lateinit var toolbar: Toolbar
        private lateinit var toggle: ActionBarDrawerToggle


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val isLoggedIn = SessionManager.isLoggedIn(this)
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val navController = navHostFragment?.let { findNavController(it) }
            val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
            val navigationView = findViewById<NavigationView>(R.id.navigation_view)
            toolbar = findViewById(R.id.toolbar)

            //Configurar Toolbar
            configToolbar(toolbar)

            //Configurar Drawer Menu
            configDrawerMenu(toolbar, drawerLayout, navigationView)

            //Configurar Navigation View
            configNavigationView(navigationView, navController, drawerLayout, isLoggedIn)

        }

        private fun configToolbar(toolbar: Toolbar?) {
            setSupportActionBar(toolbar)
            toolbar?.setTitleTextColor(android.graphics.Color.WHITE)
            toolbar?.navigationIcon?.setTint(android.graphics.Color.WHITE)

        }

        private fun configDrawerMenu(
            toolbar: Toolbar?,
            drawerLayout: DrawerLayout?,
            navigationView: NavigationView?
        ) {

            toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawerLayout?.addDrawerListener(toggle)
            toggle.syncState()

            // Cambia el color del icono del menú de hamburguesa a blanco
            toggle.drawerArrowDrawable.color = android.graphics.Color.WHITE

            // Mostrar nombre de usuario en el Drawer
            val headerView = navigationView?.getHeaderView(0)
            val tvHeaderTitle = headerView?.findViewById<TextView>(R.id.tvHeaderTitle)
            val username = SessionManager.getUsername(this) ?: "Usuario"
            tvHeaderTitle?.text = username

        }

        private fun configNavigationView(navigationView: NavigationView?, navController: NavController?, drawerLayout: DrawerLayout?, loggedIn: Boolean) {

            // Configurar el título de la ActionBar y el comportamiento del Drawer según el fragmento actual
            navController?.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.pokemonDetailFragment -> {
                        supportActionBar?.title = getString(R.string.detail_title)
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                        toggle.isDrawerIndicatorEnabled = false
                        toolbar.setNavigationIcon(drawable.abc_ic_ab_back_material)
                        toolbar.setNavigationOnClickListener {
                            navController.navigateUp()
                        }

                        toolbar.navigationIcon?.setTint(android.graphics.Color.WHITE)
                    }
                    else -> {
                        supportActionBar?.title = getString(R.string.pokedex_title)
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                        toggle.isDrawerIndicatorEnabled = true
                        toggle.syncState()
                        toolbar.setNavigationOnClickListener {
                            drawerLayout?.openDrawer(GravityCompat.START)
                        }
                    }
                }
            }

            navController?.let {
                if (navigationView != null) {
                    NavigationUI.setupWithNavController(navigationView,navController )
                }
                if (loggedIn) {
                    it.navigate(R.id.pokedexFragment)
                }
            }

            navigationView?.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_pokedex -> {
                        // Navegar al fragmento de Pokedex
                        navController?.navigate(R.id.pokedexFragment)
                        drawerLayout?.closeDrawers()
                        true
                    }

                    R.id.nav_logout -> {

                        // Mostrar diálogo de confirmación antes de cerrar sesión
                        showLogoutConfirmation()
                        true
                    }

                    else -> false
                }
            }


        }

        private fun showLogoutConfirmation() {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cerrar sesión")
                .setMessage("¿Seguro que deseas cerrar sesión?")
                .setPositiveButton("Sí") { _, _ ->
                    SessionManager.clearSession(this)
                    // Lanzar LoginActivity y terminar la actividad actual
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No") { _, _ -> }
                .show()
        }
    }