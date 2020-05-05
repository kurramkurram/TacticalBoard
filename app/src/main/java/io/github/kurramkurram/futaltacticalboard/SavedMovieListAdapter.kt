package io.github.kurramkurram.futaltacticalboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.github.kurramkurram.futaltacticalboard.db.SavedMovieListData

class SavedMovieListAdapter(
    context: Context,
    resource: Int,
    items: List<SavedMovieListData>
) : ArrayAdapter<SavedMovieListData>(context, resource, items) {

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
        val title = view!!.findViewById<TextView>(R.id.saved_movie_list_title)
        title.text = item.title

        val date = view.findViewById<TextView>(R.id.saved_movie_list_date)
        date.text = item.date

        return view
    }
}