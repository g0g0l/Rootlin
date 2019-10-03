package com.example.rootlin

import android.app.Application
import android.os.AsyncTask

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDao: NoteDao
    internal val allNotes: LiveData<List<NoteEntity>>
    // Using LiveData and caching what getAll returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    init {
        val db = NoteDatabase.getDatabase(application)
        noteDao = db!!.noteDao()
        allNotes = noteDao.all
    }

    internal fun insert(noteEntity: NoteEntity) {
        InsertAsyncTask(noteDao, noteEntity).execute()
    }

    internal fun edit(serial: Long, newText: String) {
        EditAsyncTask(noteDao, serial, newText).execute()
    }

    internal fun delete(noteEntity: NoteEntity) {
        DeleteAsyncTask(noteDao, noteEntity).execute()
    }

    internal fun deleteAll() {
        DeleteAllAsyncTask(noteDao).execute()
    }


    //----------------------AsyncTasks start----------------------------//

    private class InsertAsyncTask internal constructor(
        private val mAsyncTaskDao: NoteDao,
        private val noteEntity: NoteEntity
    ) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            val lastInsertId = mAsyncTaskDao.insert(noteEntity)
            return null
        }
    }

    private class EditAsyncTask internal constructor(
        private val mAsyncTaskDao: NoteDao,
        private val serial: Long,
        private val newText: String
    ) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            mAsyncTaskDao.edit(serial, newText)
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(
        private val mAsyncTaskDao: NoteDao,
        private val noteEntity: NoteEntity
    ) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            mAsyncTaskDao.delete(noteEntity)
            return null
        }
    }

    private class DeleteAllAsyncTask internal constructor(
        private val mAsyncTaskDao: NoteDao
    ) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }
}
