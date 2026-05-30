package com.example.lanerunner

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lanerunner.utilities.Constants
import kotlinx.coroutines.Runnable

class MainActivity : AppCompatActivity() {

    private lateinit var main_BTN_leftArrow: AppCompatImageButton
    private lateinit var main_BTN_rightArrow: AppCompatImageButton
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_IMG_buses: Array<AppCompatImageView>
    private lateinit var main_IMG_busStops: Array<Array<AppCompatImageView>>
    private lateinit var gameManager: GameManager
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var gameRunnable: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        SignalManager.init(this)

        findViews()

        gameManager = GameManager(main_IMG_hearts.size)

        initListener()

        gameRunnable = object : Runnable {
            override fun run() {
                gameManager.gameProgress()

                if (gameManager.isCollision()) {

                    val randomMessage = Constants.Messages.CRASH_MESSAGES.random()

                    SignalManager.getInstance()
                        .toast(randomMessage, SignalManager.ToastLength.SHORT)

                    SignalManager.getInstance()
                        .vibrate()

                    gameManager.reduceLife()

                    if (gameManager.currentLives >= 0) {
                        main_IMG_hearts[gameManager.currentLives].visibility = View.INVISIBLE
                    }
                }

                updateUI()

                if (gameManager.currentLives > 0) {
                    handler.postDelayed(this, Constants.Timer.DELAY)
                } else {
                    gameManager.refreshLife()
                    main_IMG_hearts.forEach { it.visibility = View.VISIBLE }
                    updateUI()
                    handler.postDelayed(this, Constants.Timer.DELAY)
                }
            }
        }
    }
    private fun findViews(){
        main_BTN_leftArrow = findViewById(R.id.main_BTN_leftArrow)
        main_BTN_rightArrow = findViewById(R.id.main_BTN_rightArrow)
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
        main_IMG_buses = arrayOf(
            findViewById(R.id.bus_left),
            findViewById(R.id.bus_center),
            findViewById(R.id.bus_right)
        )
        main_IMG_busStops = arrayOf(
            // row 0 -----> 0_1 means row 0 column 1
            arrayOf(findViewById(R.id.bus_stop_0_0), findViewById(R.id.bus_stop_0_1), findViewById(R.id.bus_stop_0_2)),
            // row 1
            arrayOf(findViewById(R.id.bus_stop_1_0), findViewById(R.id.bus_stop_1_1), findViewById(R.id.bus_stop_1_2)),
            // row 2
            arrayOf(findViewById(R.id.bus_stop_2_0), findViewById(R.id.bus_stop_2_1), findViewById(R.id.bus_stop_2_2)),
            // row 3
            arrayOf(findViewById(R.id.bus_stop_3_0), findViewById(R.id.bus_stop_3_1), findViewById(R.id.bus_stop_3_2)),
            // row 4
            arrayOf(findViewById(R.id.bus_stop_4_0), findViewById(R.id.bus_stop_4_1), findViewById(R.id.bus_stop_4_2))
        )
    }

    private fun initListener(){
        main_BTN_rightArrow.setOnClickListener{
            val previousLane = gameManager.currentLane

            val canMove = gameManager.moveRight()

            if(canMove){
                main_IMG_buses[previousLane].visibility = View.INVISIBLE
                main_IMG_buses[gameManager.currentLane].visibility = View.VISIBLE
            }
        }

        main_BTN_leftArrow.setOnClickListener{
            val previousLane = gameManager.currentLane

            val canMove = gameManager.moveLeft()

            if(canMove){
                main_IMG_buses[previousLane].visibility = View.INVISIBLE
                main_IMG_buses[gameManager.currentLane].visibility = View.VISIBLE
            }
        }
    }

    private fun updateUI() {
        val grid = gameManager.gameGrid

        for (row in 0..4) {
            for (col in 0..2) {
                if (grid[row][col] == 1) {
                    main_IMG_busStops[row][col].visibility = View.VISIBLE
                } else {
                    main_IMG_busStops[row][col].visibility = View.INVISIBLE
                }
            }
        }

        for (i in 0..2) {
            if (i == gameManager.currentLane) {
                main_IMG_buses[i].visibility = View.VISIBLE
            } else {
                main_IMG_buses[i].visibility = View.INVISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(gameRunnable)
    }

    override fun onResume() {
        super.onResume()
        if (gameManager.currentLives > 0) {
            handler.post(gameRunnable)
        }
    }

}