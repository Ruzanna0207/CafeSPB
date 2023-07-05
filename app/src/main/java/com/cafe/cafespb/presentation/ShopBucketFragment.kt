package com.cafe.cafespb.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.cafe.cafespb.R
import com.cafe.cafespb.databinding.FragmentShopBucketBinding
import com.cafe.cafespb.location_helper_classes.PermissionHelper
import com.cafe.cafespb.shop_bucket.adapter.ShopBucketAdapter
import com.cafe.cafespb.view_models.ActualShopListViewModel
import com.cafe.core.data_classes.Dishes

class ShopBucketFragment : Fragment() {
    private lateinit var binding: FragmentShopBucketBinding
    private lateinit var shopBucketAdapter: ShopBucketAdapter
    private lateinit var permissionHelper: PermissionHelper
    private var shopList = mutableListOf<Dishes>()

    private val actualShops: ActualShopListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBucketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        addShopList()
    }

    override fun onResume() {
        super.onResume()
        getLocation()
        updateShops() //проверка актульных значений
    }

    //опред-ие основного ui
    private fun setupViews() {
        actualShops.loadDate()
        actualShops.loadCity(requireActivity())

        val userPhoto = "https://i.pinimg.com/564x/a2/fd/e6/a2fde6047d99afbbaa852db5320d6639.jpg"
        Glide.with(this)
            .load(userPhoto)
            .circleCrop()
            .into(binding.photoMain)
    }

    //загрузка данных для корзины из предыдущего фрагмента, сохран-е во view model
    private fun addShopList() {
        actualShops.sharedList.observe(viewLifecycleOwner) { shops ->

            shopBucketAdapter = ShopBucketAdapter(actualShops, binding.payIt) { dish ->
                seeMore(dish)
            }

            shopBucketAdapter.kitchens = shopList
            binding.shopBbuscetRecView.adapter = shopBucketAdapter

            shopList.clear()
            shopList.addAll(shops)
            shopBucketAdapter.notifyDataSetChanged()
            checkShops()
        }
    }

    //условия для отображения ui в корзине
    private fun checkShops() {
        if (shopList.isEmpty()) {
            binding.shopBbuscetRecView.visibility = View.GONE
            binding.shopBusketImage.visibility = View.VISIBLE
            binding.shopBusketText.visibility = View.VISIBLE
            binding.payIt.visibility = View.GONE
        } else {
            binding.shopBbuscetRecView.visibility = View.VISIBLE
            binding.shopBusketImage.visibility = View.GONE
            binding.shopBusketText.visibility = View.GONE
            binding.payIt.visibility = View.VISIBLE

            binding.payIt.text = shopBucketAdapter.showTotalPrice().toString()
        }
    }

    //обновление текущ-го списка покупок
    private fun updateShops() {
        checkShops()
        actualShops.sharedList.observe(viewLifecycleOwner) {dishes ->
            shopBucketAdapter.kitchens = dishes.toMutableList()
            shopBucketAdapter.notifyDataSetChanged()
            checkShops()
        }
    }

    private fun getLocation() {
        actualShops.city.observe(viewLifecycleOwner) { city ->
            binding.city.text = city
            Log.i("loc", city)
        }
    }

    //всплывающее окно для просмотра фото и доп.информации
    private fun seeMore(dishes: Dishes) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_shop_bucket, null)
        val image = dialogView.findViewById<ImageView>(R.id.image_dish_shop)
        val textDish = dialogView.findViewById<TextView>(R.id.text_dish_shop)
        val price = dialogView.findViewById<TextView>(R.id.price_shop)
        val weight = dialogView.findViewById<TextView>(R.id.weight_shop)
        val buttonBack = dialogView.findViewById<ImageView>(R.id.back_to_dialog_shop)

        textDish.text = dishes.name
        "${dishes.price.toString()} ₽".also { price.text = it }
        "${dishes.weight.toString()}г".also { weight.text = it }

        Glide.with(this)
            .load(dishes.imageUrl)
            .optionalCenterCrop()
            .centerInside()
            .into(image)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        buttonBack.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.onRequestPermissionsResult(requestCode, grantResults)
    }

    companion object {
        fun newInstance4() = ShopBucketFragment()
    }
}