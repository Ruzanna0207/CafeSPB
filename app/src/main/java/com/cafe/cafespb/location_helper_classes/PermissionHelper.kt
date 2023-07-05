package com.cafe.cafespb.location_helper_classes

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.cafe.cafespb.presentation.MainPageFragment

//класс использ-ся для получ-я разрешений на получ-е локации пользователя
class PermissionHelper(private val fragment: MainPageFragment) {

    private val locationPermissionsRequestCode = 1001
    private val requestPermissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                permissionGrantedCallback?.invoke()
            } else {
                permissionDeniedCallback?.invoke()
            }
        }

    private var permissionGrantedCallback: (() -> Unit)? = null
    private var permissionDeniedCallback: (() -> Unit)? = null

    fun checkLocationPermission(): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        return ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(grantedCallback: () -> Unit, deniedCallback: () -> Unit) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        permissionGrantedCallback = grantedCallback
        permissionDeniedCallback = deniedCallback
        requestPermissionLauncher.launch(permission)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionsRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionGrantedCallback?.invoke()
        } else {
            permissionDeniedCallback?.invoke()
        }
    }
}


//class PermissionHelper(private val activity: FragmentActivity) {
//
//    private val locationPermissionsRequestCode = 1001
//    private val requestPermissionLauncher =
//        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                permissionGrantedCallback?.invoke()
//            } else {
//                permissionDeniedCallback?.invoke()
//            }
//        }
//
//    private var permissionGrantedCallback: (() -> Unit)? = null
//    private var permissionDeniedCallback: (() -> Unit)? = null
//
//    fun checkLocationPermission(): Boolean {
//        val permission = Manifest.permission.ACCESS_FINE_LOCATION
//        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
//    }
//
//    fun requestLocationPermission(grantedCallback: () -> Unit, deniedCallback: () -> Unit) {
//        val permission = Manifest.permission.ACCESS_FINE_LOCATION
//        permissionGrantedCallback = grantedCallback
//        permissionDeniedCallback = deniedCallback
//        requestPermissionLauncher.launch(permission)
//    }
//
//    fun onRequestPermissionsResult(
//        requestCode: Int,
//        grantResults: IntArray
//    ) {
//        if (requestCode == locationPermissionsRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            permissionGrantedCallback?.invoke()
//        } else {
//            permissionDeniedCallback?.invoke()
//        }
//    }
//}

//
//class PermissionHelper(private val context: Context) {
//
//    private val locationPermissionsRequestCode = 1001
//    private val requestPermissionLauncher =
//        (context as? FragmentActivity)?.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                permissionGrantedCallback?.invoke()
//            } else {
//                permissionDeniedCallback?.invoke()
//            }
//        }
//
//    private var permissionGrantedCallback: (() -> Unit)? = null
//    private var permissionDeniedCallback: (() -> Unit)? = null
//
//    fun checkLocationPermission(): Boolean {
//        val permission = Manifest.permission.ACCESS_FINE_LOCATION
//        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
//    }
//
//    fun requestLocationPermission(grantedCallback: () -> Unit, deniedCallback: () -> Unit) {
//        val permission = Manifest.permission.ACCESS_FINE_LOCATION
//        permissionGrantedCallback = grantedCallback
//        permissionDeniedCallback = deniedCallback
//        requestPermissionLauncher?.launch(permission)
//    }
//
//    fun onRequestPermissionsResult(
//        requestCode: Int,
//        grantResults: IntArray
//    ) {
//        if (requestCode == locationPermissionsRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            permissionGrantedCallback?.invoke()
//        } else {
//            permissionDeniedCallback?.invoke()
//        }
//    }
//}
