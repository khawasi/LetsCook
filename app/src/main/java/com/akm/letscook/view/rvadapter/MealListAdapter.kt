package com.akm.letscook.view.rvadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akm.letscook.databinding.ItemListVerticalBinding
import com.akm.letscook.model.domain.Meal

class MealListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Meal, MealListAdapter.MealViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder =
        MealViewHolder(
            ItemListVerticalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bindMeal(meal)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(meal)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean =
            oldItem.id == newItem.id
    }

    inner class MealViewHolder(private var binding: ItemListVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindMeal(meal: Meal) {
            binding.itemImageView.load(meal.thumbnailUrl)
            binding.itemTextView.text = meal.name
        }

    }

    class OnClickListener(val clickListener: (meal: Meal) -> Unit) {
        fun onClick(meal: Meal) = clickListener(meal)
    }

}