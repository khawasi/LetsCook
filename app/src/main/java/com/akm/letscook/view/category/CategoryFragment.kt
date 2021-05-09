package com.akm.letscook.view.category

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akm.letscook.R
import com.akm.letscook.databinding.FragmentCategoryBinding
import com.akm.letscook.databinding.LayoutToolbarBinding
import com.akm.letscook.util.Resource
import com.akm.letscook.view.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private val viewModel: CategoryViewModel by viewModels()

    private var _binding: FragmentCategoryBinding? = null
    private var _toolbarBinding: LayoutToolbarBinding? = null

    private var shortAnimationDuration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        _toolbarBinding = _binding!!.categoryToolbar

        (activity as MainActivity).supportActionBar?.title = "Category"

        _binding!!.categoryRecyclerView.visibility = View.GONE

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        setTheCategories()

        goToCategoryMeals()

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _toolbarBinding?.let {
            val navController = findNavController()
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            it.root.setupWithNavController(navController, appBarConfiguration)
            it.root.inflateMenu(R.menu.menu_toolbar)
            it.root.menu.findItem(R.id.navigation_graph_search)
                .setOnMenuItemClickListener {
                    view.findNavController().navigate(
                        CategoryFragmentDirections.actionCategoryFragmentToNavigationGraphSearch()
                    )
                    true
                }
            it.root.title = getString(R.string.category)
        }
        activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation_view)?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
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
        val adapter = CategoryListAdapter{
            lifecycleScope.launchWhenStarted {
                viewModel.displayCategoryMeals(it)
            }
        }

        _binding?.let {
            it.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            it.categoryRecyclerView.adapter = adapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.categories.collect { resource ->
                when (resource.status) {
                    Resource.Status.LOADING -> {
                        Log.v("CATEGORY", "LOADING")
                    }
                    Resource.Status.SUCCESS -> {
                        Log.v("CATEGORY", "SUCCESS")
                        adapter.submitList(resource.data!!)
                        showCategories()
                        hideProgressBar()
                    }
                    Resource.Status.ERROR -> {
                        _binding?.let {
                            Snackbar.make(it.root, resource.message!!, Snackbar.LENGTH_LONG).show()
                        }
                        Log.v("CATEGORY", "ERROR")
                        hideProgressBar()
                    }
                }
            }
        }
    }


    private fun showCategories() {
        _binding?.let {
            it.categoryRecyclerView.apply {
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

    private fun hideProgressBar() {
        _binding?.let {
            it.categoryShimmerLayout.animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        it.categoryShimmerLayout.visibility = View.GONE
                    }
                })
                .duration = shortAnimationDuration.toLong()
        }
    }

}