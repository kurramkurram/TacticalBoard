package io.github.kurramkurram.futaltacticalboard.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import io.github.kurramkurram.futaltacticalboard.Preference
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

    private lateinit var mSwitch: Switch

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
        mSwitch = view.findViewById(R.id.setting_half_cort)
        mSwitch.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v!!.id
        if (CORT_COLOR_ARRAY.contains(id)) {
            val viewPager = activity!!.findViewById<ViewPager>(R.id.setting_view_pager)
            val index = CORT_COLOR_ARRAY.indexOf(id)
            val backgroundColorArray =
                context!!.resources.obtainTypedArray(R.array.setting_background_array)
            val drawable = backgroundColorArray.getDrawable(index)
            viewPager.background = drawable
            backgroundColorArray.recycle()
            Preference.set(context!!, Preference.KEY_BACKGROUND_RESOURCE_INDEX, index)
        } else {
            val isHalf = mSwitch.isChecked
            Log.d("SettingCortEditFragment", "#onClick $isHalf")
            Preference.set(context!!, Preference.KEY_HALF_CORT, isHalf)
        }
    }
}