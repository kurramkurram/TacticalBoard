package io.github.kurramkurram.futaltacticalboard.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.kurramkurram.futaltacticalboard.R
import io.github.kurramkurram.futaltacticalboard.SavedVideoListAdapter
import io.github.kurramkurram.futaltacticalboard.db.SavedVideoListData
import io.github.kurramkurram.futaltacticalboard.viewmodel.SavedVideoListViewModel

class VideoFolderListActivity : AppCompatActivity() {

    companion object {
        const val KEY_RESULT_POSITION = "key_result_position"
    }

    private lateinit var mViewModel: SavedVideoListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_folder_list)

        mViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                SavedVideoListViewModel::class.java
            )

        val textView = findViewById<TextView>(R.id.video_folder_no_exist_text)
        val color = intent.getIntExtra(FutsalCortActivity.KEY_BACKGROUND_COLOR, -1)
        val listView = findViewById<ListView>(R.id.video_folder_list)
        mViewModel.getLiveData().observe(this,
            Observer<List<SavedVideoListData>> { t ->

                if (t.isNotEmpty()) {
                    listView.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                    listView.setBackgroundColor(color)
                    val arrayAdapter =
                        SavedVideoListAdapter(
                            applicationContext,
                            R.layout.saved_video_list_item,
                            t!!
                        )
                    listView.adapter = arrayAdapter
                    listView.setOnItemClickListener { _, _, position, _ ->
                        val intent = Intent()
                        val groupId = t[position].groupId
                        intent.putExtra(KEY_RESULT_POSITION, groupId)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                } else {
                    listView.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                    textView.setBackgroundColor(color)
                }
            })
    }
}