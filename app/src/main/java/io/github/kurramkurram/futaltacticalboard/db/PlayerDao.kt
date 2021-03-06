package io.github.kurramkurram.futaltacticalboard.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerDao {
    @Query("SELECT * FROM t_player_info WHERE group_id = :groupId ")
    fun selectGroup(groupId: Long): List<PlayerData>

    @Query("SELECT * FROM t_player_info")
    fun selectALL(): List<PlayerData>

    @Insert
    suspend fun insert(playerData: List<PlayerData>)

    @Insert
    suspend fun insert(playerData: PlayerData)

    @Query("DELETE FROM t_player_info WHERE group_id = :groupId")
    suspend fun delete(groupId: Int)

    @Query("DELETE FROM t_player_info")
    suspend fun delete()
}
