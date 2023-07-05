package com.cafe.cafespb.main_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cafe.cafespb.databinding.ForAdapterMainPageBinding

class MainPageAdapter(
    private val kitchens: List<com.cafe.core.data_classes.Categories>,
    private val listener: Clickable
) :
    RecyclerView.Adapter<MainPageAdapter.MainPageViewHolder>() {

    class MainPageViewHolder(private val binding: ForAdapterMainPageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: com.cafe.core.data_classes.Categories, listener: Clickable) {

            binding.textKitchen.text = property.name

            Glide.with(itemView.context)
                .load(property.imageUrl)
                .optionalCenterCrop()
                .into(binding.imageKitchen)

            //слушатель нажатий
            itemView.setOnClickListener {
                listener.onClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainPageViewHolder {
        val binding = ForAdapterMainPageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainPageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return kitchens.size
    }

    override fun onBindViewHolder(holder: MainPageViewHolder, position: Int) {
        holder.bind(kitchens[position], listener)
    }
}

// интерфейс для обработки слушателя нажатий
interface Clickable {
    fun onClick()
}