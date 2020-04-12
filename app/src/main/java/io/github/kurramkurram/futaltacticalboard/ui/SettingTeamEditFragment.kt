package io.github.kurramkurram.futaltacticalboard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kurramkurram.futaltacticalboard.R

class SettingTeamEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}