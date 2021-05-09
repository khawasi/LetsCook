package com.akm.letscook.view.favorite

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.akm.letscook.NavigationGraphDirections
import com.akm.letscook.databinding.FragmentFavoriteBinding
import com.akm.letscook.util.Resource
import com.akm.letscook.view.MainActivity
import com.akm.letscook.view.rvadapter.MealListAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModels()

    private var _binding: FragmentFavoriteBinding? = null

    private var shortAnimationDuration: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        (activity as MainActivity).supportActionBar?.title = "Favorite"

        _binding!!.favoriteRecyclerView.visibility = View.GONE

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        val adapter = MealListAdapter {
            lifecycleScope.launchWhenStarted {
                viewModel.setMealForDetail(it)
            }
        }

        setFavoriteMeals(adapter)
        listenToGoToDetail()

        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFavoriteMeals(adapter: MealListAdapter) {

        _binding?.let { it ->
            it.favoriteRecyclerView.apply {
                this.layoutManager = LinearLayoutManager(requireContext())
                this.adapter = adapter
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.meals.collect { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        Log.v("FAVORITE", "LOADING")
                    }
                    Resource.Status.SUCCESS -> {
                        Log.v("FAVORITE", "SUCCESS")

                        val meals = resource.data!!

                        adapter.submitList(meals)
                        showFavoriteMeals()
                        hideProgressBar()

                    }
                    Resource.Status.ERROR -> {
                        _binding?.let {
                            Snackbar.make(it.root, resource.message!!, Snackbar.LENGTH_LONG).show()
                        }
                        Log.v("FAVORITE", "ERrOR")
                        hideProgressBar()
                    }
                }
            }
        }

    }

    private fun listenToGoToDetail() {
        lifecycleScope.launchWhenCreated {
            viewModel.meal.collect { meal ->
                if (meal != null) {
                    this@FavoriteFragment.findNavController().navigate(
                        NavigationGraphDirections.actionGlobalDetailFragment(
                            meal.id,
                            meal.lastAccessed
                        )
                    )
                    viewModel.navigatedToMealDetail()
                }
            }
        }
    }

    private fun showFavoriteMeals() {
        _binding?.let {
            it.favoriteRecyclerView.apply {
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
            it.favoriteProgressBar.animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        it.favoriteProgressBar.visibility = View.GONE
                    }
                })
                .duration = shortAnimationDuration.toLong()
        }
    }

}