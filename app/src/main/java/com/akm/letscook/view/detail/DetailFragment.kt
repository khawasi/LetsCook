package com.akm.letscook.view.detail

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

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
        binding.detailToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.detailToolbar.menu.findItem(R.id.detail_action_favorite)
            .setOnMenuItemClickListener {
                Log.v("DETAIL FAVORITE", "TEST!!!!")
                viewModel.clickFavoriteMeal()
                true
            }

        val adapter = DetailIngredientsListAdapter()

        setDetail(adapter)
        updateFavorite()

        return binding.root
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
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                        val meal = resource.data!!
                        if(meal.name.isNotEmpty()){
                            bindMeal(meal, adapter)
                        }
                        Log.v("DETAIL", "ERROR")
                    }
                }
            }
        }
    }

    private fun bindMeal(meal: Meal, adapter: DetailIngredientsListAdapter){
        binding.detailImageViewThumbnail.load(meal.thumbnailUrl)
        binding.detailInstructionTextView.text = meal.instructions
        binding.detailIngredientsRecyclerView.adapter = adapter
        adapter.submitList(meal.ingredients)
    }

    private fun updateFavorite() {
        lifecycleScope.launchWhenStarted {
            viewModel.isFavorite.collect { isFavorite ->
                Log.v("DETAIL FAVORITE", "UPDATE!!!!")
                val favoriteItem = binding.detailToolbar.menu.findItem(R.id.detail_action_favorite)
                favoriteItem.icon =
                    if (isFavorite) {
                        Log.v("DETAIL FAVORITE", "UPDATE IF")
                        context?.let {
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.ic_round_favorite_filled_24
                            )
                        }
                    } else {
                        Log.v("DETAIL FAVORITE", "UPDATE ELSE")
                        context?.let {
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.ic_baseline_favorite_border_24
                            )
                        }
                    }
            }
        }
    }

}