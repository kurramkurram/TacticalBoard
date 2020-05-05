package io.github.kurramkurram.futaltacticalboard.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.kurramkurram.futaltacticalboard.ColorEnum
import io.github.kurramkurram.futaltacticalboard.Player
import io.github.kurramkurram.futaltacticalboard.Preference
import io.github.kurramkurram.futaltacticalboard.R
import io.github.kurramkurram.futaltacticalboard.db.PlayerData
import io.github.kurramkurram.futaltacticalboard.db.PlayerDataDatabase
import io.github.kurramkurram.futaltacticalboard.db.SavedMovieListData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FutsalCortActivity : AppCompatActivity(), View.OnClickListener,
    Player.OnAnimationCallback, SaveMovieDialogFragment.OnDialogResultCallback {

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

        private const val TAG = "FutsalCortActivity"
        private const val MOVIE_FOLDER_LIST_REQUEST = 0
        const val KEY_BACKGROUND_COLOR = "key_background_color"

        private const val VIEW_SIZE = 10
    }

    private lateinit var mWindowManager: WindowManager
    private var mPlayersBlue = arrayOfNulls<Player>(PLAYER_BLUE_ARRAY.size)
    private var mPlayersRed = arrayOfNulls<Player>(PLAYER_RED_ARRAY.size)
    private lateinit var mCortLayout: ConstraintLayout
    private lateinit var mDrawIcon: ImageView
    private lateinit var mDeleteIcon: ImageView
    private lateinit var mLine: DrawLine

    private lateinit var mSeekBar: SeekBar
    private lateinit var mMovieIndex: TextView

    private lateinit var mMovieLayout: LinearLayout

    private var mBackgroundColor: Int = 0

    private var mPlayerDataArray: ArrayList<PlayerData> = ArrayList()

    private var mGroupId = 0L
    private var mIndex = 0
    private var mFinishedMap = SparseArray<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        mCortLayout = findViewById(R.id.futsal_cort)

        val settings = findViewById<ImageView>(R.id.futsal_cort_setting)
        settings.setOnClickListener(this)

        val createMovie = findViewById<ImageView>(R.id.futsal_cort_create_movie)
        createMovie.setOnClickListener(this)

        val movieFolder = findViewById<ImageView>(R.id.futsal_cort_movie_folder)
        movieFolder.setOnClickListener(this)

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
        mSeekBar = findViewById(R.id.seek_bar_movie_edit)
        mSeekBar.isEnabled = false

        mMovieIndex = findViewById(R.id.index_play_movie_edit)
    }

    override fun onResume() {
        super.onResume()
        val context = applicationContext

        val backgroundIndex =
            Preference.get(context, Preference.KEY_BACKGROUND_RESOURCE_INDEX, 0)
        val background = findViewById<ImageView>(R.id.futsal_cort_background)

        val backgroundArray = resources.obtainTypedArray(R.array.tactical_board_background_array)
        mBackgroundColor = backgroundArray.getColor(backgroundIndex, -1)
        background.setBackgroundColor(mBackgroundColor)
        backgroundArray.recycle()

        mMovieLayout.setBackgroundColor(mBackgroundColor)

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
                    mWindowManager, arrayOf(150 * i + 20, 150), Gravity.TOP
                )
                player.setListener(this)
                player.add()
                mPlayersBlue[i] = player
            }

            for (i in PLAYER_RED_ARRAY.indices) {
                val player = Player(
                    applicationContext, i, PLAYER_RED_ARRAY[i],
                    Preference.get(context, namePrefixRed + (i + 1), ""),
                    mWindowManager, arrayOf(150 * i + 20, 0), Gravity.BOTTOM
                )
                player.setListener(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == MOVIE_FOLDER_LIST_REQUEST
                    && resultCode == Activity.RESULT_OK -> {
                val groupId = data!!.getLongExtra(MovieFolderListActivity.KEY_RESULT_POSITION, -1)
                if (groupId != -1L) {
                    selectTask(groupId)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.futsal_cort_setting -> {
                startSetting()
            }
            R.id.futsal_cort_create_movie -> {
                createMovie()
            }
            R.id.futsal_cort_movie_folder -> {
                startMovieList()
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

    @SuppressLint("SetTextI18n")
    @Synchronized
    override fun next(index: Int, id: Int, color: Int) {
        val key = color * VIEW_SIZE + id
        mFinishedMap.put(key, true)
        if (mFinishedMap.size() == VIEW_SIZE) {
            mFinishedMap.clear()

            val max = mPlayerDataArray.size / VIEW_SIZE - 1
            if (index <= max) {
                mMovieIndex.text = "$index/$max"
                mSeekBar.progress = index
            }

            for (player in mPlayersBlue) {
                player!!.next()
            }
            for (player in mPlayersRed) {
                player!!.next()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onPositiveButtonClicked(title: String) {
        GlobalScope.launch {
            try {
                val db = PlayerDataDatabase.getDatabases(applicationContext)
                val playerDao = db.playerDao()
                for (p in mPlayerDataArray) {
                    playerDao.insert(p)
                }

                val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val date = Date(System.currentTimeMillis())
                val formatted = sdf.format(date)

                val savedMovieListDao = db.savedMovieListDao()
                savedMovieListDao.insert(SavedMovieListData(0, mGroupId, title, formatted))
            } catch (e: Exception) {
                Log.e("FutsalCortActivity", "#saveTask", e)
            }
        }
        mMovieLayout.visibility = View.GONE
        mIndex = 0
        onDialogCallbackCommon()
    }

    override fun onNegativeButtonClicked() {
        onDialogCallbackCommon()
    }

    private fun onDialogCallbackCommon() {
        Log.d(TAG, "#onDialogCallbackCommon")
        for (player in mPlayersBlue) {
            player!!.add()
        }
        for (player in mPlayersRed) {
            player!!.add()
        }
    }

    private fun startSetting() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun createMovie() {
        mMovieLayout.visibility = View.VISIBLE
        mPlayerDataArray = ArrayList()
        mIndex = 0
        mMovieIndex.text = "0/$mIndex"
        mGroupId = System.currentTimeMillis()
        savePosition()
    }

    private fun startMovieList() {
        val intent = Intent(this, MovieFolderListActivity::class.java)
        intent.putExtra(KEY_BACKGROUND_COLOR, mBackgroundColor)
        startActivityForResult(intent, MOVIE_FOLDER_LIST_REQUEST)
    }

    @SuppressLint("SetTextI18n")
    private fun addMovie() {
        mIndex++
        mMovieIndex.text = "0/$mIndex"
        savePosition()
    }

    private fun savePosition() {
        for (p in mPlayersBlue) {
            val playerData = PlayerData(
                0, mGroupId, mIndex, ColorEnum.BLUE.id, p!!.mId, p.mName.text.toString(),
                p.mParams.x, p.mParams.y
            )
            mPlayerDataArray.add(playerData)
        }
        for (p in mPlayersRed) {
            val playerData = PlayerData(
                0, mGroupId, mIndex, ColorEnum.RED.id, p!!.mId, p.mName.text.toString(),
                p.mParams.x, p.mParams.y
            )
            mPlayerDataArray.add(playerData)
        }
    }

    private fun playMovie() {
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
        mSeekBar.max = mPlayerDataArray.size / VIEW_SIZE - 1
    }

    private fun saveMovie() {
        SaveMovieDialogFragment().show(this)
        for (player in mPlayersBlue) {
            player!!.remove()
        }
        for (player in mPlayersRed) {
            player!!.remove()
        }
    }

    private fun cancelMovie() {
        mMovieLayout.visibility = View.GONE
        mIndex = 0
    }

    @SuppressLint("SetTextI18n")
    private fun selectTask(groupId: Long) {
        val db = PlayerDataDatabase.getAllowMainThreadDatabases(applicationContext)
        val playerDao = db.playerDao()
        mPlayerDataArray = playerDao.selectGroup(groupId) as ArrayList<PlayerData>
        mMovieLayout.visibility = View.VISIBLE
        mMovieIndex.text = "0/" + (mPlayerDataArray.size / VIEW_SIZE - 1)
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