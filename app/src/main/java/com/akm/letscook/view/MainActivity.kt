package com.akm.letscook.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.akm.letscook.R
import com.akm.letscook.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_LetsCook);
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_fragment_container_view) as NavHostFragment? ?: return
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            binding.mainToolbar.visibility = View.VISIBLE

            if(binding.mainToolbar.menu.findItem(R.id.searchFragment) != null) {
                binding.mainToolbar.menu.findItem(R.id.searchFragment).isVisible = true
            }

            if(destination.id == R.id.categoryFragment || destination.id == R.id.homeFragment || destination.id == R.id.favoriteFragment){
                binding.mainBottomNavigationView.visibility = View.VISIBLE
            }
            else{
                if(destination.id == R.id.searchFragment || destination.id == R.id.detailFragment){
                    binding.mainToolbar.visibility = View.GONE
                }
                else{
                    binding.mainToolbar.menu.findItem(R.id.searchFragment).isVisible = false
                }
                binding.mainBottomNavigationView.visibility = View.GONE
            }
        }

        appBarConfiguration = AppBarConfiguration(binding.mainBottomNavigationView.menu)

        setupToolbar(navController, appBarConfiguration)
        setupBottomNav(navController)

        setContentView(binding.root)
    }

    private fun setupBottomNav(navController: NavController) {
        binding.mainBottomNavigationView.setupWithNavController(navController)
        binding.mainBottomNavigationView.setOnNavigationItemReselectedListener {
            //https://stackoverflow.com/a/60483719/5595098
        }
    }

    private fun setupToolbar(
        navController: NavController,
        appBarConfiguration: AppBarConfiguration
    ) {
        setSupportActionBar(binding.mainToolbar)
        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
}