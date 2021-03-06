package io.github.kurramkurram.futaltacticalboard.ui

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.github.kurramkurram.futaltacticalboard.Preference
import io.github.kurramkurram.futaltacticalboard.R
import io.github.kurramkurram.futaltacticalboard.showToast

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

        for (i in PLAYER_EDIT_TEXT_IDS.indices) {
            mPlayerEditTexts[i] = view.findViewById(PLAYER_EDIT_TEXT_IDS[i])
        }

        val resetButton = view.findViewById<TextView>(R.id.player_edit_reset_button)
        resetButton.setOnClickListener(this)
        val okButton = view.findViewById<TextView>(R.id.player_edit_ok_button)
        okButton.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
        savePlayerName()
    }

    @SuppressLint("ShowToast")
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.edit_player_blue -> {
                mTeamBlue.isEnabled = !mTeamBlue.isEnabled
                mTeamRed.isEnabled = true
                editBluePlayerName()
            }
            R.id.edit_player_red -> {
                mTeamRed.isEnabled = !mTeamRed.isEnabled
                mTeamBlue.isEnabled = true
                editRedPlayerName()
            }
            R.id.player_edit_reset_button -> {
                for (e in mPlayerEditTexts) {
                    e!!.editableText.clear()
                }
                var colorName = Preference.KEY_PLAYER_NAME_COLOR_BLUE
                if (!mTeamRed.isEnabled) { // 赤が無効の時、赤を編集中
                    colorName = Preference.KEY_PLAYER_NAME_COLOR_RED
                }

                for (count in mPlayerEditTexts.indices) {
                    Preference.set(
                        context!!,
                        Preference.KEY_PLAYER_NAME_PREFIX + colorName + (count + 1),
                        ""
                    )
                }
            }
            R.id.player_edit_ok_button -> {
                savePlayerName()
                context!!.showToast("CONFIRMED!!")
            }
        }
    }

    private fun editBluePlayerName() {
        val beforeName = Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_RED
        val afterName = Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_BLUE
        val playerBlueArray = resources.obtainTypedArray(R.array.player_blue)
        editPlayerName(beforeName, afterName, playerBlueArray)
        playerBlueArray.recycle()
    }

    private fun editRedPlayerName() {
        val beforeName = Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_BLUE
        val afterName = Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_RED
        val playerRedArray = resources.obtainTypedArray(R.array.player_red)
        editPlayerName(beforeName, afterName, playerRedArray)
        playerRedArray.recycle()
    }

    private fun editPlayerName(beforeName: String, afterName: String, array: TypedArray) {
        mPlayerEditTexts.withIndex().forEach {
            val inputName = it.value!!.text.toString()
            val name = if ("" == inputName) {
                Preference.get(context!!, beforeName + (it.index + 1), "")
            } else {
                inputName
            }
            Preference.set(context!!, beforeName + (it.index + 1), name!!)
        }

        for (e in mPlayerEditTexts) {
            e!!.editableText.clear()
        }

        mPlayerIcons.withIndex().forEach {
            it.value!!.setImageDrawable(array.getDrawable(it.index))
            val name = Preference.get(context!!, afterName + (it.index + 1), "")
            Log.d(
                "SettingTeamEditFragment",
                "#editPlayer afterName = $afterName index =  ${it.index} name = $name"
            )
            mPlayerEditTexts[it.index]!!.setText(name)
        }

        mPlayerEditLayout.visibility = View.VISIBLE
    }

    private fun savePlayerName() {
        var colorName = Preference.KEY_PLAYER_NAME_COLOR_BLUE
        if (!mTeamRed.isEnabled) { // 赤が無効の時、赤を編集中
            colorName = Preference.KEY_PLAYER_NAME_COLOR_RED
        }

        mPlayerEditTexts.withIndex().forEach {
            Preference.set(
                context!!,
                Preference.KEY_PLAYER_NAME_PREFIX + colorName + (it.index + 1),
                it.value!!.text.toString()
            )
        }
    }
}