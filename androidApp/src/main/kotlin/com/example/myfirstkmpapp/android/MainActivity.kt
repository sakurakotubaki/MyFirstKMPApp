package com.example.myfirstkmpapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myfirstkmpapp.viewmodel.ZipCodeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val viewModel = ZipCodeViewModel()
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ZipCodeScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun ZipCodeScreen(viewModel: ZipCodeViewModel) {
    var zipCode by remember { mutableStateOf("") }
    val addressState by viewModel.addressState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = zipCode,
            onValueChange = { newValue ->
                // 数字以外の文字を除去
                val filtered = newValue.filter { it.isDigit() }
                // 7文字までに制限
                if (filtered.length <= 7) {
                    zipCode = filtered
                }
            },
            label = { Text("郵便番号") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { viewModel.searchZipCode(zipCode) },
            enabled = zipCode.length == 7,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("検索")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (isLoading) {
            CircularProgressIndicator()
        }
        
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }
        
        addressState?.let { address ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("都道府県: ${address.prefecture}")
                    Text("市区町村: ${address.city}")
                    Text("町名: ${address.town}")
                }
            }
        }
    }
}
