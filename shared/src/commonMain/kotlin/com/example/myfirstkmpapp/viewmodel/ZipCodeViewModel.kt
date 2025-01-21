package com.example.myfirstkmpapp.viewmodel

import com.example.myfirstkmpapp.api.ZipCodeApi
import com.example.myfirstkmpapp.data.AddressData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ZipCodeViewModel {
    private val api = ZipCodeApi()
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    
    private val _addressState = MutableStateFlow<AddressData?>(null)
    val addressState: StateFlow<AddressData?> = _addressState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // iOS用のプロパティ
    val currentAddress: AddressData? get() = _addressState.value
    val currentIsLoading: Boolean get() = _isLoading.value
    val currentError: String? get() = _error.value
    
    fun searchZipCode(zipCode: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = api.getAddressFromZipCode(zipCode)
                if (response.status == 200 && !response.results.isNullOrEmpty()) {
                    _addressState.value = response.results.first()
                } else {
                    _error.value = "住所が見つかりませんでした"
                }
            } catch (e: Exception) {
                _error.value = "エラーが発生しました: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
