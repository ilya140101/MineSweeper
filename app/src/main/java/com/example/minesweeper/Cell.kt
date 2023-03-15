package com.example.minesweeper

import androidx.lifecycle.MutableLiveData

class Cell(var type: Type, var value: Int) {
    var condition: MutableLiveData<Condition> = MutableLiveData(Condition.HIDDEN)

    var isOpenMine: Boolean = false

    var isFlagError: MutableLiveData<Boolean> = MutableLiveData(false)

    fun clear(){
        type = Type.NUMBER
        value = 0
        condition.value = Condition.HIDDEN
        isOpenMine = false
        isFlagError.value = false
    }


}