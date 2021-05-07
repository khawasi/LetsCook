package com.akm.letscook.view.categorymeals

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.akm.letscook.NavigationGraphDirections
import com.akm.letscook.databinding.FragmentCategoryMealsBinding
import com.akm.letscook.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CategoryMealsFragment: Fragment() {

    private val viewModel: CategoryMealsViewModel by viewModels()

    private var _binding : FragmentCategoryMealsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryMealsBinding.inflate(inflater, container, false)

        showCategoryMeals()

        listenToGoToDetail()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showCategoryMeals(){
        val adapter = CategoryMealsListAdapter(
            CategoryMealsListAdapter.OnClickListener { meal ->
                lifecycleScope.launchWhenCreated {
                    viewModel.setMealForDetail(meal)
                }
            }
        )

        binding.categoryMealsRecyclerView.let {
            it.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            it.adapter = adapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.meals.collect { resource ->
                when(resource.status){

                    Resource.Status.LOADING -> {

                        Log.v("CMEALS", "LOADING")
                    }

                    Resource.Status.SUCCESS -> {
                        Log.v("CMEALS", "SUCCESS")
                        val meals = resource.data!!

                        adapter.submitList(meals)
                    }

                    Resource.Status.ERROR -> {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                        val meals = resource.data!!
                        if(meals[0].name.isNotEmpty()){
                            adapter.submitList(meals)
                        }
                        Log.v("CMEALS", "ERROR")
                    }
                }
            }
        }
    }

    private fun listenToGoToDetail(){
        lifecycleScope.launchWhenCreated {
            viewModel.meal.collect { meal ->
                if (meal!=null){
                    this@CategoryMealsFragment.findNavController().navigate(
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