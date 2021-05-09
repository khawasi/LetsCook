package com.akm.letscook.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var currentNavController: LiveData<NavController>

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_LetsCook)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        appBarConfiguration = AppBarConfiguration(binding.mainBottomNavigationView.menu)

//        setupToolbar(navController, appBarConfiguration)
        setupBottomNav()

        setContentView(binding.root)
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
//        lifecycleScope.launchWhenStarted {
//            controller.collect {
//                it?.let{
//                    val appBarConfiguration = AppBarConfiguration(navGraphIds.toSet())
//                    NavigationUI.setupWithNavController(
//                        it,
//                        appBarConfiguration
//                    )
//                }
//            }
//        }
        currentNavController = controller
        binding.mainBottomNavigationView.selectedItemId = R.id.navigation_graph_home
    }

//    private fun setupToolbar(
//        navController: NavController,
//        appBarConfiguration: AppBarConfiguration
//    ) {
//        setSupportActionBar(binding.mainToolbar)
//        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)
//    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController.value?.navigateUp() ?: false
    }
}