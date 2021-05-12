package com.akm.letscook.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.akm.letscook.R
import com.akm.letscook.databinding.ActivityMainBinding
import com.akm.letscook.util.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var _uiStateJob: Job? = null
    private var _binding: ActivityMainBinding? = null
    private var _appBarConfiguration: AppBarConfiguration? = null
    private var _currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_LetsCook)
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        _appBarConfiguration = AppBarConfiguration(_binding!!.mainBottomNavigationView.menu)

//        setupToolbar(navController, appBarConfiguration)
        if(savedInstanceState==null) {
            setupBottomNav()
            _binding!!.mainBottomNavigationView.selectedItemId = R.id.navigation_graph_home
        }

        setupDayNightMode()

        setContentView(_binding!!.root)
    }

    override fun onDestroy() {
        _binding = null
        _appBarConfiguration = null
        _currentNavController = null
        _uiStateJob?.cancel()
        super.onDestroy()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNav()
    }

    override fun onSupportNavigateUp(): Boolean {
        return _currentNavController?.value?.navigateUp() ?: false
    }

    private fun setupBottomNav() {
        val navGraphIds =
            listOf(
                R.navigation.navigation_graph_category,
                R.navigation.navigation_graph_home,
                R.navigation.navigation_graph_favorite
            )
        val controller = _binding?.mainBottomNavigationView?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.main_fragment_container_view,
            intent = intent
        )
        _currentNavController = controller
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