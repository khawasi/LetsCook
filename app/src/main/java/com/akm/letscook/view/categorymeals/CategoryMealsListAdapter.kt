package com.akm.letscook.view.categorymeals

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akm.letscook.R
import com.akm.letscook.databinding.ItemListCardBinding
import com.akm.letscook.model.domain.Meal
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class CategoryMealsListAdapter(
    private val onItemClicked: (Meal) -> Unit
) : ListAdapter<Meal, CategoryMealsListAdapter.ViewHolder>(DiffCallback) {

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
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal) =
            oldItem.id == newItem.id

    }

    inner class ViewHolder(
        private var binding: ItemListCardBinding,
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

        fun bind(meal: Meal) {
            currentMeal = meal
            binding.itemImageView.apply {
                Glide.with(this)
                    .load(meal.thumbnailUrl)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_outline_24))
                    .into(this)
            }
            binding.itemTextView.text = meal.name
        }

    }
}