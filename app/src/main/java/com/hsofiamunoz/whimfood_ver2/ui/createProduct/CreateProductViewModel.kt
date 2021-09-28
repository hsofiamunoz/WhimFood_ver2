package com.hsofiamunoz.whimfood_ver2.ui.createProduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateProductViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is create Fragment"
    }
    val text: LiveData<String> = _text
}