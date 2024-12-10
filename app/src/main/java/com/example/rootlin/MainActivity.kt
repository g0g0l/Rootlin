package com.example.rootlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rootlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val NEW_NOTE_ACTIVITY_REQUEST_CODE = 10
        const val EDIT_NOTE_ACTIVITY_REQUEST_CODE = 20
        const val EXTRA_REPLY_TEXT = "reply_text"
        const val EXTRA_REPLY_SERIAL = "reply_serial"
        const val EXTRA_EDIT = "edit"
    }

    interface RvCallback {
        fun onEdit(noteEntity: NoteEntity)
        fun onDelete(noteEntity: NoteEntity)
    }

    private lateinit var noteViewModel: NoteViewModel

    //Actual works start here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        val adapter = NoteListAdapter(this, object : RvCallback {
            override fun onEdit(noteEntity: NoteEntity) {
                val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
                intent.putExtra(EXTRA_EDIT, noteEntity)
                editNoteResultLauncher.launch(intent)
            }

            override fun onDelete(noteEntity: NoteEntity) {
                noteViewModel.delete(noteEntity)
            }

        })
        binding.contentMain.noteList.adapter = adapter
        binding.contentMain.noteList.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        noteViewModel.allNotes.observe(this,
            Observer<List<NoteEntity>> { notes ->
                // Update the cached copy of the notes in the adapter.
                adapter.setNotes(notes)
            })


        binding.fab.setOnClickListener {
            newNoteResultLauncher.launch(Intent(this, NewNoteActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_all -> {
                noteViewModel.deleteAll()
                return true
            }
            R.id.action_about -> {
               startActivity(Intent(this,AboutActivity::class.java))
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private var newNoteResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        //Serial should always be 0 for insert because it's set auto increment
        if (result.resultCode == Activity.RESULT_OK) {

                noteViewModel.insert(
                    NoteEntity(0, result.data!!.getStringExtra(EXTRA_REPLY_TEXT)!!)
                )
        } else {
            Toast.makeText(this, "Mission aborted", Toast.LENGTH_LONG).show()
        }
    }
    var editNoteResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        //Serial should always be 0 for insert because it's set auto increment
        if (result.resultCode == Activity.RESULT_OK) {
                noteViewModel.edit(
                    result.data!!.getLongExtra(EXTRA_REPLY_SERIAL, 0),
                    result.data!!.getStringExtra(EXTRA_REPLY_TEXT)!!
                )
        } else {
            Toast.makeText(this, "Mission aborted", Toast.LENGTH_LONG).show()
        }
    }
}
