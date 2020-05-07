package io.github.kurramkurram.futaltacticalboard.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PlayerData::class, SavedVideoListData::class],
    version = 1,
    exportSchema = false
)
abstract class PlayerDataDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao

    abstract fun savedVideoListDao(): SavedVideoListDao

    companion object {
        @Volatile
        private var INSTANCE: PlayerDataDatabase? = null
        @Volatile
        private var MAIN_THREAD_INSTANCE: PlayerDataDatabase? = null

        fun getDatabases(context: Context): PlayerDataDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlayerDataDatabase::class.java,
                    "player_data.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

        fun getAllowMainThreadDatabases(context: Context): PlayerDataDatabase {
            val tmpInstance = MAIN_THREAD_INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlayerDataDatabase::class.java,
                    "player_data.db"
                ).allowMainThreadQueries().build()
                MAIN_THREAD_INSTANCE = instance
                return instance
            }
        }
    }
}