package com.akm.letscook.view.detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
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
import coil.load
import com.akm.letscook.R
import com.akm.letscook.databinding.FragmentDetailBinding
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()

    private var _binding: FragmentDetailBinding? = null
    private var _uiContentJob: Job? = null
    private var _uiFavJob: Job? = null
    private var shortAnimationDuration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()

        initializeViewVisibility()

        val adapter = DetailIngredientsListAdapter()

        setDetail(adapter)
        updateFavorite()

    }

    override fun onDestroyView() {
        _binding = null
        _uiContentJob?.cancel()
        _uiFavJob?.cancel()
        super.onDestroyView()
    }

    private fun setToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        _binding?.let {
            it.detailToolbar.setupWithNavController(navController, appBarConfiguration)
            it.detailToolbar.menu.findItem(R.id.detail_action_favorite)
                .setOnMenuItemClickListener {
                    Log.v("DETAIL FAVORITE", "TEST!!!!")
                    viewModel.clickFavoriteMeal()
                    true
                }
        }
    }

    private fun initializeViewVisibility() {
        _binding?.let {
            it.detailLinearLayout.visibility = View.GONE
            it.detailImageViewThumbnail.visibility = View.GONE
            it.detailCollapsingToolbarLayout.title = ""
            it.detailToolbar.title = ""
        }
    }

    private fun setDetail(adapter: DetailIngredientsListAdapter) {
        _uiContentJob = lifecycleScope.launchWhenStarted {
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
                        _binding?.let {
                            Snackbar.make(it.root, resource.message!!, Snackbar.LENGTH_LONG).show()
                        }
                        hideLoading()
                        Log.v("DETAIL", "ERROR")
                    }
                }
            }
        }
    }

    private fun bindMeal(meal: Meal, adapter: DetailIngredientsListAdapter) {
        _binding?.let {
            it.detailImageViewThumbnail.load(meal.thumbnailUrl) {
                error(context?.let { it1 ->
                    ContextCompat.getDrawable(
                        it1,
                        R.drawable.ic_baseline_error_outline_24
                    )
                })
            }
//            {
//                allowHardware(false)
//                target(
//                    onSuccess = { result ->
//                        val bitmap = (result as BitmapDrawable).bitmap
//                        Palette.from(bitmap).generate { palette ->
//                            val vibrant: Palette.Swatch? = palette?.vibrantSwatch
//                            vibrant?.let { swatch ->
//                                val textTitle: Spannable = SpannableString(meal.name)
//                                textTitle.setSpan(
//                                    ForegroundColorSpan(swatch.titleTextColor),
//                                    0,
//                                    textTitle.length,
//                                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
//                                )
//                                it.detailCollapsingToolbarLayout.title = textTitle
//                            }
//                        }
//                        it.detailImageViewThumbnail.setImageDrawable(resultZ)
//                    }
//                )
//            }
            it.detailImageViewThumbnail.contentDescription = "Image For ${meal.name}"
            it.detailInstructionTextView.text = meal.instructions
            it.detailIngredientsRecyclerView.adapter = adapter
//            it.detailCollapsingToolbarLayout.title = meal.name
            it.detailToolbarTitleTextView.text = meal.name
            it.detailToolbarTitleTextView.isSelected = true
        }
        adapter.submitList(meal.ingredients)
    }

    private fun updateFavorite() {
        _uiFavJob = lifecycleScope.launchWhenStarted {
            viewModel.isFavorite.collect { isFavorite ->
                Log.v("DETAIL FAVORITE", "UPDATE!!!!")
                _binding?.let {
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

    private fun showContent() {
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

    private fun hideLoading() {
        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        _binding?.let {
            it.detailShimmerLayoutToolbar.animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        it.detailShimmerLayoutToolbar.visibility = View.GONE
                    }
                })
                .duration = shortAnimationDuration.toLong()

            it.detailShimmerLayoutContent.animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        it.detailShimmerLayoutContent.visibility = View.GONE
                    }
                })
                .duration = shortAnimationDuration.toLong()
        }
    }

}