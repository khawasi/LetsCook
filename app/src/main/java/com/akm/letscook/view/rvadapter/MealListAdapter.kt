package com.akm.letscook.view.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akm.letscook.R
import com.akm.letscook.databinding.ItemListVerticalBinding
import com.akm.letscook.model.domain.Meal
import com.bumptech.glide.Glide

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
        onItemClicked: (Meal) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentMeal: Meal? = null

        init {
            itemView.setOnClickListener {
                currentMeal?.let { meal ->
                    onItemClicked(meal)
                }
            }
        }

        fun bindMeal(meal: Meal) {
            currentMeal = meal
            binding.itemImageView.apply {
                Glide.with(this)
                    .load(meal.thumbnailUrl)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_outline_24))
                    .into(this)
            }
            binding.itemImageView.transitionName = meal.thumbnailUrl
            binding.itemTextView.text = meal.name
        }

    }

}