package com.akm.letscook.view.categorymeals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akm.letscook.R
import com.akm.letscook.databinding.ItemListCardBinding
import com.akm.letscook.model.domain.Meal

class CategoryMealsListAdapter(private val onItemClicked: (Meal) -> Unit) :
    ListAdapter<Meal, CategoryMealsListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemListCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClicked
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bind(meal)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal) =
            oldItem == newItem

    }

    inner class ViewHolder(private var binding: ItemListCardBinding,
                           private val onItemClicked: (Meal) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentMeal: Meal? = null

        init {
            itemView.setOnClickListener {
                currentMeal?.let(onItemClicked)
            }
        }

        fun bind(meal: Meal) {
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