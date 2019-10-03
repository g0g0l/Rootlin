package com.example.rootlin

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

