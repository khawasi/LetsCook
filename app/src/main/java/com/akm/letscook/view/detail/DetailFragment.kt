package com.akm.letscook.view.detail

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
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()

    private var _binding: FragmentDetailBinding? = null

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

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        _binding!!.detailToolbar.setupWithNavController(navController, appBarConfiguration)
        _binding!!.detailToolbar.menu.findItem(R.id.detail_action_favorite)
            .setOnMenuItemClickListener {
                Log.v("DETAIL FAVORITE", "TEST!!!!")
                viewModel.clickFavoriteMeal()
                true
            }

        val adapter = DetailIngredientsListAdapter()

        setDetail(adapter)
        updateFavorite()

        return _binding!!.root
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

                    }

                    Resource.Status.ERROR -> {
                        _binding?.let{
                            Snackbar.make(it.root, resource.message!!, Snackbar.LENGTH_LONG).show()
                        }
                        Log.v("DETAIL", "ERROR")
                    }
                }
            }
        }
    }

    private fun bindMeal(meal: Meal, adapter: DetailIngredientsListAdapter){
        _binding?.let {
            it.detailImageViewThumbnail.load(meal.thumbnailUrl)
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

}