package com.akm.letscook.view.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akm.letscook.R
import com.akm.letscook.databinding.FragmentSearchBinding
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.util.Resource
import com.akm.letscook.view.rvadapter.MealListAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private var _uiContentJob: Job? = null
    private var _uiQueryJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation_view)?.visibility =
            View.GONE
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.let { binding ->

            binding.searchTextInputLayout.setStartIconOnClickListener {
                findNavController().navigateUp()
            }

            binding.searchTextInputEditText.doAfterTextChanged {
                viewModel.setQuery(it.toString())
            }

        }

        listenToSearch()
        showSearchResult()
    }

    override fun onDestroyView() {
        _binding = null
        _uiContentJob?.cancel()
        _uiQueryJob?.cancel()
        super.onDestroyView()
    }

    private fun listenToSearch() {
        _uiQueryJob = lifecycleScope.launchWhenStarted {
            viewModel.query.collect {
                viewModel.searchMealsByName(it)
            }
        }
    }

    private fun showSearchResult() {

        val adapter = MealListAdapter { meal ->
            goToDetail(meal)
        }

        _binding?.let {
            it.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            it.searchRecyclerView.adapter = adapter
        }

        _uiContentJob = lifecycleScope.launchWhenStarted {
            viewModel.meals.collect { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        Log.v("SEARCH", "LOADING")
                    }
                    Resource.Status.SUCCESS -> {
                        Log.v("SEARCH", "SUCCESS")

                        val meals = resource.data!!

                        adapter.submitList(meals)

                    }
                    Resource.Status.ERROR -> {
                        _binding?.let {
                            Snackbar.make(it.root, resource.message!!, Snackbar.LENGTH_LONG).show()
                        }
                        val meals = resource.data!!
                        if (meals.isNotEmpty() && meals[0].name.isNotEmpty()) {
                            adapter.submitList(meals)
                        }
                        Log.v("SEARCH", "ERRORR")
                    }
                }
            }
        }
    }

    private fun goToDetail(meal: Meal) {
        this.findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                mealId = meal.id,
                savedDate = meal.lastAccessed,
                mealName = meal.name
            )
        )
    }
}