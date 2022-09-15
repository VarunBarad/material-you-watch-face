package com.example.materialcolor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.materialcolor.ui.theme.MaterialColorTheme
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    companion object {
        private const val COLORS_PATH = "/colors"
        private const val KEY_COLOR_HOUR = "key_color_hour"
        private const val KEY_COLOR_MINUTE = "key_color_minute"
        private const val KEY_COLOR_SECOND = "key_color_second"
    }

    private val dataClient by lazy { Wearable.getDataClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialColorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(::sendColors)
                }
            }
        }
    }

    private fun sendColors(color: String) {
        lifecycleScope.launch {
            try {
                val request = PutDataMapRequest.create(COLORS_PATH).apply {
                    when (color) {
                        "cyan" -> {
                            dataMap.putInt(KEY_COLOR_HOUR, 11)
                            dataMap.putInt(KEY_COLOR_MINUTE, 12)
                            dataMap.putInt(KEY_COLOR_SECOND, 13)
                        }
                        "brown" -> {
                            dataMap.putInt(KEY_COLOR_HOUR, 21)
                            dataMap.putInt(KEY_COLOR_MINUTE, 22)
                            dataMap.putInt(KEY_COLOR_SECOND, 23)
                        }
                        "orange" -> {
                            dataMap.putInt(KEY_COLOR_HOUR, 31)
                            dataMap.putInt(KEY_COLOR_MINUTE, 32)
                            dataMap.putInt(KEY_COLOR_SECOND, 33)
                        }
                    }
                }.asPutDataRequest().setUrgent()

                dataClient.putDataItem(request).await()
            } catch (e:Exception) {
                Log.d("VarunVarunVarun", "Failed sending colors", e)
            }
        }
    }
}

@Composable
fun HomeScreen(
    buttonClickListener: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { buttonClickListener("cyan") }) {
            Text("Cyan")
        }
        Button(onClick = { buttonClickListener("brown") }) {
            Text("Brown")
        }
        Button(onClick = { buttonClickListener("orange") }) {
            Text("Orange")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialColorTheme {
        HomeScreen(buttonClickListener = {})
    }
}