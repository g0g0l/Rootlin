package com.example.rootlin

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The Room Magic is in this file, where you map a Java method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

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
