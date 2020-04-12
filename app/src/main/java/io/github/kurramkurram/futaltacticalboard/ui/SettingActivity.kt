package io.github.kurramkurram.futaltacticalboard.ui

import android.content.Context
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.viewpager.widget.ViewPager

import io.github.kurramkurram.futaltacticalboard.R

class SettingActivity : AppCompatActivity() {

    private val mFragmentManager = supportFragmentManager

    companion object {
        const val SETTING_CORT_EDIT = 0
        const val SETTING_TEAM_EDIT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val pagerAdapter =
            SettingPagerAdapter(
                mFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                applicationContext
            )
        val pager = findViewById<ViewPager>(R.id.setting_view_pager)
        pager.adapter = pagerAdapter
    }

    class SettingPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentStatePagerAdapter(fm, behavior) {

        private lateinit var mContext: Context

        constructor(fm: FragmentManager, behavior: Int, context: Context) : this(fm, behavior) {
            mContext = context
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                SETTING_CORT_EDIT -> SettingCortEditFragment()
                SETTING_TEAM_EDIT -> SettingTeamEditFragment()
                else -> SettingOthersFragment()
            }
        }

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                SETTING_CORT_EDIT -> mContext.resources.getString(R.string.setting_tab_name_1)
                SETTING_TEAM_EDIT -> mContext.resources.getString(R.string.setting_tab_name_2)
                else -> mContext.resources.getString(R.string.setting_tab_name_3)
            }
        }
    }
}