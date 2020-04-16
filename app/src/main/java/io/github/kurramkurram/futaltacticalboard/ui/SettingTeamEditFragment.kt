package io.github.kurramkurram.futaltacticalboard.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import io.github.kurramkurram.futaltacticalboard.Preference
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
            R.id.player_edit_reset_button -> {
                for (e in mPlayerEditTexts) {
                    e!!.editableText.clear()
                }
                var colorName = Preference.KEY_PLAYER_NAME_COLOR_BLUE
                if (!mTeamRed.isEnabled) { // 赤が無効の時、赤を編集中
                    colorName = Preference.KEY_PLAYER_NAME_COLOR_RED
                }

                for ((count, name) in mPlayerEditTexts.withIndex()) {
                    Preference.set(
                        context!!,
                        Preference.KEY_PLAYER_NAME_PREFIX + colorName + (count + 1),
                        ""
                    )
                }
            }
            R.id.player_edit_ok_button -> {
                savePlayerName()
                Toast.makeText(context!!, "CONFIRMED!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editPlayerName(array: Array<Int>) {
        var beforeName =
            Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_BLUE
        var afterName =
            Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_RED
        if (FutsalCortActivity.PLAYER_BLUE_ARRAY.contentEquals(array)) {
            beforeName = Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_RED
            afterName = Preference.KEY_PLAYER_NAME_PREFIX + Preference.KEY_PLAYER_NAME_COLOR_BLUE
        }

        for ((count, name) in mPlayerEditTexts.withIndex()) {
            Preference.set(context!!, beforeName + (count + 1), name!!.text.toString())
        }

        for (e in mPlayerEditTexts) {
            e!!.editableText.clear()
        }

        for ((count, image) in mPlayerIcons.withIndex()) {
            image!!.setImageResource(array[count])
            val name = Preference.get(context!!, afterName + (count + 1), "")
            mPlayerEditTexts[count]!!.setText(name)
        }

        mPlayerEditLayout.visibility = View.VISIBLE
    }

    private fun savePlayerName() {
        var colorName = Preference.KEY_PLAYER_NAME_COLOR_BLUE
        if (!mTeamRed.isEnabled) { // 赤が無効の時、赤を編集中
            colorName = Preference.KEY_PLAYER_NAME_COLOR_RED
        }

        for ((count, name) in mPlayerEditTexts.withIndex()) {
            Preference.set(
                context!!,
                Preference.KEY_PLAYER_NAME_PREFIX + colorName + (count + 1),
                name!!.text.toString()
            )
        }
    }
}