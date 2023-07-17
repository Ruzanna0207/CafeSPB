package com.cafe.cafespb.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.Debug
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.cafe.cafespb.R
import com.cafe.cafespb.adapters.adapter_main_page.Clickable
import com.cafe.cafespb.adapters.adapter_main_page.MainPageAdapter
import com.cafe.cafespb.databinding.FragmentMainPageBinding
import com.cafe.cafespb.location_helper_classes_main_page.PermissionHelper
import com.cafe.cafespb.view_models.MainViewModel
import kotlinx.coroutines.cancelChildren

class MainPageFragment : Fragment(), Clickable {

    private lateinit var binding: FragmentMainPageBinding
    private lateinit var permissionHelper: PermissionHelper
    private val actualKitchens = mutableListOf<com.cafe.core.data_classes.Categories>()

    private val adapterMainPage: MainPageAdapter by lazy {
        MainPageAdapter(actualKitchens, this)
    }
    private val kitchensViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageBinding.inflate(inflater, container, false)
        if (actualKitchens.isEmpty()) {
            // Загрузка еще не выполнена или данные отсутствуют
            kitchensViewModel.loadKitchens()
        } else {
            // Данные уже загружены, используем их
            adapterMainPage.notifyDataSetChanged()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kitchensViewModel.loadCity(requireContext())

        setupViews()

        permissionHelper = PermissionHelper(this)
        if (permissionHelper.checkLocationPermission()) {
            Debug.getLocation()
        } else {
            requestLocationPermission()
        }
        getLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwner.lifecycleScope.coroutineContext.cancelChildren()
    }

    companion object {
        fun newInstance() = MainPageFragment()
    }

    //опред-ие основного ui, загрузка значений
    private fun setupViews() {
        kitchensViewModel.loadDate()

        if (actualKitchens.isEmpty()) {
            // Загрузка еще не выполнена или данные отсутствуют
            kitchensViewModel.loadKitchens()
        } else {
            // Данные уже загружены, используем их
            adapterMainPage.notifyDataSetChanged()
        }

        kitchensViewModel.date.observe(viewLifecycleOwner) { date ->
            binding.date.text = date
        }

        //загрузка последнего сохр. города, если город изменился, то после проверки это знач-е далее изменится
        binding.city.text = getSavedCity() ?: ""

        val userPhoto = "https://i.pinimg.com/564x/a2/fd/e6/a2fde6047d99afbbaa852db5320d6639.jpg"
        Glide.with(this)
            .load(userPhoto)
            .circleCrop()
            .into(binding.photoMain)

        kitchensViewModel.currentKitchens.observe(viewLifecycleOwner) { kitchensData ->
            if (kitchensData.isNotEmpty()) {
                actualKitchens.clear()
                actualKitchens.addAll(kitchensData)
                adapterMainPage.notifyDataSetChanged()
            } else {
                // Загрузка не выполнена или данные отсутствуют
                kitchensViewModel.loadKitchens()
            }
        }
        binding.mainRecView.adapter = adapterMainPage
    }

    private fun getLocation() {
        kitchensViewModel.city.observe(viewLifecycleOwner) { city ->
            binding.city.text = city
            val savedCity = getSavedCity()
            if (savedCity == null) {
                // Если город не сохранен в SharPref
                saveCity(city)
            } else {
                // Если город уже есть проверяем изменился ли он
                if (city != savedCity) {
                    // если город изменился
                    saveCity(city)
                } else {
                    // Город не изменился
                    binding.city.text = savedCity
                }
            }
        }
    }

    private fun saveCity(city: String) {
        val sharedPreferences =
            requireContext().getSharedPreferences("CityName", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("city", city)
        editor.apply()
    }

    private fun getSavedCity(): String? {
        val sharedPreferences =
            requireContext().getSharedPreferences("CityName", Context.MODE_PRIVATE)
        return sharedPreferences.getString("city", "")
    }

    private fun requestLocationPermission() {
        permissionHelper.requestLocationPermission(
            grantedCallback = {},
            deniedCallback = {}
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.onRequestPermissionsResult(requestCode, grantResults)
    }

    //переопред-е действий для слушателя нажатий
    override fun onClick() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame, CategoriesFragment.newInstanceCategory())
            .addToBackStack(null)
            .commit()
    }
}