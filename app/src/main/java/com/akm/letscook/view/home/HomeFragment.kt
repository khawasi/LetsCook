package com.akm.letscook.view.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.akm.letscook.R
import com.akm.letscook.databinding.FragmentHomeBinding
import com.akm.letscook.databinding.LayoutToolbarBinding
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.util.Resource
import com.akm.letscook.view.MainViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//https://developer.android.com/training/animation/reveal-or-hide-view#Crossfade

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private var _toolbarBinding: LayoutToolbarBinding? = null
    private var _uiStateJob: Job? = null

    private var shortAnimationDuration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _toolbarBinding = _binding!!.homeToolbar

        _binding!!.homeCardView.visibility = View.GONE

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        return _binding!!.root
    }

    private fun setRecommendedMeal() {
        _uiStateJob = lifecycleScope.launch {
            homeViewModel.meal.collect { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        Log.v("FRAGMENT", "LOADING")
                    }

                    Resource.Status.SUCCESS -> {
                        Log.v("FRAGMENT", "SUCCESS 1")

                        val meal = resource.data!!
                        setCardView(meal)
                        showCardView()
                        hideProgressBar()

                    }

                    Resource.Status.ERROR -> {
                        Log.v("FRAGMENT", "ERROR")
                        _binding?.let {
                            Snackbar.make(it.root, resource.message!!, Snackbar.LENGTH_LONG).show()
                        }
                        hideProgressBar()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(view)

        setRecommendedMeal()

        activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation_view)?.visibility =
            View.VISIBLE
    }

    private fun setToolbar(view: View) {
        _toolbarBinding?.let {
            val navController = findNavController()
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            it.root.setupWithNavController(navController, appBarConfiguration)
            it.root.inflateMenu(R.menu.menu_toolbar)
            it.root.menu.findItem(R.id.day_night_theme).title =
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
            it.root.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_graph_search -> {
                        view.findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToNavigationGraphSearch()
                        )
                        true
                    }
                    R.id.day_night_theme -> {
                        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                            Configuration.UI_MODE_NIGHT_YES -> {
//                                dark mode is on turned to light mode
                                mainViewModel.setIsNightMode(false)
                            }
                            Configuration.UI_MODE_NIGHT_NO -> {
//                                light mode is on turned to dark mode
                                mainViewModel.setIsNightMode(true)
                            }
                        }
                        true
                    }
                    else -> false
                }
            }
            it.root.title = getString(R.string.app_name)
        }
    }

    override fun onDestroyView() {
        _binding = null
        _toolbarBinding = null
        _uiStateJob?.cancel()
        super.onDestroyView()
    }

    private fun setCardView(meal: Meal) {
        _binding?.let {
            it.homeTextViewName.text = meal.name
            it.homeImageViewThumbnail.apply {
                Glide.with(this).load(meal.thumbnailUrl).into(this)
                transitionName = meal.id.toString()
            }
            it.homeConstraintLayout.setOnClickListener { view ->


                val extras = FragmentNavigatorExtras(
                    it.homeImageViewThumbnail to meal.id.toString()
                )

                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragmentInHome(
                    mealId = meal.id,
                    savedDate = meal.lastAccessed,
                    mealName = meal.name
                )
                view.findNavController().navigate(action, extras)
            }
        }
    }

    private fun showCardView() {
        _binding?.let {
            it.homeCardView.apply {
                // Set the content view to 0% opacity but visible, so that it is visible
                // (but fully transparent) during the animation.
                alpha = 0f
                visibility = View.VISIBLE

                // Animate the content view to 100% opacity, and clear any animation
                // listener set on the view.
                animate()
                    .alpha(1f)
                    .setListener(null)
                    .duration = shortAnimationDuration.toLong()

            }
        }
    }

    private fun hideProgressBar() {
        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        _binding?.let {
            it.homeShimmerLayout.animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        it.homeShimmerLayout.visibility = View.GONE
                    }
                })
                .duration = shortAnimationDuration.toLong()
        }
    }

}