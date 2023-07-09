package com.example.twoviewxml

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.twoviewxml.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var viewModel: SecondViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstNumber = intent.getIntExtra("FIRST_NUMBER", 0)
        val secondNumber = intent.getIntExtra("SECOND_NUMBER", 0)

        viewModel = ViewModelProvider(this).get(SecondViewModel::class.java)

        observeViewModel()
        viewModel.fetchData(applicationContext, firstNumber, secondNumber)
    }
    // Наблюдение за LiveData sum в viewModel и реагирование на изменения
    private fun observeViewModel() {
        viewModel.sum.observe(this) { sum ->
            binding.sumTextView.text = "$sum"
        }

        // Наблюдение за LiveData userList в viewModel и реагирование на изменения
        viewModel.userList.observe(this) { userList ->
            for (user in userList) {
                val nameTextView = TextView(this)
                nameTextView.text = user.name
                nameTextView.textSize = 16f
                nameTextView.gravity = Gravity.CENTER

                val ageTextView = TextView(this)
                ageTextView.text = user.age
                ageTextView.textSize = 16f
                ageTextView.gravity = Gravity.CENTER

                // отображение данных во вью
                binding.nameColumnLayout.addView(nameTextView)
                binding.ageColumnLayout.addView(ageTextView)
            }
        }
        // Установка слушателя клика на кнопку goBack
        binding.goBackButton.setOnClickListener {
            finish() // Завершение текущей активности (SecondActivity)
        }
    }
}
