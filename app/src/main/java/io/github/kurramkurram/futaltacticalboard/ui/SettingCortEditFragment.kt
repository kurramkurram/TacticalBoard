package io.github.kurramkurram.futaltacticalboard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kurramkurram.futaltacticalboard.R

class SettingCortEditFragment : Fragment(), View.OnClickListener {

    companion object {
        val CORT_COLOR_ARRAY = arrayOf(
            R.id.setting_cort_color_0,
            R.id.setting_cort_color_1,
            R.id.setting_cort_color_2,
            R.id.setting_cort_color_3
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cort_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        for (i in CORT_COLOR_ARRAY) {
            val cortColor = view.findViewById<View>(i)
            cortColor.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when {
            // TODO:
            // CORT_COLOR_ARRAY.contains(v!!.id)
        }
    }
}