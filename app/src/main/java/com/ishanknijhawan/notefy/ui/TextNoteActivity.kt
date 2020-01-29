package com.ishanknijhawan.notefy.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.ishanknijhawan.notefy.Entity.Note
import com.ishanknijhawan.notefy.R
import com.ishanknijhawan.notefy.ViewModel.ViewModel
import kotlinx.android.synthetic.main.activity_text_note.*
import org.jetbrains.anko.*
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnFastChooseColorListener
import java.util.*


class TextNoteActivity : AppCompatActivity() {

        lateinit var titleNote: EditText
        lateinit var contentNote: EditText
        lateinit var viewModel: ViewModel

        var bigArchive: Boolean = false
        var bigBookmark: Boolean = false
        var bigDelete: Boolean = false

        var noteColor: Int = 0
        var finalColor: Int = -1
        var dateTime = ""
        var mYear: Int = 0
        var mMonth: Int = 0
        var mDay: Int= 0
        var mHour: Int= 0
        var mMinute: Int= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_note)

        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        val bottomSheetBehavior2 = BottomSheetBehavior.from(bottomSheet2)

        val rq1 = intent.getStringExtra("REQUEST_CODE")

        if (rq1 == "poochi") {
            val bool = intent.getStringExtra("BOOL")
            if (bool == "true"){
                iv_pin.setImageResource(android.R.color.transparent)
                iv_pin.setBackgroundResource(R.drawable.ic_push_pin_black_final)
            }
            else if(bool == "false"){
                Log.i("BG","setting this bg")
                iv_pin.setImageResource(android.R.color.transparent)
                iv_pin.setBackgroundResource(R.drawable.ic_push_pin_final)
            }

            val arc = intent.getStringExtra("ARC")
            if (arc == "false"){
                iv_archive.setImageResource(android.R.color.transparent)
                iv_archive.setBackgroundResource(R.drawable.ic_archive_black_24dp)
            }
            else if (arc == "true"){
                Log.i("BG","setting this bg")
                iv_archive.setImageResource(android.R.color.transparent)
                iv_archive.setBackgroundResource(R.drawable.ic_unarchive_black_24dp)
            }
        }
        else {
            iv_pin.setBackgroundResource(R.drawable.ic_push_pin_final)
            iv_archive.setBackgroundResource(R.drawable.ic_archive_black_24dp)
        }

        iv_reminder.setOnClickListener {
            datePickerFunction()
        }

        addMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        menu_menu.setOnClickListener {
            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_EXPANDED
        }

        reminder_chip.setOnCheckedChangeListener { compoundButton, b ->
            Toast.makeText(this,"Checked $b",Toast.LENGTH_SHORT).show()
        }
        reminder_chip.visibility = View.GONE

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        addMenu.setImageResource(R.drawable.ic_add_square_button)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        //tv_state.text = "Expanded"
                        //addMenu.setImageResource(R.drawable.ic_downarrow)
                        addMenu.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
//                        menu_menu.setOnClickListener {
//                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_EXPANDED
//                        }
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        //tv_state.text = "collapsed"
                        addMenu.setImageResource(R.drawable.ic_add_square_button)
                        addMenu.setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        //tv_state.text = "dragging..."
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        //tv_state.text = "settling"
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })

        bottomSheetBehavior2.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        menu_menu.setImageResource(R.drawable.ic_menu)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        //tv_state.text = "Expanded"
                        //menu_menu.setImageResource(R.drawable.ic_downarrow)
