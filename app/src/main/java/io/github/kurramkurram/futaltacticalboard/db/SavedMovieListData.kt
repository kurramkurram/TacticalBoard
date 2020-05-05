package io.github.kurramkurram.futaltacticalboard.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_saved_movie_info")
data class SavedMovieListData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "group_id") val groupId: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "date") val date: String
)