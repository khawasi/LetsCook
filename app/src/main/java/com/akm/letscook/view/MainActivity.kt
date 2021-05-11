package com.akm.letscook.view

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.akm.letscook.R
import com.akm.letscook.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var _uiStateJob: Job? = null
    private var _navController: NavController? = null
    private var _binding: ActivityMainBinding? = null
    private var _appBarConfiguration: AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_LetsCook)
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_fragment_container_view) as NavHostFragment? ?: return
        _navController = navHostFragment.navController
        _navController!!.addOnDestinationChangedListener { _, destination, _ ->
            _binding?.let {
                it.mainToolbar.visibility = View.VISIBLE
                if (destination.id == R.id.categoryFragment || destination.id == R.id.homeFragment || destination.id == R.id.favoriteFragment) {
                    it.mainBottomNavigationView.visibility = View.VISIBLE
                    it.mainToolbar.menu.setGroupVisible(R.id.main_toolbar_menu, true)
                } else {
                    if (destination.id == R.id.searchFragment || destination.id == R.id.detailFragment) {
                        it.mainToolbar.visibility = View.GONE
                    } else {
                        it.mainToolbar.menu.setGroupVisible(R.id.main_toolbar_menu, false)
                    }
                    it.mainBottomNavigationView.visibility = View.GONE
                }
            }
        }

        _appBarConfiguration = AppBarConfiguration(_binding!!.mainBottomNavigationView.menu)

        if (savedInstanceState == null) {
            setupToolbar(_navController, _appBarConfiguration)
            setupBottomNav(_navController)
        }

        setupDayNightMode()

        setContentView(_binding!!.root)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupToolbar(_navController, _appBarConfiguration)
        setupBottomNav(_navController)
    }

    override fun onDestroy() {
        _uiStateJob?.cancel()
        _binding = null
        _navController = null
        _appBarConfiguration = null
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        _binding?.let {
            it.mainToolbar.menu.findItem(R.id.day_night_theme).title =
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
//                                dark mode is on
                        getString(R.string.light_mode)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
//                                light mode is on
                        getString(R.string.dark_mode)
                    }
                    else -> ""
                }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.searchFragment -> {
                return if (_navController != null) {
                    item.onNavDestinationSelected(_navController!!)
                } else {
                    true
                }
            }
            R.id.day_night_theme -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
//                                dark mode is on turned to light mode
                        viewModel.setIsNightMode(false)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
//                                light mode is on turned to dark mode
                        viewModel.setIsNightMode(true)
                    }
                }
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setupBottomNav(navController: NavController?) {
        _binding?.let {
            if (navController != null) {
                it.mainBottomNavigationView.setupWithNavController(navController)
            }
            it.mainBottomNavigationView.setOnNavigationItemReselectedListener {
                //https://stackoverflow.com/a/60483719/5595098
            }
        }
    }

    private fun setupToolbar(
        navController: NavController?,
        appBarConfiguration: AppBarConfiguration?
    ) {
        _binding?.let {
            it.mainToolbar.title = ""
            setSupportActionBar(it.mainToolbar)
            supportActionBar?.title = ""
            if (navController != null && appBarConfiguration != null) {
                it.mainToolbar.setupWithNavController(navController, appBarConfiguration)
            }
        }
    }

    private fun setupDayNightMode() {
        _uiStateJob = lifecycleScope.launchWhenCreated {
            viewModel.isNightMode.collect { isNightMode ->
                if (isNightMode) {
//                  turned to dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
//                  turned to light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}