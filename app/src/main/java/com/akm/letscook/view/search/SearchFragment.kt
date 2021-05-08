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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akm.letscook.NavigationGraphDirections
import com.akm.letscook.databinding.FragmentSearchBinding
import com.akm.letscook.util.Resource
import com.akm.letscook.view.rvadapter.MealListAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        _binding!!.searchTextInputLayout.setStartIconOnClickListener {
            findNavController().popBackStack()
        }

        _binding!!.searchTextInputEditText.doAfterTextChanged {
            viewModel.setQuery(it.toString())
        }
        listenToSearch()
        showSearchResult()
        listenToGoToDetail()

        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listenToSearch() {
        lifecycleScope.launchWhenStarted {
            viewModel.query.collect {
                viewModel.searchMealsByName(it)
            }
        }
    }

    private fun showSearchResult() {

        val adapter = MealListAdapter(
            MealListAdapter.OnClickListener {
                lifecycleScope.launchWhenStarted {
                    viewModel.setMealForDetail(it)
                }
            }
        )

        _binding?.let {
            it.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            it.searchRecyclerView.adapter = adapter
        }


        lifecycleScope.launchWhenStarted {
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
                        _binding?.let{
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

    private fun listenToGoToDetail() {
        lifecycleScope.launchWhenCreated {
            viewModel.meal.collect { meal ->
                if (meal != null) {
                    this@SearchFragment.findNavController().navigate(
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