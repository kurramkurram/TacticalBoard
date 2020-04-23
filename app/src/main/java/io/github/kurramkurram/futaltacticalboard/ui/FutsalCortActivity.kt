package io.github.kurramkurram.futaltacticalboard.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.kurramkurram.futaltacticalboard.Player
import io.github.kurramkurram.futaltacticalboard.Preference
import io.github.kurramkurram.futaltacticalboard.R

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
    }

    private lateinit var mWindowManager: WindowManager
    private var mPlayersBlue = arrayOfNulls<Player>(PLAYER_BLUE_ARRAY.size)
    private var mPlayersRed = arrayOfNulls<Player>(PLAYER_RED_ARRAY.size)
    private lateinit var mCortLayout: ConstraintLayout
    private lateinit var mDrawIcon: ImageView
    private lateinit var mDeleteIcon: ImageView
    private lateinit var mLine: DrawLine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        mCortLayout = findViewById(R.id.futsal_cort)

        val settings = findViewById<ImageView>(R.id.futsal_cort_setting)
        settings.setOnClickListener(this)

        mDrawIcon = findViewById(R.id.futsal_cort_draw_line)
        mDrawIcon.setOnClickListener(this)

        mDeleteIcon = findViewById(R.id.futsal_cort_delete_line)
        mDeleteIcon.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

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
                    applicationContext,
                    PLAYER_BLUE_ARRAY[i],
                    Preference.get(context, namePrefixBlue + (i + 1), ""),
                    mWindowManager,
                    arrayOf(150 * i, 0),
                    Gravity.TOP
                )
                player.add()
                mPlayersBlue[i] = player
            }

            for (i in PLAYER_RED_ARRAY.indices) {
                val player = Player(
                    applicationContext,
                    PLAYER_RED_ARRAY[i],
                    Preference.get(context, namePrefixRed + (i + 1), ""),
                    mWindowManager,
                    arrayOf(150 * i, 0),
                    Gravity.BOTTOM
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
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.futsal_cort_draw_line -> {
                mDrawIcon.visibility = View.GONE
                mDeleteIcon.visibility = View.VISIBLE
                mLine = DrawLine(applicationContext)
                mCortLayout.addView(mLine, 1)
            }
            R.id.futsal_cort_delete_line -> {
                mCortLayout.removeView(mLine)
                mDeleteIcon.visibility = View.GONE
                mDrawIcon.visibility = View.VISIBLE
            }
        }
    }
}