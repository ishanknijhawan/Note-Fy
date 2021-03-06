package com.ishanknijhawan.notefy.Db

import android.content.Context
import android.os.AsyncTask
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ishanknijhawan.notefy.Dao.NoteDao
import com.ishanknijhawan.notefy.Entity.Converters
import com.ishanknijhawan.notefy.Entity.Note


@Database(entities = [Note::class],version = 1)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        var instance: NoteDatabase? = null
        fun getDatabase(context: Context): NoteDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, NoteDatabase::class.java,
                    "note_database"
                ).build()
            }
            return instance
        }
    }
}