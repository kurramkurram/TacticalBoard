package io.github.kurramkurram.futaltacticalboard.repository

import android.app.Application
import androidx.lifecycle.LiveData
import io.github.kurramkurram.futaltacticalboard.db.PlayerDataDatabase
import io.github.kurramkurram.futaltacticalboard.db.SavedVideoListDao
import io.github.kurramkurram.futaltacticalboard.db.SavedVideoListData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SavedVideoListRepository(application: Application) {

    private val mDb: PlayerDataDatabase = PlayerDataDatabase.getDatabases(application)
    private val mDao: SavedVideoListDao = mDb.savedVideoListDao()
    private val mLiveData: LiveData<List<SavedVideoListData>> = mDao.selectAll()

    fun getLiveData(): LiveData<List<SavedVideoListData>> = mLiveData

    fun insert(savedVideoListData: SavedVideoListData) =
        GlobalScope.launch {
            mDao.insert(savedVideoListData)
        }
}