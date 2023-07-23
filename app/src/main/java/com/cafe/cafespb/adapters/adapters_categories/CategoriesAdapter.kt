package com.cafe.cafespb.adapters.adapters_categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cafe.cafespb.databinding.ForAdapterCategoriesDishesBinding
import com.cafe.core.data_classes.Dishes

//установлен слушатель нажа-й в виде лямбда выраж-я
class CategoriesAdapter(private val onItemClick: (Dishes) -> Unit) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    var kitchens = listOf<Dishes>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class CategoriesViewHolder(private val binding: ForAdapterCategoriesDishesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Dishes) {

            binding.cathegoriesText.text = property.name
            Glide.with(itemView.context)
                .load(property.imageUrl)
                .centerCrop()
                .centerInside()
                .into(binding.cathegoriesImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = ForAdapterCategoriesDishesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoriesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return kitchens.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(kitchens[position])
        holder.itemView.setOnClickListener { onItemClick(kitchens[position]) } //слушатель нажатий
    }
}