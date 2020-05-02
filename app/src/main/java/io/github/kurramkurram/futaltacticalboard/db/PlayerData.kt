package io.github.kurramkurram.futaltacticalboard.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_player_info")
class PlayerData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "group_id") val groupId: Long,
    @ColumnInfo(name = "action_index") val index: Int,
    @ColumnInfo(name = "player_color") val playerColor: Int,
    @ColumnInfo(name = "player_id") val playerId: Int,
    @ColumnInfo(name = "player_name") val playerName: String,
    @ColumnInfo(name = "player_x") val playerX: Int,
    @ColumnInfo(name = "player_y") val playerY: Int
)