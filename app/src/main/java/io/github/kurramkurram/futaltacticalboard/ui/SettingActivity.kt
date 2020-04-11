package io.github.kurramkurram.futaltacticalboard.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import io.github.kurramkurram.futaltacticalboard.R

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val pagerAdapter = SettingPagerAdapter()
        val pager = findViewById<ViewPager>(R.id.setting_view_pager)
        pager.adapter = pagerAdapter
    }

    class SettingPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentStatePagerAdapter(fm, behavior) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> SettingCortEditFragment()
                1 -> SettingTeamEditFragment()
                else -> 
            }
        }

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence? {
            return "pageTitle $position"
        }
    }
}