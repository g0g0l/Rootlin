package com.example.rootlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils

import androidx.appcompat.app.AppCompatActivity
import com.example.rootlin.databinding.ActivityNewNoteBinding

/**
 * Activity for entering a word.
 */

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var editNoteModel: NoteEntity? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getSerializableExtra(MainActivity.EXTRA_EDIT) != null)
            editNoteModel = intent.getSerializableExtra(MainActivity.EXTRA_EDIT) as NoteEntity

        if (editNoteModel != null)
            binding.etNoteTxt.setText(editNoteModel!!.note)

        binding.btnSave.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(binding.etNoteTxt.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(
                    MainActivity.EXTRA_REPLY_TEXT,
                    binding.etNoteTxt.text.toString()
                )
                if (editNoteModel != null)
                    replyIntent.putExtra(MainActivity.EXTRA_REPLY_SERIAL, editNoteModel!!.serial)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
}

