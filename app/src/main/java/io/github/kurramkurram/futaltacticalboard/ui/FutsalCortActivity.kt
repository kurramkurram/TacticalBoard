package io.github.kurramkurram.futaltacticalboard.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import io.github.kurramkurram.futaltacticalboard.Player
import io.github.kurramkurram.futaltacticalboard.R

class FutsalCortActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var mWindowManager: WindowManager
    private var mPlayersBlue = arrayOfNulls<Player>(5)
    private var mPlayersRed = arrayOfNulls<Player>(5)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val settings = findViewById<ImageView>(R.id.futsal_cort_setting)
        settings.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        if (mPlayersBlue[0] == null) {
            for (i in 0..4) {
                val player = Player(
                    applicationContext,
                    PLAYER_BLUE_ARRAY[i],
                    "",
                    mWindowManager,
                    arrayOf(150 * i, 0),
                    Gravity.TOP
                )
                player.add()
                mPlayersBlue[i] = player
            }

            for (i in 0..4) {
                val player = Player(
                    applicationContext,
                    PLAYER_RED_ARRAY[i],
                    "",
                    mWindowManager,
                    arrayOf(150 * i, 0),
                    Gravity.BOTTOM
                )
                player.add()
                mPlayersRed[i] = player
            }
        } else {
            for (p in mPlayersBlue) {
                p!!.add()
            }
            for (p in mPlayersRed) {
                p!!.add()
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
        mPlayersBlue = arrayOfNulls(5)
        mPlayersRed = arrayOfNulls(5)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.futsal_cort_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}