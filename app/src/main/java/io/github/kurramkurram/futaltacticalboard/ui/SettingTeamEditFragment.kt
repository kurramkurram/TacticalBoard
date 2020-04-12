package io.github.kurramkurram.futaltacticalboard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import io.github.kurramkurram.futaltacticalboard.R

class SettingTeamEditFragment : Fragment(), View.OnClickListener {

    private lateinit var mPlayerEditLayout: LinearLayout
    private lateinit var mTeamRed: ImageView
    private lateinit var mTeamBlue: ImageView
    private lateinit var mSpinner: Spinner

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

        val adapter = context?.let { ArrayAdapter<Int>(it, android.R.layout.simple_list_item_1) }
        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        for (i in FutsalCortActivity.PLAYER_RED_ARRAY.indices) {
            adapter.add(i + 1)
        }
        mSpinner = view.findViewById(R.id.player_number_spinner)
        mSpinner.adapter = adapter
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.edit_player_blue -> {
                mTeamBlue.isEnabled = !mTeamBlue.isEnabled
                mTeamRed.isEnabled = true
                editPlayerName()
            }
            R.id.edit_player_red -> {
                mTeamRed.isEnabled = !mTeamRed.isEnabled
                mTeamBlue.isEnabled = true
                editPlayerName()
            }
        }
    }

    private fun editPlayerName() {
        mPlayerEditLayout.visibility = View.VISIBLE
    }
}