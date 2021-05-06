package com.akm.letscook.view.category

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
import androidx.recyclerview.widget.RecyclerView
import com.akm.letscook.databinding.FragmentCategoryBinding
import com.akm.letscook.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private val viewModel: CategoryViewModel by viewModels()

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        setTheCategories()

        goToCategoryMeals()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goToCategoryMeals(){
        lifecycleScope.launchWhenStarted {
            Log.v("CATEGORY", "COLLECT")
            viewModel.category.collect {
                if(it != null) {
                    this@CategoryFragment.findNavController().navigate(
                        CategoryFragmentDirections.actionCategoryFragmentToCategoryMealsFragment(
                            categoryName = it.name,
                            savedDate = it.lastAccessed
                        )
                    )
                    viewModel.navigatedToCategoryMeals()
                }
            }
        }
    }

    private fun setTheCategories(){
        val adapter = CategoryListAdapter(
            CategoryListAdapter.OnClickListener{
                lifecycleScope.launchWhenStarted {
                    viewModel.displayCategoryMeals(it)
                }
            }
        )
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW

        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRecyclerView.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.categories.collect { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        Log.v("CATEGORY", "LOADING")
                    }
                    Resource.Status.SUCCESS -> {
                        Log.v("CATEGORY", "SUCCESS")
                        adapter.submitList(resource.data!!)
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                        val categories = resource.data!!
                        if(categories[0].name.isNotEmpty()){
                            adapter.submitList(categories)
                        }
                        Log.v("CATEGORY", "ERROR")
                    }
                }
            }
        }
    }

}