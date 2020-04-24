package io.github.kurramkurram.futaltacticalboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class SettingOthersListAdapter(
    context: Context,
    resource: Int,
    items: List<SettingOthersListItem>
) : ArrayAdapter<SettingOthersListItem>(context, resource, items) {

    private val mResource = resource
    private val mItems = items
    private val mInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = mInflater.inflate(mResource, null)
        }
        val item = mItems[position]
        val name = view!!.findViewById<TextView>(R.id.list_item_name)
        name.text = item.text

        val arrow = view.findViewById<ImageView>(R.id.list_item_arrow)
        arrow.visibility = if (item.isShowArrow) View.VISIBLE else View.INVISIBLE

        return view
    }
}