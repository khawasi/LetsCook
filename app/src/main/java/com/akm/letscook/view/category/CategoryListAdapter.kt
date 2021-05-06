package com.akm.letscook.view.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.akm.letscook.databinding.ItemListVerticalBinding
import com.akm.letscook.model.domain.Category

class CategoryListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Category, CategoryListAdapter.CategoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemListVerticalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bindCategory(category)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(category)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem.id == newItem.id
                    && oldItem.name == newItem.name
                    && oldItem.lastAccessed == newItem.lastAccessed
                    && oldItem.thumbnailUrl == newItem.thumbnailUrl
    }

    inner class CategoryViewHolder(private var binding: ItemListVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindCategory(category: Category) {
            binding.itemImageView.load(category.thumbnailUrl)
            binding.itemTextView.text = category.name
        }

    }

    class OnClickListener(val clickListener: (category: Category) -> Unit) {
        fun onClick(category: Category) = clickListener(category)
    }
}