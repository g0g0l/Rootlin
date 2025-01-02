package com.example.rootlin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
                populateDatabase(INSTANCE!!)
                //PopulateDbAsync(INSTANCE!!).execute()
            }
        }

        private fun populateDatabase(db: NoteDatabase) {
            runBlocking {
                launch(Dispatchers.IO) {
                    val mDao: NoteDao = db.noteDao()
                    mDao.deleteAll()

                    val noteEntity = NoteEntity(0,"Bibaswann is a great programmer/genius")
                    mDao.insert(noteEntity)
                }
            }
        }
    }

}
