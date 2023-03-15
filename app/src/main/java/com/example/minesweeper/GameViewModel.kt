package com.example.minesweeper


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.thread

class GameViewModel(val height: Int, val width: Int, private val countMine : Int) : ViewModel() {

    val countCell: Int = height * width

    val gameState: MutableLiveData<GameState> = MutableLiveData(GameState.WAIT)

    var seconds: MutableLiveData<Int> = MutableLiveData(0)


    private lateinit var _countRemainingMines: MutableLiveData<Int>
    val countRemainingMines: MutableLiveData<Int>
        get() = _countRemainingMines

    private lateinit var _gameField: MutableList<Cell>
    val gameField: MutableList<Cell>
        get() = _gameField


    init {

        if (countMine > (countCell - 9)) {
            throw Exception("А large number of mines")
        }
        clear()

        thread {
            while (true) {
                Thread.sleep(1000)
                if (gameState.value == GameState.STARTED)
                    seconds.postValue(seconds.value?.plus(1))
            }
        }
    }

    fun changeFlag(id: Int) {
        val cell = gameField[id]
        when (cell.condition.value) {
            Condition.FLAG -> {
                cell.condition.value = Condition.HIDDEN
                countRemainingMines.value = countRemainingMines.value?.plus(1)
            }
            else -> {
                cell.condition.value = Condition.FLAG
                countRemainingMines.value = countRemainingMines.value?.minus(1)
            }
        }

        /*if (countRemainingMines.value == 0 && !gameField.any { cell -> cell.type == Type.MINE && cell.condition.value != Condition.FLAG })
            gameState.value = GameState.WIN*/
    }

    private fun gameLose() {
        gameState.value = GameState.LOSE
        gameField.forEach {
            if (it.type == Type.MINE && it.condition.value != Condition.FLAG)
                it.condition.value = Condition.VISIBLE
            if (it.type != Type.MINE && it.condition.value == Condition.FLAG)
                it.isFlagError.value = true
        }
    }

    private fun gameWin() {
        gameField.forEachIndexed { index, cell ->
            if (cell.type == Type.MINE && cell.condition.value != Condition.FLAG)
                changeFlag(index)
        }
        gameState.value = GameState.WIN
    }

    fun clear() {
        gameState.value = GameState.WAIT
        seconds.postValue(0)
        if (!::_countRemainingMines.isInitialized)
            _countRemainingMines = MutableLiveData(countMine)
        else
            countRemainingMines.value = countMine
        if (!::_gameField.isInitialized)
            _gameField = MutableList(countCell) { Cell(Type.NUMBER, 0) }
        else
            gameField.forEach {
                it.clear()
            }

    }

    fun open(id: Int) {
        val cell = gameField[id]
        if (cell.type == Type.MINE) {
            cell.isOpenMine = true
            gameLose()
            return
        }
        if (gameState.value == GameState.WAIT) {
            generate(id)
            print()
        }
        if (cell.value == 0)
            openWave(id)
        cell.condition.value = Condition.VISIBLE

        if (gameField.none { it.type == Type.NUMBER && it.condition.value != Condition.VISIBLE })
            gameWin()

        return
    }

    private fun openWave(id: Int) {
        val cells: Queue<Int> = LinkedList()
        cells.add(id)
        while (!cells.isEmpty()) {
            val tmpId = cells.poll() as Int
            if (gameField[tmpId].condition.value != Condition.HIDDEN)
                continue
            gameField[tmpId].condition.value = Condition.VISIBLE
            val x = getX(tmpId)
            val y = getY(tmpId)
            if (gameField[tmpId].value == 0)
                for (i in -1..1)
                    for (j in -1..1)
                        if (checkCoordinate(x + i, y + j))
                            cells.add(getId(x + i, y + j))

        }
    }

    private fun generate(id: Int) {
        gameState.value = GameState.STARTED
        val listId = (0 until countCell).toMutableList()
        val x = getX(id)
        val y = getY(id)
        for (i in -1..1)
            for (j in -1..1)
                if (checkCoordinate(x + i, y + j))
                    listId.remove(getId(x + i, y + j))

        for (i in 0 until countMine) {
            val mineId = listId.random()
            listId.remove(mineId)
            gameField[mineId].type = Type.MINE
        }

        for (i in 0 until countCell)
            gameField[i].value = getValue(i)
    }

    private fun getValue(id: Int): Int {
        val x = getX(id)
        val y = getY(id)
        var value = 0
        for (i in -1..1)
            for (j in -1..1)
                if (checkCoordinate(x + i, y + j) && gameField[getId(
                        x + i,
                        y + j
                    )].type == Type.MINE
                )
                    value++
        return value

    }

    private fun getX(id: Int): Int {
        return id % width
    }

    private fun getY(id: Int): Int {
        return id / width
    }

    private fun getId(x: Int, y: Int): Int {
        return y * width + x
    }

    private fun checkCoordinate(x: Int, y: Int): Boolean {
        return (x in 0 until width) && (y in 0 until height)
    }

    private fun print() {
        for (i in 0 until height) {
            print("$i: ")
            for (j in 0 until width) {
                val cell = gameField[getId(j, i)]
                print(
                    when (cell.type == Type.MINE) {
                        true -> "*"
                        else -> cell.value
                    }.toString() + " "
                )
            }
            println()

        }
    }

    fun print2() {
        for (i in 0 until height) {
            print("$i: ")
            for (j in 0 until width) {
                val cell = gameField[getId(j, i)]
                print(
                    when (cell.condition.value) {
                        Condition.HIDDEN -> "*"
                        else -> cell.value
                    }.toString() + " "
                )
            }
            println()

        }
    }

}