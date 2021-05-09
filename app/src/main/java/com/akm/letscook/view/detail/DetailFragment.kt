package com.akm.letscook.view.detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.akm.letscook.R
import com.akm.letscook.databinding.FragmentDetailBinding
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.util.Resource
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()

    private var _binding: FragmentDetailBinding? = null
    private var shortAnimationDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation_view)?.visibility = View.GONE

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        _binding!!.detailToolbar.setupWithNavController(navController, appBarConfiguration)
        _binding!!.detailToolbar.menu.findItem(R.id.detail_action_favorite)
            .setOnMenuItemClickListener {
                Log.v("DETAIL FAVORITE", "TEST!!!!")
                viewModel.clickFavoriteMeal()
                true
            }

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding!!.detailLinearLayout.visibility = View.GONE
        _binding!!.detailImageViewThumbnail.visibility = View.GONE
        _binding!!.detailCollapsingToolbarLayout.title = ""
        _binding!!.detailToolbar.title = ""


        val adapter = DetailIngredientsListAdapter()

        setDetail(adapter)
        updateFavorite()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDetail(adapter: DetailIngredientsListAdapter){
        lifecycleScope.launchWhenStarted {
            viewModel.meal.collect { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        Log.v("DETAIL", "LOADING")
                    }

                    Resource.Status.SUCCESS -> {
                        Log.v("DETAIL", "SUCCESS 1")

                        val meal = resource.data!!
                        bindMeal(meal, adapter)
                        showContent()
                        hideLoading()
                    }

                    Resource.Status.ERROR -> {
                        _binding?.let{
                            Snackbar.make(it.root, resource.message!!, Snackbar.LENGTH_LONG).show()
                        }
                        hideLoading()
                        Log.v("DETAIL", "ERROR")
                    }
                }
            }
        }
    }

    private fun bindMeal(meal: Meal, adapter: DetailIngredientsListAdapter){
        _binding?.let {
            it.detailImageViewThumbnail.apply {
                Glide.with(this)
                    .load(meal.thumbnailUrl)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_outline_24))
                    .into(this)
            }
            it.detailImageViewThumbnail.contentDescription = "Image For ${meal.name}"
            it.detailInstructionTextView.text = meal.instructions
            it.detailIngredientsRecyclerView.adapter = adapter
            it.detailCollapsingToolbarLayout.title = meal.name
        }
        adapter.submitList(meal.ingredients)
    }

    private fun updateFavorite() {
        lifecycleScope.launchWhenStarted {
            viewModel.isFavorite.collect { isFavorite ->
                Log.v("DETAIL FAVORITE", "UPDATE!!!!")
                _binding?.let{
                    val favoriteItem = it.detailToolbar.menu.findItem(R.id.detail_action_favorite)
                    favoriteItem.icon =
                        if (isFavorite) {
                            Log.v("DETAIL FAVORITE", "UPDATE IF")
                            context?.let { context ->
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_round_favorite_filled_24
                                )
                            }
                        } else {
                            Log.v("DETAIL FAVORITE", "UPDATE ELSE")
                            context?.let { context ->
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_baseline_favorite_border_24
                                )
                            }
                        }
                }
            }
        }
    }

    private fun showContent(){
        _binding?.let {
            it.detailImageViewThumbnail.apply {
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
            it.detailLinearLayout.apply {
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

    private fun hideLoading(){
        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        _binding?.let {
            it.detailShimmerLayoutToolbar.animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        it.detailShimmerLayoutToolbar.visibility = View.GONE
                    }
                })
                .duration = shortAnimationDuration.toLong()

            it.detailShimmerLayoutContent.animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        it.detailShimmerLayoutContent.visibility = View.GONE
                    }
                })
                .duration = shortAnimationDuration.toLong()
        }
    }

}