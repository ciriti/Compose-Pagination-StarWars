package com.example.messageapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.messageapp.data.network.ConnectivityRepository
import com.example.messageapp.domain.sync.MessageSyncManager
import com.example.messageapp.ui.theme.StarWarsAppTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.activityScope

class MainActivity : ComponentActivity() {

    private val connectivityRepository by inject<ConnectivityRepository>()
    private val messageSyncManager by inject<MessageSyncManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageSyncManager.monitorNetworkAndSync()

        lifecycleScope.launch {
            connectivityRepository
                .isConnected
                .collect { isConnected ->
                    println("========== isConnected: $isConnected")
                }
        }

        enableEdgeToEdge()
        setContent {
            StarWarsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StarWarsAppTheme {
        Greeting("Android")
    }
}
