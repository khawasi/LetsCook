package com.akm.letscook.view.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akm.letscook.NavigationGraphDirections
import com.akm.letscook.databinding.FragmentFavoriteBinding
import com.akm.letscook.util.Resource
import com.akm.letscook.view.rvadapter.MealListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FavoriteFragment: Fragment() {

    private val viewModel: FavoriteViewModel by viewModels()

    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        val adapter = MealListAdapter(
            MealListAdapter.OnClickListener {
                lifecycleScope.launchWhenStarted {
                    viewModel.setMealForDetail(it)
                }
            }
        )

        showFavoriteMeals(adapter)
        listenToGoToDetail()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showFavoriteMeals(adapter: MealListAdapter){

        binding.favoriteRecyclerView.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
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

                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                        Log.v("FAVORITE", "ERRORR")
                    }
                }
            }
        }
    }

    private fun listenToGoToDetail(){
        lifecycleScope.launchWhenCreated {
            viewModel.meal.collect { meal ->
                if (meal!=null){
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

}