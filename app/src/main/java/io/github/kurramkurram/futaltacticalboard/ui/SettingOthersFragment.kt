package io.github.kurramkurram.futaltacticalboard.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.github.kurramkurram.futaltacticalboard.R
import io.github.kurramkurram.futaltacticalboard.SettingOthersListAdapter
import io.github.kurramkurram.futaltacticalboard.SettingOthersListItem
import io.github.kurramkurram.futaltacticalboard.ui.info.AppInfoActivity

class SettingOthersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_others, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.setting_other_list_view)


        val itemList = ArrayList<SettingOthersListItem>()
        val appInfoText = context!!.resources.getString(R.string.app_info)
        val item = SettingOthersListItem(appInfoText, true)
        itemList.add(item)

        val adapter =
            SettingOthersListAdapter(context!!, R.layout.setting_others_list_item, itemList)
        listView.adapter = adapter
        listView.setOnItemClickListener { _,
                                          v,
                                          _,
                                          _ ->
            when (v.findViewById<TextView>(R.id.list_item_name).text) {
                appInfoText -> {
                    val intent = Intent(context, AppInfoActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
