package com.akm.letscook.view.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akm.letscook.databinding.ItemListIngredientBinding
import com.akm.letscook.model.domain.MealIngredient

class DetailIngredientsListAdapter:
    ListAdapter<MealIngredient, DetailIngredientsListAdapter.ViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemListIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = getItem(position)
        holder.bindIngredient(ingredient)
    }

    inner class ViewHolder(private val binding: ItemListIngredientBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bindIngredient(mealIngredient: MealIngredient){
            binding.ingredientNameTextView.text = mealIngredient.name
            binding.ingredientMeasurementTextView.text = mealIngredient.measurement
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<MealIngredient>(){
        override fun areItemsTheSame(oldItem: MealIngredient, newItem: MealIngredient) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MealIngredient, newItem: MealIngredient) =
            oldItem.name == newItem.name && oldItem.measurement == newItem.measurement

    }

}