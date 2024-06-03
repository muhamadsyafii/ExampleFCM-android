package id.syafii.examplefcm

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  activity: SplashScreenActivity
) {
  LaunchedEffect(key1 = true, block = {
    delay(1000)
    activity.startActivity(Intent(activity, MainActivity::class.java))
    activity.finish()
  })

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(color = Color.Blue),
    contentAlignment = Alignment.Center
  ) {
    Image(
      painter = painterResource(R.drawable.bg_splash_screen),
      contentDescription = "App Logo",
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.FillBounds
    )

    Image(
      painter = painterResource(R.drawable.ic_app_logo),
      contentDescription = "App Logo",
      modifier = Modifier
        .height(120.dp)
        .width(120.dp),
      contentScale = ContentScale.Fit
    )

  }
}