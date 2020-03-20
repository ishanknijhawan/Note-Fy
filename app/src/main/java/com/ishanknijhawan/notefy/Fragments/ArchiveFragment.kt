package com.ishanknijhawan.notefy.Fragments


import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.ishanknijhawan.notefy.Adapter.NoteAdapter
import com.ishanknijhawan.notefy.Entity.Note
import com.ishanknijhawan.notefy.FirebaseDatabase.FireStore

import com.ishanknijhawan.notefy.R
import com.ishanknijhawan.notefy.ViewModel.ViewModel
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.fragment_archive.*


class ArchiveFragment : Fragment() {

    lateinit var viewModel: ViewModel
    lateinit var getAllNotes: LiveData<List<Note>>
    lateinit var allNotes: List<Note>
    lateinit var noteAdapter: NoteAdapter
    lateinit var prefs: SharedPreferences
    lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_archive, container, false)

        prefs = PreferenceManager.getDefaultSharedPreferences(this.requireContext())

        val user = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FireStore.getDatabase(user.toString())!!

        val helper by lazy {

            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val note = allNotes[position]
                    val id = note.id
                    note.archive = false
                    note.deleted = true

                    viewModel.update(allNotes[position])
                    Snackbar.make(fragmentContainer, "Note moved to bin", Snackbar.LENGTH_LONG)
                        .apply {
                            view.layoutParams =
                                (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                                    setMargins(16, 16, 16, 16)
                                }
                        }
                        .setActionTextColor(Color.parseColor("#FFA500"))
                        .setAction("Undo")
                        {
                            note.archive = true
                            note.deleted = false
                            viewModel.update(note)
                        }
                        .show()

                }
            }
        }

        val helper2 by lazy {

            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val note = allNotes[position]
                    val id = note.id
                    note.archive = false

                    viewModel.update(allNotes[position])
                    Snackbar.make(fragmentContainer, "Note Unarchived", Snackbar.LENGTH_LONG)
                        .apply {
                            view.layoutParams =
                                (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                                    setMargins(16, 16, 16, 16)
                                }
                        }
                        .setActionTextColor(Color.parseColor("#FFA500"))
                        .setAction("Undo")
                        {
                            note.archive = true
                            viewModel.update(note)
                        }
                        .show()

                }
            }
        }

        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        getAllNotes = viewModel.getArchivedNotes()

        getAllNotes.observe(this, Observer {
            allNotes = getAllNotes.value!!
            rv_archive.layoutManager =
                StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            rv_archive.adapter = NoteAdapter(allNotes, this.requireContext())
            noteAdapter = NoteAdapter(allNotes, this.requireContext())

            if (prefs.getBoolean("switch_left", false).toString() == "true") {
                val swipe = ItemTouchHelper(helper)
                swipe.attachToRecyclerView(rv_archive)
            }
            if (prefs.getBoolean("switch_right", false).toString() == "true") {
                val swipe2 = ItemTouchHelper(helper2)
                swipe2.attachToRecyclerView(rv_archive)
            }

        })

        return view
    }
}

