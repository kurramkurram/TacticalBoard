package io.github.kurramkurram.futaltacticalboard.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.kurramkurram.futaltacticalboard.ColorEnum
import io.github.kurramkurram.futaltacticalboard.Player
import io.github.kurramkurram.futaltacticalboard.Preference
import io.github.kurramkurram.futaltacticalboard.R
import io.github.kurramkurram.futaltacticalboard.db.PlayerData
import io.github.kurramkurram.futaltacticalboard.db.PlayerDataDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FutsalCortActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val PLAYER_RED_ARRAY = arrayOf(
            R.drawable.player_red_1,
            R.drawable.player_red_2,
            R.drawable.player_red_3,
            R.drawable.player_red_4,
            R.drawable.player_red_5
        )
        val PLAYER_BLUE_ARRAY = arrayOf(
            R.drawable.player_blue_1,
            R.drawable.player_blue_2,
            R.drawable.player_blue_3,
            R.drawable.player_blue_4,
            R.drawable.player_blue_5
        )

        private val TAG = "FutsalCortActivity"
    }

    private lateinit var mWindowManager: WindowManager
    private var mPlayersBlue = arrayOfNulls<Player>(PLAYER_BLUE_ARRAY.size)
    private var mPlayersRed = arrayOfNulls<Player>(PLAYER_RED_ARRAY.size)
    private lateinit var mCortLayout: ConstraintLayout
    private lateinit var mDrawIcon: ImageView
    private lateinit var mDeleteIcon: ImageView
    private lateinit var mLine: DrawLine

    private val mScope = CoroutineScope(Dispatchers.Default)
    private lateinit var mMovieLayout: LinearLayout

    private var mPlayerDataArray: ArrayList<PlayerData> = ArrayList()

    private var mIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        mCortLayout = findViewById(R.id.futsal_cort)

        val settings = findViewById<ImageView>(R.id.futsal_cort_setting)
        settings.setOnClickListener(this)

        val createMovie = findViewById<ImageView>(R.id.futsal_cort_create_movie)
        createMovie.setOnClickListener(this)

        mDrawIcon = findViewById(R.id.futsal_cort_draw_line)
        mDrawIcon.setOnClickListener(this)

        mDeleteIcon = findViewById(R.id.futsal_cort_delete_line)
        mDeleteIcon.setOnClickListener(this)

        val addMovie = findViewById<ImageView>(R.id.add_movie_edit)
        addMovie.setOnClickListener(this)

        val playMovie = findViewById<ImageView>(R.id.play_movie_edit)
        playMovie.setOnClickListener(this)

        val saveMovie = findViewById<ImageView>(R.id.save_movie_edit)
        saveMovie.setOnClickListener(this)

        val cancelMovie = findViewById<ImageView>(R.id.cancel_movie_edit)
        cancelMovie.setOnClickListener(this)

        mMovieLayout = findViewById(R.id.futsal_cort_movie_layout)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "#onResume")

        val context = applicationContext

        val backgroundIndex =
            Preference.get(context, Preference.KEY_BACKGROUND_RESOURCE_INDEX, 0)
        val background = findViewById<ImageView>(R.id.futsal_cort_background)

        val backgroundArray = resources.obtainTypedArray(R.array.tactical_board_background_array)
        val color = backgroundArray.getColor(backgroundIndex, -1)
        background.setBackgroundColor(color)
        backgroundArray.recycle()

        val isHalf = Preference.get(context, Preference.KEY_HALF_CORT, false)

        var drawableArray = resources.obtainTypedArray(R.array.tactical_board_array)

        if (isHalf) {
            drawableArray = resources.obtainTypedArray(R.array.tactical_board_half_array)
        }

        val drawable = drawableArray.getDrawable(backgroundIndex)
        background.setImageDrawable(drawable)
        drawableArray.recycle()

        val namePrefixBlue =
            Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_BLUE
        val namePrefixRed =
            Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_RED
        if (mPlayersBlue[0] == null) {

            for (i in PLAYER_BLUE_ARRAY.indices) {
                val player = Player(
                    applicationContext, i, PLAYER_BLUE_ARRAY[i],
                    Preference.get(context, namePrefixBlue + (i + 1), ""),
                    mWindowManager, arrayOf(150 * i, 150), Gravity.TOP
                )
                player.add()
                mPlayersBlue[i] = player
            }

            for (i in PLAYER_RED_ARRAY.indices) {
                val player = Player(
                    applicationContext, i, PLAYER_RED_ARRAY[i],
                    Preference.get(context, namePrefixRed + (i + 1), ""),
                    mWindowManager, arrayOf(150 * i, 0), Gravity.BOTTOM
                )
                player.add()
                mPlayersRed[i] = player
            }
        } else {
            for ((i, p) in mPlayersBlue.withIndex()) {
                p!!.setName(Preference.get(context, namePrefixBlue + (i + 1), ""))
                p.add()
            }
            for ((i, p) in mPlayersRed.withIndex()) {
                p!!.setName(Preference.get(context, namePrefixRed + (i + 1), ""))
                p.add()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        for (p in mPlayersBlue) {
            p!!.remove()
        }
        for (p in mPlayersRed) {
            p!!.remove()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayersBlue = arrayOfNulls(PLAYER_BLUE_ARRAY.size)
        mPlayersRed = arrayOfNulls(PLAYER_RED_ARRAY.size)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.futsal_cort_setting -> {
                startSetting()
            }
            R.id.futsal_cort_create_movie -> {
                createMovie()
            }
            R.id.add_movie_edit -> {
                addMovie()
            }
            R.id.play_movie_edit -> {
                playMovie()
            }
            R.id.save_movie_edit -> {
                saveMovie()
            }
            R.id.cancel_movie_edit -> {
                cancelMovie()
            }
            R.id.futsal_cort_draw_line -> {
                drawLine()
            }
            R.id.futsal_cort_delete_line -> {
                deleteLine()
            }
        }
    }

    private suspend fun saveTask() {
        Log.d("FutsalCortActivity", "#saveTask")

        try {
            val db = PlayerDataDatabase.getDatabases(applicationContext)
            val playerDao = db.playerDao()
            playerDao.insert(mPlayerDataArray)
        } catch (e: Exception) {
            Log.e("FutsalCortActivity", "#saveTask", e)
        }
    }

    private fun startSetting() {
        Log.d(TAG, "#startSetting")
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    private fun createMovie() {
        Log.d(TAG, "#createMovie")
        mMovieLayout.visibility = View.VISIBLE
        mPlayerDataArray = ArrayList()
        mIndex = 0
        savePosition()
    }

    private fun addMovie() {
        Log.d(TAG, "#addMovie")
        mIndex++
        savePosition()
    }

    private fun savePosition() {
        Log.d(TAG, "#savePosition")
        for (p in mPlayersBlue) {
            val playerData = PlayerData(
                0, 1, mIndex, ColorEnum.BLUE.id, p!!.mId, p.mName.toString(),
                p.mParams.x, p.mParams.y
            )
            mPlayerDataArray.add(playerData)
        }
        for (p in mPlayersRed) {
            val playerData = PlayerData(
                0, 1, mIndex, ColorEnum.RED.id, p!!.mId, p.mName.toString(),
                p.mParams.x, p.mParams.y
            )
            mPlayerDataArray.add(playerData)
        }
    }

    private fun playMovie() {
        Log.d(TAG, "#playMovie")
        for (playerData in mPlayerDataArray) {
            val color = playerData.playerColor
            val playerId = playerData.playerId
            val x = playerData.playerX
            val y = playerData.playerY

            when (color) {
                ColorEnum.BLUE.id -> {
                    for (player in mPlayersBlue) {
                        if (player!!.mId == playerId) {
                            player.put(arrayOf(x, y))
                        }
                    }
                }
                ColorEnum.RED.id -> {
                    for (player in mPlayersRed) {
                        if (player!!.mId == playerId) {
                            player.put(arrayOf(x, y))
                        }
                    }
                }
            }
        }
        for (player in mPlayersBlue) {
            player!!.play()
        }
        for (player in mPlayersRed) {
            player!!.play()
        }

    }

    private fun saveMovie() {
        mScope.launch {
            saveTask()
        }
    }

    private fun cancelMovie() {
        mMovieLayout.visibility = View.GONE
    }

    private fun drawLine() {
        mDrawIcon.visibility = View.GONE
        mDeleteIcon.visibility = View.VISIBLE
        mLine = DrawLine(applicationContext)
        mCortLayout.addView(mLine, 1)
    }

    private fun deleteLine() {
        mCortLayout.removeView(mLine)
        mDeleteIcon.visibility = View.GONE
        mDrawIcon.visibility = View.VISIBLE
    }
}