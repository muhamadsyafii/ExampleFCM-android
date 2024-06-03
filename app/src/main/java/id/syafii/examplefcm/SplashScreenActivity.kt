package id.syafii.examplefcm

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.messaging.FirebaseMessaging
import id.syafii.examplefcm.ui.theme.ExampleFCMTheme
import id.syafii.examplefcm.utils.permission.PermissionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {

  private val requestNotificationPermission =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//      startSplashScreen()
      getTokenAndStartSplashScreen()
    }

  @SuppressLint("CoroutineCreationDuringComposition")
  @RequiresApi(VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ExampleFCMTheme {
        CheckNotificationPermission()
        lifecycleScope.launch {
          val bearerToken = getBearerToken(this@SplashScreenActivity)
          Log.d("CheckToken", "accessToken: $bearerToken")
        }
      }
    }
  }

  private fun startSplashScreen() {
    setContent {
      ExampleFCMTheme {
        SplashScreen(this)
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  @Composable
  fun CheckNotificationPermission() {
    val permission = PermissionHelper.POST_NOTIFICATION
    when {
      ContextCompat.checkSelfPermission(
        this, permission
      ) == PackageManager.PERMISSION_GRANTED -> {
        // Permission is already granted, start the splash screen
//        startSplashScreen()
        getTokenAndStartSplashScreen()
      }

      else -> {
        requestNotificationPermission.launch(permission)
      }
    }
  }

  private fun getTokenAndStartSplashScreen() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
      if (task.isSuccessful) {
        val token = task.result
        Log.d("CheckToken", "token: $token")
        startSplashScreen()
      } else {
        Log.d("CheckToken", "token: else - ${task.exception}")
        startSplashScreen() // Start splash screen even if token fetch fails
      }
    }
  }

  suspend fun getBearerToken(context: Context): String {
    return withContext(Dispatchers.IO) {
      val inputStream: InputStream = context.assets.open("service-account.json")
      val credentials: GoogleCredentials = GoogleCredentials.fromStream(inputStream).createScoped(
        arrayListOf("https://www.googleapis.com/auth/firebase.messaging")
      )
      // Ambil token akses
      val token = credentials.refreshAccessToken().tokenValue
      // Kembalikan bearer token
      "Bearer ${token}"
    }
  }

}