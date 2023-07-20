package com.cafe.cafespb.shop_bucket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cafe.cafespb.databinding.ForAdapterShopBucketBinding
import com.cafe.cafespb.view_models.ActualShopListViewModel
import com.cafe.core.data_classes.Dishes

class ShopBucketAdapter(
    private val sharedViewModel: ActualShopListViewModel,
    private var payItButton: Button? = null, private val onDishClick: (Dishes) -> Unit
) : RecyclerView.Adapter<ShopBucketAdapter.ShopViewHolder>() {

    var kitchens = mutableListOf<Dishes>()

    inner class ShopViewHolder(private val binding: ForAdapterShopBucketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var num = 1 //знач-е для счетчика кол-ва товаров

        fun bind(property: Dishes) {
            binding.textBusket.text = property.name
            "${(property.price ?: 0)} ₽/ед".also { binding.priceBusket.text = it }
            "${property.weight.toString()}г".also { binding.weight.text = it }

            Glide.with(binding.root.context)
                .load(property.imageUrl)
                .centerInside()
                .into(binding.imageBusket)

            num = getQuantity(property)
            updatePayItText()

            //действия для кнопки +
            binding.plusButton.setOnClickListener {
                num++
                setQuantity(property, num)
                binding.numberText.text = num.toString()
                updatePayItText() //обновление цены
            }

            //действия для кнопки -
            binding.minusButton.setOnClickListener {
                if (num > 1) {
                    num--
                    setQuantity(property, num)
                    binding.numberText.text = num.toString()
                    updatePayItText()
                } else if (num == 1) {
                    removeElement(property)
                }
                updatePayItText() //обновление цены
            }

            //слушатель нажатий для изображения
            binding.imageBusket.setOnClickListener {
                onDishClick(property)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding = ForAdapterShopBucketBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val currentItem = kitchens[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return kitchens.size
    }

    //фун-я для удаления элемента из корзины
    fun removeElement(item: Dishes) {
        val position = kitchens.indexOf(item)
        if (position != -1) {
            kitchens.removeAt(position)
            notifyItemRemoved(position)
            sharedViewModel.removeShop(kitchens, item)
            updatePayItText()
        }
    }

    //фун-я для показа текущей цены
    fun showTotalPrice(): Int {
        var totalPrice = 0
        for (dish in kitchens) {
            val price = dish.price ?: 0
            val quantity = getQuantity(dish)
            totalPrice += quantity * price
        }
        return totalPrice
    }

    //фун-ии для подсчета кол-ва и цены
    private val dishQuantityMap = mutableMapOf<Dishes, Int>()

    private fun getQuantity(dish: Dishes): Int {
        return dishQuantityMap.getOrDefault(dish, 1)
    }

    private fun setQuantity(dish: Dishes, quantity: Int) {
        dishQuantityMap[dish] = quantity
    }

    //фун-я для выведения текущей цены
    fun updatePayItText() {
        payItButton?.let {
            it.text = "К оплате ${showTotalPrice()}₽"
        }
    }
}