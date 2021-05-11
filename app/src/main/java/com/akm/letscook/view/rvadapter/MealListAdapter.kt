package com.akm.letscook.view.rvadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akm.letscook.R
import com.akm.letscook.databinding.ItemListVerticalBinding
import com.akm.letscook.model.domain.Meal

class MealListAdapter(private val onItemClicked: (Meal) -> Unit) :
    ListAdapter<Meal, MealListAdapter.MealViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder =
        MealViewHolder(
            ItemListVerticalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClicked
        )

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bindMeal(meal)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean =
            oldItem == newItem
    }

    inner class MealViewHolder(
        private var binding: ItemListVerticalBinding,
        val onItemClicked: (Meal) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentMeal: Meal? = null

        init {
            itemView.setOnClickListener {
                currentMeal?.let {
                    onItemClicked(it)
                }
            }
        }

        fun bindMeal(meal: Meal) {
            currentMeal = meal
            binding.apply {
                itemImageView.load(meal.thumbnailUrl) {
                    error(
                        ContextCompat.getDrawable(
                            binding.itemImageView.context,
                            R.drawable.ic_baseline_error_outline_24
                        )
                    )
                }
                itemTextView.text = meal.name
            }
        }

    }
}