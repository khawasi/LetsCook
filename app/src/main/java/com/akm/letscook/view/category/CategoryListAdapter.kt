package com.akm.letscook.view.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akm.letscook.R
import com.akm.letscook.databinding.ItemListVerticalBinding
import com.akm.letscook.model.domain.Category
import com.bumptech.glide.Glide

class CategoryListAdapter(private val onItemClicked: (Category) -> Unit) :
    ListAdapter<Category, CategoryListAdapter.CategoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemListVerticalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClicked
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bindCategory(category)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem == newItem
    }

    inner class CategoryViewHolder(
        private var binding: ItemListVerticalBinding,
        onItemClicked: (Category) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentCategory: Category? = null

        init {
            itemView.setOnClickListener {
                currentCategory?.let {
                    onItemClicked(it)
                }
            }
        }

        fun bindCategory(category: Category) {
            currentCategory = category
            binding.itemImageView.apply {
                Glide.with(this)
                    .load(category.thumbnailUrl)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_outline_24))
                    .into(this)
            }
            binding.itemTextView.text = category.name
        }

    }
}