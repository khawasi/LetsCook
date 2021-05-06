package com.akm.letscook.view.categorymeals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akm.letscook.databinding.ItemListCardBinding
import com.akm.letscook.model.domain.Meal

class CategoryMealsListAdapter(val onClickListener: OnClickListener) :
    ListAdapter<Meal, CategoryMealsListAdapter.ViewHolder>(DiffCalback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemListCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bind(meal)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(meal)
        }
    }

    companion object DiffCalback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal) =
            oldItem.id == newItem.id

    }

    inner class ViewHolder(private var binding: ItemListCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: Meal) {
            binding.itemImageView.load(meal.thumbnailUrl)
            binding.itemTextView.text = meal.name
        }

    }

    class OnClickListener(val clickListener: (meal: Meal) -> Unit) {
        fun onClick(meal: Meal) = clickListener(meal)
    }

}