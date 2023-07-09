package com.example.twoviewxml

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class SecondViewModel : ViewModel() {
    private val _sum = MutableLiveData<Int>(0)
    val sum: LiveData<Int> = _sum

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> = _userList

    private val _isLoading = MutableLiveData<Boolean>(false)

    // Метод fetchData принимает контекст приложения и два значения firstNumber и secondNumber
    fun fetchData(context: Context, firstNumber: Int, secondNumber: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            // Парсинг json
            try {
                // Чтение содержимого файла name.json из папки assets и получение массива "users" из JSON-объекта
                val jsonString =
                    context.assets.open("name.json").bufferedReader().use { it.readText() }
                val jsonData = JSONObject(jsonString).getJSONArray("users")

                val userList = mutableListOf<User>()
                //Перебираем и получаем то, что необходимо (name и age)
                for (i in 0 until jsonData.length()) {
                    val userObject = jsonData.getJSONObject(i)
                    val name = userObject.getString("name")
                    val age = userObject.getString("age")
                    val user = User(name, age)
                    userList.add(user)
                }

                _userList.value = userList
                // Обновление значения _sum
                val sum = firstNumber + secondNumber
                _sum.value = sum

            } catch (e: Exception) {
                // Обработка ошибки при загрузке данных
            } finally {
                _isLoading.value = false
            }
        }
    }
}
