package id.syafii.examplefcm.utils.permission

import android.Manifest.permission
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi

object PermissionHelper {
  private var STORAGE_PERMISSIONS = arrayOf(
    permission.READ_EXTERNAL_STORAGE,
    permission.WRITE_EXTERNAL_STORAGE
  )

  private var LOCATION_PERMISSIONS = arrayOf(
    permission.ACCESS_COARSE_LOCATION,
    permission.ACCESS_FINE_LOCATION
  )

  private var CAMERA_PERMISSIONS = arrayOf(
    permission.CAMERA
  )

  private var AUDIO_PERMISSION = arrayOf(
    permission.RECORD_AUDIO
  )

  @RequiresApi(VERSION_CODES.TIRAMISU)
  var POST_NOTIFICATION = permission.POST_NOTIFICATIONS

  fun getLocationPermission(): Array<String> {
    return LOCATION_PERMISSIONS
  }


  @RequiresApi(VERSION_CODES.TIRAMISU)
  fun getStoragePermission(): Array<String> {
    return when {
      Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU -> {
        arrayOf(
          permission.READ_MEDIA_IMAGES,
          permission.READ_MEDIA_VIDEO
        )
      }
      Build.VERSION.SDK_INT >= VERSION_CODES.Q -> {
        arrayOf(permission.READ_EXTERNAL_STORAGE)
      }
      else -> {
        STORAGE_PERMISSIONS
      }
    }
  }



  fun getCameraPermission(): Array<String> {
    return CAMERA_PERMISSIONS
  }

  fun getAudioPermission(): Array<String> {
    return AUDIO_PERMISSION
  }

}