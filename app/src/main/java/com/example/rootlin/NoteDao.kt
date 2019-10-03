package com.example.rootlin

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @get:Query("SELECT * from note_table")
    val all: LiveData<List<NoteEntity>>

    @Query("update note_table set note=:newText where serial=:serial")
    fun edit(serial: Long, newText: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(noteEntity: NoteEntity): Long

    @Query("DELETE FROM note_table")
    fun deleteAll()

    @Delete
    fun delete(noteEntity: NoteEntity)
}