//                        addMenu.setOnClickListener {
//                            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_COLLAPSED
//                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//                        }
                        menu_menu.setOnClickListener {
                            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        //tv_state.text = "collapsed"
                        menu_menu.setImageResource(R.drawable.ic_menu)
                        menu_menu.setOnClickListener {
                            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        //tv_state.text = "dragging..."
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        //tv_state.text = "settling"
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })

        val rq = intent.getStringExtra("REQUEST_CODE")
        titleNote = findViewById(R.id.et_note_title)
        contentNote = findViewById(R.id.et_note_content)

        iv_back.setOnClickListener {
            if (rq == "poochi"){
                updateNote()
            }
            else {
                saveNote()
            }
            finish()
        }

        iv_archive.setOnClickListener {
            Snackbar.make(it, "Added to archive", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.parseColor("#FFA500"))
                .setAction("Undo")
                { Toast.makeText(this@TextNoteActivity,"clicked undo",Toast.LENGTH_SHORT).show() }
                .show()
        }

        iv_pin.setOnClickListener {
            if (iv_pin.background.constantState == resources.getDrawable(R.drawable.ic_push_pin_final).constantState){
                iv_pin.setBackgroundResource(R.drawable.ic_push_pin_black_final)
                bigBookmark = true
                updateNote()
            }
            else if(iv_pin.background.constantState == resources.getDrawable(R.drawable.ic_push_pin_black_final).constantState) {
                iv_pin.setBackgroundResource(R.drawable.ic_push_pin_final)
                bigBookmark = false
                updateNote()
            }

        }

        if (rq == "poochi"){
            val title = intent.getStringExtra("INTENT_TITLE")
            val description = intent.getStringExtra("INTENT_NOTE")
            finalColor = intent.getIntExtra("INTENT_COLOR",-1)

            if (finalColor == 0)
                finalColor = -1


            Log.i("COLOR","color is $finalColor")
            if (finalColor != -1) {
                window.navigationBarColor = finalColor
            }

            Log.i("QWE","value of title in TNA is $title")
            Log.i("QWE","value of note in TNA is $description")
            Log.i("QWE","value of color in TNA is $finalColor")

            titleNote.setText(title)
            contentNote.setText(description)

            titleNote.hintTextColor = darkenColorHint(finalColor)
            contentNote.hintTextColor = darkenColorHint(finalColor)

            lll.backgroundColor = finalColor
            ll_bs1.backgroundColor = finalColor
            ll_bs2.backgroundColor = finalColor
            ll_toolbar1.backgroundColor = finalColor
            titleNote.backgroundColor = finalColor
            contentNote.backgroundColor = finalColor
            window.statusBarColor = darkenColorHint(finalColor)
            cl_textNote.backgroundColor = finalColor
            reminder_chip.chipBackgroundColor = ColorStateList.valueOf(finalColor)
            reminder_chip.chipStrokeColor = ColorStateList.valueOf(darkenColor(finalColor))

            ColorPicker(this).setDefaultColorButton(finalColor)

            if (finalColor != -1)
            window.navigationBarColor = darkenColor(finalColor)
        }

        val rb = resources.obtainTypedArray(R.array.rainbow)

        bs_colours.setOnClickListener {

            val colorPicker = ColorPicker(this)
            colorPicker.setOnFastChooseColorListener(object : OnFastChooseColorListener {
                override fun setOnFastChooseColorListener(
                    position: Int,
                    color: Int
                ) { // put code
                    finalColor = rb.getColor(position,-1)
                    setColors(rb,finalColor)
                }

                override fun onCancel() { // put code
                    colorPicker.dismissDialog()
                }
            })
                .setColors(R.array.rainbow)
                .setColumns(4)
                .setTitlePadding(5,5,10,10)
                .setColorButtonTickColor(Color.parseColor("#222222"))
                .setDefaultColorButton(finalColor)
                //.setColorButtonSize(40,40)
                .setColorButtonMargin(12,5,12,5)
                .setTitle("Note color")
                .setRoundColorButton(true)
                .show()
        }

    }

    private fun datePickerFunction() { // Get Current Date
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                dateTime = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                //Call Time Picker Here
                timePicker()
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    private fun timePicker() { // Get Current Time
        val c = Calendar.getInstance()
        mHour = c[Calendar.HOUR_OF_DAY]
        mMinute = c[Calendar.MINUTE]
        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            this,
            OnTimeSetListener { view, hourOfDay, minute ->
                mHour = hourOfDay
                mMinute = minute
                Toast.makeText(this,"$dateTime $hourOfDay:$minute",Toast.LENGTH_SHORT).show()
            }, mHour, mMinute, false
        )
        timePickerDialog.show()
    }

    private fun setColors(rb: TypedArray, color: Int) {
        lll.backgroundColor = color
        et_note_title.backgroundColor = color
        et_note_content.backgroundColor = color
        ll_bs1.backgroundColor = color
        ll_bs2.backgroundColor = color
        ll_toolbar1.backgroundColor = color
        cl_textNote.backgroundColor = color
        reminder_chip.chipBackgroundColor = ColorStateList.valueOf(color)
        reminder_chip.chipStrokeColor = ColorStateList.valueOf(darkenColor(color))

        titleNote.hintTextColor = darkenColorHint(color)
        contentNote.hintTextColor = darkenColorHint(color)

        if (color != -1)
        window.navigationBarColor = darkenColor(color)
        else
            window.navigationBarColor = Color.parseColor("#000000")
        window.statusBarColor = darkenColorHint(color)

        rb.recycle()
    }

    @ColorInt
    fun darkenColor(@ColorInt color: Int): Int {
        return Color.HSVToColor(FloatArray(3).apply {
            Color.colorToHSV(color, this)
            this[2] *= 0.9f
        })
    }

    @ColorInt
    fun darkenColorHint(@ColorInt color: Int): Int {
        return Color.HSVToColor(FloatArray(3).apply {
            Color.colorToHSV(color, this)
            this[2] *= 0.7f
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val rq = intent.getStringExtra("REQUEST_CODE")
        if (rq == "poochi"){
            updateNote()
        }
        else {
            saveNote()
        }
        finish()
        super.onBackPressed()
    }

    private fun saveNote() {

        val finalTitle = titleNote.text.toString()
        val content = contentNote.text.toString()

        if (finalColor != -1)
            window.statusBarColor = darkenColor(finalColor)

        if (finalTitle.isEmpty() && content.isEmpty()) {
            Toast.makeText(this,"Empty note discarded",Toast.LENGTH_SHORT).show()
        }

        else {
            viewModel.insert(
                Note(title = finalTitle,
                    description = content,
                    archive = bigArchive,
                    label = "home",
                    bookmark = bigBookmark,
                    deleted = bigDelete,
                    color = finalColor)
            )

            Log.i("TAG","value of title is $finalTitle")
            Log.i("TAG","value of description is $content")
            Log.i("TAG","value of color while saving is $noteColor")

        }
    }

    private fun updateNote() {
        val rq = intent.getStringExtra("REQUEST_CODE")

        if (rq == "poochi") {
            val arc = intent.getStringExtra("ARC")
            bigArchive = arc == "true"
        }

        val finalTitle = titleNote.text.toString()
        val content = contentNote.text.toString()

        val note = Note(title = finalTitle,
            description = content,
            archive = bigArchive,
            label = "home",
            bookmark = bigBookmark,
            deleted = bigDelete,
            color = finalColor)

        note.id = intent.getLongExtra("INTENT_NOTE_ID",-1)

        if (finalTitle == "" && content == "") {
            viewModel.delete(note)
            Toast.makeText(this,"Empty note discarded",Toast.LENGTH_SHORT).show()
        }

        else {
            viewModel.update(note)

            Log.i("TAG","value of title is $finalTitle")
            Log.i("TAG","value of description is $content")
            Log.i("TAG","value of color while updating is $noteColor")
        }
    }

}
