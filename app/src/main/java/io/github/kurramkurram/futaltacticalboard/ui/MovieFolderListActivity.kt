package io.github.kurramkurram.futaltacticalboard.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.kurramkurram.futaltacticalboard.R
import io.github.kurramkurram.futaltacticalboard.SavedMovieListAdapter
import io.github.kurramkurram.futaltacticalboard.db.SavedMovieListData
import io.github.kurramkurram.futaltacticalboard.viewmodel.SavedMovieListViewModel

class MovieFolderListActivity : AppCompatActivity() {

    companion object {
        const val KEY_RESULT_POSITION = "key_result_position"
    }

    private lateinit var mViewModel: SavedMovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_folder_list)

        mViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                SavedMovieListViewModel::class.java
            )

        val listView = findViewById<ListView>(R.id.movie_folder_list)
        listView.setBackgroundColor(intent.getIntExtra(FutsalCortActivity.KEY_BACKGROUND_COLOR, -1))
        mViewModel.getLiveData().observe(this,
            Observer<List<SavedMovieListData>> { t ->
                val arrayAdapter =
                    SavedMovieListAdapter(applicationContext, R.layout.saved_movie_list_item, t!!)
                listView.adapter = arrayAdapter
                listView.setOnItemClickListener { _, _, position, _ ->
                    val intent = Intent()
                    val groupId = t[position].groupId
                    intent.putExtra(KEY_RESULT_POSITION, groupId)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            })
    }
}