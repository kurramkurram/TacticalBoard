package io.github.kurramkurram.futaltacticalboard.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedMovieListDao {

    @Query("SELECT* FROM t_saved_movie_info")
    fun selectAll(): LiveData<List<SavedMovieListData>>

    @Insert
    suspend fun insert(data: SavedMovieListData)

    @Query("DELETE FROM t_saved_movie_info")
    suspend fun delete()

    @Query("DELETE FROM t_saved_movie_info WHERE group_id = :groupId")
    suspend fun delete(groupId: Int)
}