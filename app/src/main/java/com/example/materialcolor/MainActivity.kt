package com.example.materialcolor

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.materialcolor.ui.theme.MaterialColorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialColorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(::showToast)
                }
            }
        }
    }

    private fun showToast(color: String) {
        Toast.makeText(this, color, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun HomeScreen(
    showToast: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { showToast("cyan") }) {
            Text("Cyan")
        }
        Button(onClick = { showToast("brown") }) {
            Text("Brown")
        }
        Button(onClick = { showToast("orange") }) {
            Text("Orange")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialColorTheme {
        HomeScreen(showToast = {})
    }
}