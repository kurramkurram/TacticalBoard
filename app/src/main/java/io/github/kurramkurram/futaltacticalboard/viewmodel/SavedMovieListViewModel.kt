package io.github.kurramkurram.futaltacticalboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.kurramkurram.futaltacticalboard.db.SavedMovieListData
import io.github.kurramkurram.futaltacticalboard.repository.SavedMovieListRepository

class SavedMovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: SavedMovieListRepository = SavedMovieListRepository(application)
    private val mLiveData: LiveData<List<SavedMovieListData>> = mRepository.getLiveData()

    fun getLiveData(): LiveData<List<SavedMovieListData>> = mLiveData

    fun insert(savedMovieListData: SavedMovieListData) = mRepository.insert(savedMovieListData)
}