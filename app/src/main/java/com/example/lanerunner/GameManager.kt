package com.example.lanerunner

class GameManager(private val lifeCount: Int = 3) {

    var currentLives: Int = lifeCount
        private set
    var currentLane: Int = 1
        private set

    var isGameOver: Boolean = false
        private set

    var gameGrid: Array<IntArray> = Array(5) { IntArray(3) }
        private set

    fun moveRight(): Boolean{
        if (currentLane < 2){
            currentLane++
            return true
        }
        return false
    }

    fun moveLeft(): Boolean {
        if (currentLane > 0) {
            currentLane--
            return true
        }
        return false
    }

    fun isCollision(): Boolean {
        return gameGrid[4][currentLane] == 1
    }

    fun reduceLife() {
        if (currentLives > 0) {
            currentLives--
        }
    }

    fun refreshLife() {
        currentLives = lifeCount
    }

    fun gameProgress(){
        if (isGameOver) return

        for (row in 4 downTo 1) {
            for (col in 0..2){
                gameGrid[row][col] = gameGrid[row - 1][col]
            }
        }
        for (col in 0..2) {
            gameGrid[0][col] = 0
        }
        val isCreateNewStop = (0..1).random() == 1 //setting the chances of new station
        if (isCreateNewStop) {
            val randomColumn = (0..2).random()
            gameGrid[0][randomColumn] = 1 //make new station visible
        }
    }
}