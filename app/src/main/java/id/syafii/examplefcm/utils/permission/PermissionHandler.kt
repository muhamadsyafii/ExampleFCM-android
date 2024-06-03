package id.syafii.examplefcm.utils.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

class PermissionState {

  var permissions: Array<String> = arrayOf()

  var permissionCallback: PermissionListener? = null

  var shouldRequestPermission by mutableStateOf(false)

  val isAllGranted: Boolean
    get() = permissions.isEmpty()

  var isAnyRevoked by mutableStateOf(false)

  fun updatePermissions(newData: Array<String>) {
    permissions = newData
    isAnyRevoked = newData.isNotEmpty()
  }

  fun filterSelectedPermission(context: Context) {
    permissions = permissions.filter { context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }.toTypedArray()
  }


  fun showPermissionRequest(
    requiredPermission: Array<String>,
    callback: PermissionListener? = null
  ) {
    permissions = requiredPermission
    permissionCallback = callback
    shouldRequestPermission = true
    isAnyRevoked = false
  }

  /*
  * Request Permission
  */

  fun requestStoragePermission(callback: PermissionListener?) {
    showPermissionRequest(PermissionHelper.getStoragePermission(), callback)
  }

  fun requestCameraPermission(callback: PermissionListener?) {
    showPermissionRequest(PermissionHelper.getCameraPermission(), callback)
  }

  fun requestLocationPermission(callback: PermissionListener?) {
    showPermissionRequest(PermissionHelper.getLocationPermission(), callback)
  }

  fun requestAudioPermission(callback: PermissionListener?) {
    showPermissionRequest(PermissionHelper.getAudioPermission(), callback)
  }
}


interface PermissionListener {
  fun isAllPermissionGranted()
  fun isAnyPermissionDenied(permission: String)
}


@Composable
fun PermissionHandler(
  permissionState: PermissionState
) {
  if (!permissionState.shouldRequestPermission) return

  val context = LocalContext.current
  permissionState.filterSelectedPermission(context)

  when {
    /* All Permission Granted */
    permissionState.isAllGranted -> {
      permissionState.permissionCallback?.isAllPermissionGranted()
      permissionState.shouldRequestPermission = false
    }

    /* Any Permission Denied */
    permissionState.isAnyRevoked -> {
      permissionState.permissionCallback?.isAnyPermissionDenied(permissionState.permissions.firstOrNull().toPermission())
      permissionState.shouldRequestPermission = true // Keep requesting if any permission is denied
      return
    }
  }

  val launcherPermissions = rememberLauncherForActivityResult(
    RequestMultiplePermissions()
  ) { permissionsMap ->
    val deniedPermissions = permissionsMap.filter { !it.value }.map { it.key }
    permissionState.updatePermissions(deniedPermissions.toTypedArray())
    permissionState.isAnyRevoked = deniedPermissions.isNotEmpty()

    if (deniedPermissions.isEmpty()) {
      permissionState.permissionCallback?.isAllPermissionGranted()
    } else {
      deniedPermissions.forEach {
        permissionState.permissionCallback?.isAnyPermissionDenied(it)
      }
    }
  }

  LaunchedEffect(Unit) {
    if (permissionState.shouldRequestPermission) {
      launcherPermissions.launch(permissionState.permissions)
    }
  }
}

private fun String?.toPermission(): String {
  return when (this) {
    in PermissionHelper.getLocationPermission() -> "Lokasi"
    in PermissionHelper.getStoragePermission() -> "Storage"
    in PermissionHelper.getCameraPermission() -> "Kamera"
    else -> "Unknown"
  }
}