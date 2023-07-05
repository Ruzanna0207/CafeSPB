package com.cafe.cafespb.categories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cafe.cafespb.R
import com.cafe.cafespb.databinding.ForAdapterCategoriesButtonsBinding

//установлен слушатель нажа-й в виде лямбда выраж-я
class ButtonsAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<ButtonsAdapter.ViewHolder>() {

    var item = listOf<String>()
        set(value) {
            val distinctItems = value.distinct()
            field = distinctItems
            notifyDataSetChanged()
        }
    private var selectedItemPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ForAdapterCategoriesButtonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = item[position]
        holder.bind(currentItem)

        //условия для создания кнопок если они нажаты и не нажаты
        val context = holder.itemView.context
        val textColor = when (selectedItemPosition) {
            position -> ContextCompat.getColor(context, R.color.white)
            else -> ContextCompat.getColor(context, R.color.black)
        }
        holder.binding.textButton.setTextColor(textColor)

        if (selectedItemPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.ic_purple_button)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.ic_buttons)
        }

        //слушатель нажатий для кнопок
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
            setSelectedItem(position)
        }
    }

    override fun getItemCount(): Int = item.size

    inner class ViewHolder(val binding: ForAdapterCategoriesButtonsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dish: String) {
            binding.textButton.text = dish
        }
    }

    //усл-я для выбранной позиции
    private fun setSelectedItem(position: Int) {
        val previousSelectedItem = selectedItemPosition
        selectedItemPosition = position

        if (previousSelectedItem != RecyclerView.NO_POSITION) {
            notifyItemChanged(previousSelectedItem)
        }
        if (selectedItemPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedItemPosition)
        }
    }
}
