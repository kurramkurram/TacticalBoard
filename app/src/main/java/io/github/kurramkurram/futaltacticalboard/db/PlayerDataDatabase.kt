package io.github.kurramkurram.futaltacticalboard.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlayerData::class], version = 1, exportSchema = false)
abstract class PlayerDataDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile
        private var INSTANCE: PlayerDataDatabase? = null

        fun getDatabases(context: Context): PlayerDataDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlayerDataDatabase::class.java,
                    "player_data"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}