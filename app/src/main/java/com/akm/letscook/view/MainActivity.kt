package com.akm.letscook.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.akm.letscook.R
import com.akm.letscook.databinding.ActivityMainBinding
import com.akm.letscook.util.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var _uiStateJob: Job? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var currentNavController: LiveData<NavController>

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_LetsCook)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        appBarConfiguration = AppBarConfiguration(binding.mainBottomNavigationView.menu)

//        setupToolbar(navController, appBarConfiguration)
        if(savedInstanceState==null) {
            setupBottomNav()
            binding.mainBottomNavigationView.selectedItemId = R.id.navigation_graph_home
        }

        setupDayNightMode()

        setContentView(binding.root)
    }

    override fun onDestroy() {
        _uiStateJob?.cancel()
        super.onDestroy()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNav()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController.value?.navigateUp() ?: false
    }

    private fun setupBottomNav() {
        val navGraphIds =
            listOf(
                R.navigation.navigation_graph_category,
                R.navigation.navigation_graph_home,
                R.navigation.navigation_graph_favorite
            )
        val controller = binding.mainBottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.main_fragment_container_view,
            intent = intent
        )
        currentNavController = controller
    }

    private fun setupDayNightMode(){
        _uiStateJob = lifecycleScope.launchWhenStarted {
            viewModel.isNightMode.collect { isNightMode ->
                if(isNightMode){
//                  turned to dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else{
//                  turned to light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}