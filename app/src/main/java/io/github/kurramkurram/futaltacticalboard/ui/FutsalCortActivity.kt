package io.github.kurramkurram.futaltacticalboard.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.kurramkurram.futaltacticalboard.ColorEnum
import io.github.kurramkurram.futaltacticalboard.PlayerLayout
import io.github.kurramkurram.futaltacticalboard.Preference
import io.github.kurramkurram.futaltacticalboard.R
import io.github.kurramkurram.futaltacticalboard.db.PlayerData
import io.github.kurramkurram.futaltacticalboard.db.PlayerDataDatabase
import io.github.kurramkurram.futaltacticalboard.db.SavedVideoListData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FutsalCortActivity : AppCompatActivity(), View.OnClickListener,
    PlayerLayout.OnAnimationCallback, SavedVideoDialogFragment.OnDialogResultCallback {

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
        private const val VIDEO_FOLDER_LIST_REQUEST = 0
        const val KEY_BACKGROUND_COLOR = "key_background_color"

        private const val VIEW_SIZE = 10

        const val PLAYER_INIT_POSITION_X = 20
        const val PLAYER_INIT_POSITION_OFFSET = 150
        const val PLAYER_INIT_POSITION_Y = 150


        const val NAME_PREFIX_BLUE =
            Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_BLUE
        const val NAME_PREFIX_RED =
            Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_RED
    }

    private var mPlayersBlue = arrayOfNulls<PlayerLayout>(PLAYER_BLUE_ARRAY.size)
    private var mPlayersRed = arrayOfNulls<PlayerLayout>(PLAYER_RED_ARRAY.size)
    private lateinit var mCortLayout: ConstraintLayout
    private lateinit var mDrawIcon: ImageView
    private lateinit var mDeleteIcon: ImageView
    private lateinit var mLine: DrawLine

    private lateinit var mSeekBar: SeekBar
    private lateinit var mVideoIndex: TextView

    private lateinit var mVideoLayout: LinearLayout

    private var mBackgroundColor: Int = 0

    private var mPlayerDataArray: ArrayList<PlayerData> = ArrayList()

    private var mGroupId = 0L
    private var mIndex = 0
    private var mFinishedMap = SparseArray<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cort)

        mCortLayout = findViewById(R.id.futsal_cort)

        val settings = findViewById<ImageView>(R.id.futsal_cort_setting)
        settings.setOnClickListener(this)

        val createVideo = findViewById<ImageView>(R.id.futsal_cort_create_video)
        createVideo.setOnClickListener(this)

        val videoFolder = findViewById<ImageView>(R.id.futsal_cort_video_folder)
        videoFolder.setOnClickListener(this)

        mDrawIcon = findViewById(R.id.futsal_cort_draw_line)
        mDrawIcon.setOnClickListener(this)

        mDeleteIcon = findViewById(R.id.futsal_cort_delete_line)
        mDeleteIcon.setOnClickListener(this)

        val addVideo = findViewById<ImageView>(R.id.add_video_edit)
        addVideo.setOnClickListener(this)

        val playVideo = findViewById<ImageView>(R.id.play_video_edit)
        playVideo.setOnClickListener(this)

        val saveVideo = findViewById<ImageView>(R.id.save_video_edit)
        saveVideo.setOnClickListener(this)

        val cancelVideo = findViewById<ImageView>(R.id.cancel_video_edit)
        cancelVideo.setOnClickListener(this)

        mVideoLayout = findViewById(R.id.futsal_cort_video_layout)
        mSeekBar = findViewById(R.id.seek_bar_video_edit)
        mSeekBar.isEnabled = false

        mVideoIndex = findViewById(R.id.index_play_video_edit)

        initPlayer(applicationContext)
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

        mVideoLayout.setBackgroundColor(mBackgroundColor)

        val isHalf = Preference.get(context, Preference.KEY_HALF_CORT, false)

        var drawableArray = resources.obtainTypedArray(R.array.tactical_board_array)

        if (isHalf) {
            drawableArray = resources.obtainTypedArray(R.array.tactical_board_half_array)
        }

        val drawable = drawableArray.getDrawable(backgroundIndex)
        background.setImageDrawable(drawable)
        drawableArray.recycle()

        for ((i, p) in mPlayersBlue.withIndex()) {
            val name = Preference.get(context, NAME_PREFIX_BLUE + (i + 1), "") as String
            p!!.setName(name)
        }
        for ((i, p) in mPlayersRed.withIndex()) {
            val name = Preference.get(context, NAME_PREFIX_RED + (i + 1), "") as String
            p!!.setName(name)
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
            requestCode == VIDEO_FOLDER_LIST_REQUEST
                    && resultCode == Activity.RESULT_OK -> {
                val groupId = data!!.getLongExtra(VideoFolderListActivity.KEY_RESULT_POSITION, -1)
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
            R.id.futsal_cort_create_video -> {
                createVideo()
            }
            R.id.futsal_cort_video_folder -> {
                startVideoList()
            }
            R.id.add_video_edit -> {
                addVideo()
            }
            R.id.play_video_edit -> {
                playVideo()
            }
            R.id.save_video_edit -> {
                saveVideo()
            }
            R.id.cancel_video_edit -> {
                cancelVideo()
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
                mVideoIndex.text = "$index/$max"
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

                val savedVideoListDao = db.savedVideoListDao()
                savedVideoListDao.insert(SavedVideoListData(0, mGroupId, title, formatted))
            } catch (e: Exception) {
                Log.e(TAG, "#saveTask", e)
            }
        }
        mVideoLayout.visibility = View.GONE
        mIndex = 0
    }

    override fun onNegativeButtonClicked() {
        // do nothing
    }

    private fun initPlayer(context: Context) {
        for (i in PLAYER_BLUE_ARRAY.indices) {
            val name = Preference.get(context, NAME_PREFIX_BLUE + (i + 1), "") as String
            val player =
                PlayerLayout(
                    applicationContext,
                    i,
                    PLAYER_BLUE_ARRAY[i],
                    ColorEnum.BLUE,
                    name,
                    PLAYER_INIT_POSITION_X + i * PLAYER_INIT_POSITION_OFFSET,
                    PLAYER_INIT_POSITION_Y, this
                )
            player.add(mCortLayout)
            mPlayersBlue[i] = player
        }

        for (i in PLAYER_RED_ARRAY.indices) {
            val name = Preference.get(context, NAME_PREFIX_RED + (i + 1), "") as String
            val player =
                PlayerLayout(
                    applicationContext,
                    i,
                    PLAYER_RED_ARRAY[i],
                    ColorEnum.RED,
                    name,
                    PLAYER_INIT_POSITION_X + i * PLAYER_INIT_POSITION_OFFSET,
                    PLAYER_INIT_POSITION_Y + PLAYER_INIT_POSITION_OFFSET, this
                )
            player.add(mCortLayout)
            mPlayersRed[i] = player
        }
    }

    private fun startSetting() {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun createVideo() {
        mVideoLayout.visibility = View.VISIBLE
        mPlayerDataArray = ArrayList()
        mIndex = 0
        mVideoIndex.text = "0/$mIndex"
        mGroupId = System.currentTimeMillis()
        savePosition()
    }

    private fun startVideoList() {
        val intent = Intent(this, VideoFolderListActivity::class.java)
        intent.putExtra(KEY_BACKGROUND_COLOR, mBackgroundColor)
        startActivityForResult(intent, VIDEO_FOLDER_LIST_REQUEST)
    }

    @SuppressLint("SetTextI18n")
    private fun addVideo() {
        mIndex++
        mVideoIndex.text = "0/$mIndex"
        savePosition()
    }

    private fun savePosition() {
        for (p in mPlayersBlue) {
            val playerData = PlayerData(
                0,
                mGroupId,
                mIndex,
                ColorEnum.BLUE.id,
                p!!.mId,
                p.mName.text.toString(),
                p.x.toInt(),
                p.y.toInt()
            )
            mPlayerDataArray.add(playerData)
        }
        for (p in mPlayersRed) {
            val playerData = PlayerData(
                0,
                mGroupId,
                mIndex,
                ColorEnum.RED.id,
                p!!.mId,
                p.mName.text.toString(),
                p.x.toInt(),
                p.y.toInt()
            )
            mPlayerDataArray.add(playerData)
        }
    }

    private fun playVideo() {
        for (playerData in mPlayerDataArray) {
            val color = playerData.playerColor
            val playerId = playerData.playerId
            val x = playerData.playerX
            val y = playerData.playerY

            when (color) {
                ColorEnum.BLUE.id -> {
                    for (player in mPlayersBlue) {
                        if (player!!.mId == playerId) {
                            player.putPoint(arrayOf(x, y))
                        }
                    }
                }
                ColorEnum.RED.id -> {
                    for (player in mPlayersRed) {
                        if (player!!.mId == playerId) {
                            player.putPoint(arrayOf(x, y))
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

    private fun saveVideo() {
        SavedVideoDialogFragment().show(this)
    }

    private fun cancelVideo() {
        mVideoLayout.visibility = View.GONE
        mIndex = 0
    }

    @SuppressLint("SetTextI18n")
    private fun selectTask(groupId: Long) {
        val db = PlayerDataDatabase.getAllowMainThreadDatabases(applicationContext)
        val playerDao = db.playerDao()
        mPlayerDataArray = playerDao.selectGroup(groupId) as ArrayList<PlayerData>
        mVideoLayout.visibility = View.VISIBLE
        mVideoIndex.text = "0/" + (mPlayerDataArray.size / VIEW_SIZE - 1)
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