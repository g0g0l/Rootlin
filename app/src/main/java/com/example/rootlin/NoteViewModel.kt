package com.example.rootlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        insertCoroutine(noteEntity)
    }

    internal fun edit(serial: Long, newText: String) {
        editCoroutine(serial, newText)
    }

    internal fun delete(noteEntity: NoteEntity) {
        deleteCoroutine(noteEntity)
    }

    internal fun deleteAll() {
        deleteAllCoroutine()
    }

    //----------------------Coroutines start----------------------------//

    private fun insertCoroutine(noteEntity: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        noteDao.insert(noteEntity)
    }

    private fun editCoroutine(serial: Long, newText: String) = viewModelScope.launch(Dispatchers.IO) {
        noteDao.edit(serial, newText)
    }

    private fun deleteCoroutine(noteEntity: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        noteDao.delete(noteEntity)
    }

    private fun deleteAllCoroutine() = viewModelScope.launch(Dispatchers.IO) {
        noteDao.deleteAll()
    }
}