package com.cafe.cafespb.presentation.categories

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cafe.cafespb.R
import com.cafe.cafespb.adapters.adapters_categories.ButtonsAdapter
import com.cafe.cafespb.adapters.adapters_categories.CategoriesAdapter
import com.cafe.cafespb.databinding.FragmentCategoriesBinding
import com.cafe.cafespb.view_models.ActualShopListViewModel
import com.cafe.cafespb.view_models.CategoriesViewModel
import com.cafe.core.data_classes.Dishes
import kotlinx.coroutines.cancelChildren


class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var buttonsAdapter: ButtonsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter


    private var listDishesCategories = mutableListOf<Dishes>()
    private var filteredTagsForSearch = mutableListOf<Dishes>()

    private val categoriesViewModel: CategoriesViewModel by viewModels()

    private val shopListModel: ActualShopListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        fetchCategories()
        fetchButtons()
        setupOnBackClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwner.lifecycleScope.coroutineContext.cancelChildren()
    }

    //опред-ие ui
    private fun setupViews() {
        val userPhoto = "https://i.pinimg.com/564x/a2/fd/e6/a2fde6047d99afbbaa852db5320d6639.jpg"
        Glide.with(this)
            .load(userPhoto)
            .circleCrop()
            .into(binding.photoMainCathegories)
    }

    //фун-я определяет адаптер для категорий
    private fun fetchCategories() {
        categoriesViewModel.loadCategory()

        categoriesAdapter = CategoriesAdapter { data ->
            showAndAddDish(data) //вызывается диалоговое окно
        }
        binding.cathegoriesRecView.adapter = categoriesAdapter
        binding.cathegoriesRecView.layoutManager = GridLayoutManager(requireContext(), 3)

        categoriesViewModel.currentCategory.observe(viewLifecycleOwner) { categoryData ->
            categoryData?.let { categories ->
                if (categoryData.isNotEmpty()) {
                    listDishesCategories.clear()
                    listDishesCategories.addAll(categories)
                    categoriesAdapter.kitchens = listDishesCategories
                    categoriesAdapter.notifyDataSetChanged()
                } else {
                    // Загрузка не выполнена или данные отсутствуют
                    categoriesViewModel.loadCategory()
                }
            }
        }

        categoriesViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //фун-я определяет адаптер для кнопок-категорий
    private fun fetchButtons() {
        categoriesViewModel.loadTags()

        buttonsAdapter = ButtonsAdapter { tag ->
            searchForTag(tag)
        }
        binding.dishesButton.adapter = buttonsAdapter
        val layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.dishesButton.layoutManager = layoutManager

        categoriesViewModel.currentTags.observe(viewLifecycleOwner) { tags ->
            buttonsAdapter.item = tags
            buttonsAdapter.notifyDataSetChanged()
        }
    }

    //фун-я определяет поиск по тегу при помощи кнопок
    private fun searchForTag(tag: String) {
        categoriesViewModel.currentCategory.observe(viewLifecycleOwner) { categoryData ->
            categoryData?.let { categories ->
                filteredTagsForSearch.clear()
                val newList = categories.filter { it.tegs.contains(tag) }
                filteredTagsForSearch.addAll(newList)
                categoriesAdapter.kitchens = filteredTagsForSearch
                categoriesAdapter.notifyDataSetChanged()
            }
        }
    }

    //обработка нажатий для кнопки назад из категорий
    private fun setupOnBackClickListeners() {
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    // всплывающее диалоговое окно
    private fun showAndAddDish(data: Dishes) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_categories, null)
        val image = dialogView.findViewById<ImageView>(R.id.image_dish)
        val textDish = dialogView.findViewById<TextView>(R.id.text_dish)
        val price = dialogView.findViewById<TextView>(R.id.price)
        val weight = dialogView.findViewById<TextView>(R.id.weight)
        val description = dialogView.findViewById<TextView>(R.id.description)
        val button = dialogView.findViewById<Button>(R.id.add_dish)
        val buttonBack = dialogView.findViewById<ImageView>(R.id.back_to_dialog)

        textDish.text = data.name
        "${data.price.toString()} ₽".also { price.text = it }
        "${data.weight.toString()}г".also { weight.text = it }
        description.text = data.description

        Glide.with(this)
            .load(data.imageUrl)
            .optionalCenterCrop()
            .centerInside()
            .into(image)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        //добавление товара в корзину
        button.setOnClickListener {
            shopListModel.addToShopList(data)
            alertDialog.dismiss()
        }

        buttonBack.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    companion object {
        fun newInstanceCategory() = CategoriesFragment()
    }
}