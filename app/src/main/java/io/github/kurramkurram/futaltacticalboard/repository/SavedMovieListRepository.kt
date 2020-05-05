package io.github.kurramkurram.futaltacticalboard.repository

import android.app.Application
import androidx.lifecycle.LiveData
import io.github.kurramkurram.futaltacticalboard.db.PlayerDataDatabase
import io.github.kurramkurram.futaltacticalboard.db.SavedMovieListDao
import io.github.kurramkurram.futaltacticalboard.db.SavedMovieListData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SavedMovieListRepository(application: Application) {

    private val mDao: SavedMovieListDao
    private val mLiveData: LiveData<List<SavedMovieListData>>

    init {
        val db = PlayerDataDatabase.getDatabases(application)
        mDao = db.savedMovieListDao()
        mLiveData = mDao.selectAll()
    }

    fun getLiveData(): LiveData<List<SavedMovieListData>> = mLiveData

    fun insert(savedMovieListData: SavedMovieListData) =
        GlobalScope.launch {
            mDao.insert(savedMovieListData)
        }
}