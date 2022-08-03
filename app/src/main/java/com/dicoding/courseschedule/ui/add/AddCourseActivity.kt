package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_course)

        val factory = AddViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        viewModel.saved.observe(this) {
            if (it.getContentIfNotHandled() == true)
                onBackPressed()
            else {
                val messageToast = getString(R.string.input_empty_message)
                Toast.makeText(this, messageToast, Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val courseName = findViewById<TextInputEditText>(R.id.add_name_course).text.toString().trim()
                val dayCourse = findViewById<Spinner>(R.id.add_spinner_day).selectedItemPosition
                val startTimeCourse = findViewById<TextView>(R.id.add_text_start_time).text.toString().trim()
                val endTimeCourse = findViewById<TextView>(R.id.add_text_end_time).text.toString().trim()
                val lecturerCourse = findViewById<TextInputEditText>(R.id.add_name_lecturer).text.toString().trim()
                val noteCourse = findViewById<TextInputEditText>(R.id.add_note).text.toString().trim()
                viewModel.insertCourse(courseName, dayCourse,
                    startTimeCourse, endTimeCourse, lecturerCourse, noteCourse)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showStartTimePicker(view: View) {
        TimePickerFragment().show(
            supportFragmentManager, " startTime"
        )
        this.view = view
    }

    fun showEndTimePicker(view: View) {
        TimePickerFragment().show(
            supportFragmentManager, " endTime"
        )
        this.view = view
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (view.id) {
            R.id.add_start_time -> {
                findViewById<TextView>(R.id.add_text_start_time).text = timeFormat.format(calender.time)
            }
            R.id.add_end_time -> {
                findViewById<TextView>(R.id.add_text_end_time).text = timeFormat.format(calender.time)
            }
        }
    }

}