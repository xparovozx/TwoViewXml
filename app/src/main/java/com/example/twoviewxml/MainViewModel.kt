package com.example.twoviewxml

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    // Состояние кнопки "Next"
    private val _isNextButtonEnabled = MutableLiveData<Boolean>(false)
    val isNextButtonEnabled: LiveData<Boolean> = _isNextButtonEnabled

    // Состояние кнопки "Next", т.е. заполнены ли ячейки
    fun onNumbersChanged(firstNumber: String, secondNumber: String) {
        _isNextButtonEnabled.value = firstNumber.isNotEmpty() && secondNumber.isNotEmpty()
    }
}

