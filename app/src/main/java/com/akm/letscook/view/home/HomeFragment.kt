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
import coil.metadata
import coil.transform.RoundedCornersTransformation
import com.akm.letscook.NavigationGraphDirections
import com.akm.letscook.R
import com.akm.letscook.databinding.FragmentHomeBinding
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.util.Resource
import com.akm.letscook.view.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//https://developer.android.com/training/animation/reveal-or-hide-view#Crossfade

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null

    private var shortAnimationDuration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)

        _binding!!.homeCardView.visibility = View.GONE

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        lifecycleScope.launch {
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


        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
//        binding.homeTextViewName.text = meal.name
//        binding.homeImageViewThumbnail.apply {
//            load(meal.thumbnailUrl) {
//                allowHardware(false)
//                transformations(RoundedCornersTransformation(4f, 4f, 4f, 4f))
//            }
//            transitionName = meal.id.toString()
////                            memoryCacheKey = metadata!!.memoryCacheKey.toString()
//        }
//
//        binding.homeLinearLayout.setOnClickListener { view ->
//
//            memoryCacheKey =
//                binding.homeImageViewThumbnail.metadata!!.memoryCacheKey.toString()
//
//            val extras = FragmentNavigatorExtras(
//                binding.homeImageViewThumbnail to meal.id.toString()
//            )
//
//            val action = NavigationGraphDirections.actionGlobalDetailFragment(
//                meal.id,
//                meal.lastAccessed
//            )
//            view.findNavController().navigate(action, extras)
//        }
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
//        binding.homeCardView.apply {
//            // Set the content view to 0% opacity but visible, so that it is visible
//            // (but fully transparent) during the animation.
//            alpha = 0f
//            visibility = View.VISIBLE
//
//            // Animate the content view to 100% opacity, and clear any animation
//            // listener set on the view.
//            animate()
//                .alpha(1f)
//                .setListener(null)
//                .duration = shortAnimationDuration.toLong()
//
//        }
    }

    private fun hideProgressBar() {
        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        _binding?.let {
            it.homeProgressBar.animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        it.homeProgressBar.visibility = View.GONE
                    }
                })
                .duration = shortAnimationDuration.toLong()
        }
//        binding.homeProgressBar.animate()
//            .alpha(0f)
//            .setListener(object : AnimatorListenerAdapter(){
//                override fun onAnimationEnd(animation: Animator?) {
//                    binding.homeProgressBar.visibility = View.GONE
//                }
//            })
//            .duration = shortAnimationDuration.toLong()
    }

}