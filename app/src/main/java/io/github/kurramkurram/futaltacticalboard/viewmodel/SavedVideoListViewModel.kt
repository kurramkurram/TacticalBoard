package io.github.kurramkurram.futaltacticalboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.kurramkurram.futaltacticalboard.db.SavedVideoListData
import io.github.kurramkurram.futaltacticalboard.repository.SavedVideoListRepository

class SavedVideoListViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: SavedVideoListRepository = SavedVideoListRepository(application)
    private val mLiveData: LiveData<List<SavedVideoListData>> = mRepository.getLiveData()

    fun getLiveData(): LiveData<List<SavedVideoListData>> = mLiveData

    fun insert(savedVideoListData: SavedVideoListData) = mRepository.insert(savedVideoListData)
}