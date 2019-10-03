package com.example.rootlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_note.*

/**
 * Activity for entering a word.
 */

class NewNoteActivity : AppCompatActivity() {

    var editNoteModel: NoteEntity? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        if (intent.getSerializableExtra(MainActivity.EXTRA_EDIT) != null)
            editNoteModel = intent.getSerializableExtra(MainActivity.EXTRA_EDIT) as NoteEntity

        if (editNoteModel != null)
            etNoteTxt.setText(editNoteModel!!.note)

        btnSave.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(etNoteTxt!!.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(MainActivity.EXTRA_REPLY_TEXT, etNoteTxt!!.text.toString())
                if (editNoteModel != null)
                    replyIntent.putExtra(MainActivity.EXTRA_REPLY_SERIAL, editNoteModel!!.serial)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
}

