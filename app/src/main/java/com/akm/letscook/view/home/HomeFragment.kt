package com.akm.letscook.view.home

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
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import coil.load
import coil.transform.RoundedCornersTransformation
import com.akm.letscook.NavigationGraphDirections
import com.akm.letscook.R
import com.akm.letscook.databinding.FragmentHomeBinding
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.util.Resource
import com.akm.letscook.view.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//https://developer.android.com/training/animation/reveal-or-hide-view#Crossfade

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private var _uiStateJob: Job? = null

    private var shortAnimationDuration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        _binding!!.homeCardView.visibility = View.GONE

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)

        setRecommendedMeal()
    }

    override fun onDestroyView() {
        _binding = null
        _uiStateJob?.cancel()
        super.onDestroyView()
    }

    private fun setRecommendedMeal(){
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

    private fun setCardView(meal: Meal){
//        var memoryCacheKey = ""
        _binding?.let {
            it.homeTextViewName.text = meal.name
            it.homeImageViewThumbnail.apply {
                load(meal.thumbnailUrl) {
                    allowHardware(false)
                    transformations(RoundedCornersTransformation(4f, 4f, 4f, 4f))
                }
                transitionName = meal.id.toString()
//                            memoryCacheKey = metadata!!.memoryCacheKey.toString()
            }
            it.homeLinearLayout.setOnClickListener { view ->

//                memoryCacheKey =
//                    it.homeImageViewThumbnail.metadata!!.memoryCacheKey.toString()

                val extras = FragmentNavigatorExtras(
                    it.homeImageViewThumbnail to meal.id.toString()
                )

                val action = NavigationGraphDirections.actionGlobalDetailFragment(
                    meal.id,
                    meal.lastAccessed
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
                .setListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        it.homeShimmerLayout.visibility = View.GONE
                    }
                })
                .duration = shortAnimationDuration.toLong()
        }
    }

}