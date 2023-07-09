package com.example.twoviewxml

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.twoviewxml.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java) // Получаем экземпляр виьюмодели

        setupViews() // Настраиваем визуальные компоненты
        observeViewModel() // Наблюдаем за состоянием виьюмодели
    }

    private fun setupViews() {
        binding.nextButton.setOnClickListener { // Обработчик клика кнопки "Next"
            val firstNumber = binding.firstNumberEditText.text.toString() // Получаем значение из поля ввода первого числа
            val secondNumber = binding.secondNumberEditText.text.toString() // Получаем значение из поля ввода второго числа
            viewModel.onNumbersChanged(firstNumber, secondNumber) // Вызываем метод виьюмодели для обновления чисел

            if (viewModel.isNextButtonEnabled.value == true) {
                binding.progressBar.visibility = View.VISIBLE // Отображение прогресс-бара

                GlobalScope.launch {
                    delay(2000) // Задержка в 2 секунды
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE // Скрытие прогресс-бара после задержки
                        navigateToSecondView(firstNumber.toInt(), secondNumber.toInt()) // Переходим на второй экран, передавая значения чисел
                    }
                }
            }
        }

        val textWatcher = object : TextWatcher { //отслеживание изменений в текстовых полях
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val firstNumber = binding.firstNumberEditText.text.toString()
                val secondNumber = binding.secondNumberEditText.text.toString()
                viewModel.onNumbersChanged(firstNumber, secondNumber) // Вызываем метод вьюмодели для обновления чисел
            }
        }
        //слушатель изменений (получение чисел)
        binding.firstNumberEditText.addTextChangedListener(textWatcher)
        binding.secondNumberEditText.addTextChangedListener(textWatcher)
    }
    // Активируем кнопку "Next" при указании чисел в первом вью
    private fun observeViewModel() {
        viewModel.isNextButtonEnabled.observe(this) { isEnabled ->
            binding.nextButton.isEnabled = isEnabled
        }
    }
    // Создаем интент для передачи чисел из первого вью и перехода/передачи на второй экран
    private fun navigateToSecondView(firstNumber: Int, secondNumber: Int) {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("FIRST_NUMBER", firstNumber)
        intent.putExtra("SECOND_NUMBER", secondNumber)
        startActivity(intent)
    }
}
