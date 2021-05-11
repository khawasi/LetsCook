package com.akm.letscook.view.categorymeals

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.akm.letscook.R
import com.akm.letscook.databinding.FragmentCategoryMealsBinding
import com.akm.letscook.databinding.LayoutToolbarBinding
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.util.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CategoryMealsFragment : Fragment() {

    private val viewModel: CategoryMealsViewModel by viewModels()
    private val fragmentArgs: CategoryMealsFragmentArgs by navArgs()

    private var _binding: FragmentCategoryMealsBinding? = null
    private var _toolbarBinding: LayoutToolbarBinding? = null
    private var _uiStateJob: Job? = null

    private var shortAnimationDuration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryMealsBinding.inflate(inflater, container, false)
        _toolbarBinding = _binding!!.categoryMealsToolbar

        _binding!!.categoryMealsRecyclerView.visibility = View.GONE

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        setCategoryMeals()

        activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation_view)?.visibility =
            View.VISIBLE
    }

    override fun onDestroyView() {
        _binding = null
        _toolbarBinding = null
        _uiStateJob?.cancel()
        super.onDestroyView()
    }

    private fun setToolbar() {
        _toolbarBinding?.let {
            val navController = findNavController()
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            it.root.setupWithNavController(navController, appBarConfiguration)
            it.root.title = fragmentArgs.categoryName
        }
    }

    private fun setCategoryMeals() {

        val adapter = CategoryMealsListAdapter { meal ->
            goToDetail(meal)
        }

        _binding?.let {
            it.categoryMealsRecyclerView.apply {
                this.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                this.adapter = adapter
            }
        }

        _uiStateJob = lifecycleScope.launchWhenStarted {
            viewModel.meals.collect { resource ->
                when (resource.status) {

                    Resource.Status.LOADING -> {

                        Log.v("CMEALS", "LOADING")
                    }

                    Resource.Status.SUCCESS -> {
                        Log.v("CMEALS", "SUCCESS")
                        val meals = resource.data!!

                        adapter.submitList(meals)
                        showCategoryMeals()
                        hideProgressBar()
                    }

                    Resource.Status.ERROR -> {
                        _binding?.let {
                            Snackbar.make(it.root, resource.message!!, Snackbar.LENGTH_LONG).show()
                        }
                        Log.v("CMEALS", "ERROR")
                        hideProgressBar()
                    }
                }
            }
        }
    }

    private fun goToDetail(meal: Meal) {
        this.findNavController().navigate(
            CategoryMealsFragmentDirections.actionCategoryMealsFragmentToDetailFragmentInCategory(
                mealId = meal.id,
                savedDate = meal.lastAccessed,
                mealName = meal.name
            )
        )
    }

    private fun showCategoryMeals() {
        _binding?.let {
            it.categoryMealsRecyclerView.apply {
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
        _binding?.let {
            it.categoryMealsShimmerLayout.animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        it.categoryMealsShimmerLayout.visibility = View.GONE
                    }
                })
                .duration = shortAnimationDuration.toLong()
        }
    }

}