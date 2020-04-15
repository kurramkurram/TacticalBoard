package io.github.kurramkurram.futaltacticalboard.ui

import android.content.res.TypedArray
import android.os.Bundle
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
    private lateinit var mViewPager: ViewPager
    private lateinit var mBackgroundColorArray: TypedArray

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
        mViewPager = activity!!.findViewById(R.id.setting_view_pager)
        mBackgroundColorArray =
            context!!.resources.obtainTypedArray(R.array.setting_background_array)
        val index = Preference.get(context!!, Preference.KEY_BACKGROUND_RESOURCE_INDEX, 0)
        mViewPager.background = mBackgroundColorArray.getDrawable(index)

        mSwitch.isChecked = Preference.get(context!!, Preference.KEY_HALF_CORT, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBackgroundColorArray.recycle()
    }

    override fun onClick(v: View?) {
        val id = v!!.id
        if (CORT_COLOR_ARRAY.contains(id)) {
            val index = CORT_COLOR_ARRAY.indexOf(id)
            val drawable = mBackgroundColorArray.getDrawable(index)
            mViewPager.background = drawable
            Preference.set(context!!, Preference.KEY_BACKGROUND_RESOURCE_INDEX, index)
        } else {
            val isHalf = mSwitch.isChecked
            Preference.set(context!!, Preference.KEY_HALF_CORT, isHalf)
        }
    }
}