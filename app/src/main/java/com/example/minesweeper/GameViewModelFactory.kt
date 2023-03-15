package com.example.minesweeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModelFactory(val height: Int, val width: Int, private val countMine: Int) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(height, width, countMine) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}