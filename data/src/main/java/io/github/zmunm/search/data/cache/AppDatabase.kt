package io.github.zmunm.search.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.zmunm.search.data.cache.converter.DateConverter
import io.github.zmunm.search.data.cache.dao.DocumentDao
import io.github.zmunm.search.data.cache.table.TableDocument

@Database(entities = [TableDocument::class], version = 1, exportSchema = false)
@TypeConverters(
    DateConverter::class,
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao

    companion object {

        private const val DATABASE_NAME = "DATABASE"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                    }
                )
                .build()
        }
    }
}
