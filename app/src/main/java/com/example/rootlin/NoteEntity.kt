package com.example.rootlin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note_table")
class NoteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "serial") val serial: Long,
    @ColumnInfo(name = "note") val note: String
):Serializable
