package com.example.rootlin

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        // marking the instance as volatile to ensure atomic access to the variable
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        internal fun getDatabase(context: Context): NoteDatabase? {
            if (INSTANCE == null) {
                synchronized(NoteDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            NoteDatabase::class.java,
                            "note_database"
                        ).fallbackToDestructiveMigration().addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        //This is optional
        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

            //Uncomment below to delete and repopulate database everytime app starts
            //        @Override
            //        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            //            super.onOpen(db);
            //            new PopulateDbAsync(INSTANCE).execute();
            //        }

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsync(INSTANCE!!).execute()
            }
        }

        private class PopulateDbAsync internal constructor(db: NoteDatabase) :
            AsyncTask<Void, Void, Void>() {

            private val mDao: NoteDao = db.noteDao()

            override fun doInBackground(vararg params: Void): Void? {
                // Start the app with a clean database every time.
                // Not needed if you only populate on creation.
                mDao.deleteAll()

                val noteEntity = NoteEntity(0,"Bibaswann is a great programmer/genius")
                mDao.insert(noteEntity)
                return null
            }
        }
    }

}
