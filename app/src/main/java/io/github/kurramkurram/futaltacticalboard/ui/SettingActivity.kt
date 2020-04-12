package io.github.kurramkurram.futaltacticalboard.ui

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
            SettingPagerAdapter(mFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        val pager = findViewById<ViewPager>(R.id.setting_view_pager)
        pager.adapter = pagerAdapter
    }

    class SettingPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentStatePagerAdapter(fm, behavior) {

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
                SETTING_CORT_EDIT -> "CORT"
                SETTING_TEAM_EDIT -> "TEAM"
                else -> "OTHERS"
            }
        }
    }
}