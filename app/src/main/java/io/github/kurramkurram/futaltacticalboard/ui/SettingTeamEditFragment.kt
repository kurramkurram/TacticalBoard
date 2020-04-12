package io.github.kurramkurram.futaltacticalboard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import io.github.kurramkurram.futaltacticalboard.R

class SettingTeamEditFragment : Fragment(), View.OnClickListener {

    private lateinit var mPlayerEditLayout: LinearLayout
    private lateinit var mTeamRed: ImageView
    private lateinit var mTeamBlue: ImageView

    private val mPlayerIcons = arrayOfNulls<ImageView>(PLAYER_ICON_IDS.size)
    private val mPlayerEditTexts = arrayOfNulls<EditText>(PLAYER_EDIT_TEXT_IDS.size)

    companion object {
        val PLAYER_ICON_IDS = arrayOf(
            R.id.setting_player_1,
            R.id.setting_player_2,
            R.id.setting_player_3,
            R.id.setting_player_4,
            R.id.setting_player_5
        )
        val PLAYER_EDIT_TEXT_IDS = arrayOf(
            R.id.player_name_edit_text_1,
            R.id.player_name_edit_text_2,
            R.id.player_name_edit_text_3,
            R.id.player_name_edit_text_4,
            R.id.player_name_edit_text_5
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mTeamRed = view.findViewById(R.id.edit_player_red)
        mTeamRed.setOnClickListener(this)
        mTeamRed.isEnabled = true
        mTeamBlue = view.findViewById(R.id.edit_player_blue)
        mTeamBlue.setOnClickListener(this)
        mTeamBlue.isEnabled = true
        mPlayerEditLayout = view.findViewById(R.id.player_edit_layout)

        for (i in PLAYER_ICON_IDS.indices) {
            mPlayerIcons[i] = view.findViewById(PLAYER_ICON_IDS[i])
        }

        for (i in PLAYER_ICON_IDS.indices) {
            mPlayerEditTexts[i] = view.findViewById(PLAYER_EDIT_TEXT_IDS[i])
        }
    }

    override fun onPause() {
        super.onPause()
        // TODO：状態保存
    }

    override fun onClick(v: View?) {
        val array: Array<Int>
        when (v!!.id) {
            R.id.edit_player_blue -> {
                mTeamBlue.isEnabled = !mTeamBlue.isEnabled
                mTeamRed.isEnabled = true
                array = FutsalCortActivity.PLAYER_BLUE_ARRAY
                editPlayerName(array)
            }
            R.id.edit_player_red -> {
                mTeamRed.isEnabled = !mTeamRed.isEnabled
                mTeamBlue.isEnabled = true
                array = FutsalCortActivity.PLAYER_RED_ARRAY
                editPlayerName(array)
            }
        }
    }

    private fun editPlayerName(array: Array<Int>) {
        // TODO：状態保存


        for ((count, image) in mPlayerIcons.withIndex()) {
            image!!.setImageResource(array[count])
        }

        for (e in mPlayerEditTexts) {
            e!!.editableText.clear()
        }

        mPlayerEditLayout.visibility = View.VISIBLE
    }
}