package com.example.rootlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView


class NoteListAdapter internal constructor(
    context: Context,
    private val rvCallback: MainActivity.RvCallback
) :
    RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mNoteEntities: List<NoteEntity>

    inner class NoteViewHolder : RecyclerView.ViewHolder {
        val tvNote: TextView
        val btnEdit: ImageView
        val btnDelete: ImageView

        constructor(itemView: View) : super(itemView) {
            this.tvNote = itemView.findViewById(R.id.tvNote)
            this.btnEdit = itemView.findViewById(R.id.btnEdit)
            this.btnDelete = itemView.findViewById(R.id.btnDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = mInflater.inflate(R.layout.note_row, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = mNoteEntities[position]
        holder.tvNote.text = current.note
        holder.btnEdit.setOnClickListener {
            rvCallback.onEdit(current)
        }
        holder.btnDelete.setOnClickListener {
            rvCallback.onDelete(current)
        }

    }

    internal fun setNotes(noteEntities: List<NoteEntity>) {
        mNoteEntities = noteEntities
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mNoteEntities.size
    }

    init {
        this.mNoteEntities = emptyList<NoteEntity>()
    }
}


