package io.github.kurramkurram.futaltacticalboard.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedVideoListDao {

    @Query("SELECT* FROM t_saved_video_info")
    fun selectAll(): LiveData<List<SavedVideoListData>>

    @Insert
    suspend fun insert(data: SavedVideoListData)

    @Query("DELETE FROM t_saved_video_info")
    suspend fun delete()

    @Query("DELETE FROM t_saved_video_info WHERE group_id = :groupId")
    suspend fun delete(groupId: Int)
}