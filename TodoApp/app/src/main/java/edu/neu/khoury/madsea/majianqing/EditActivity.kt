package edu.neu.khoury.madsea.majianqing

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*
import kotlin.properties.Delegates

class EditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    private lateinit var editWordView: EditText
    private lateinit var details: EditText
    private lateinit var deadline: EditText
    lateinit var reminder: TextView
    private lateinit var timePick: Button
    var clicked: Boolean = false
    lateinit var spinnerResult: TextView
    var id by Delegates.notNull<Int>()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)
        id = intent.getIntExtra("item", 0)
        val tags: Spinner = findViewById(R.id.tags)
        editWordView = findViewById(R.id.task_title)
        details = findViewById(R.id.more_details)
        deadline = findViewById(R.id.deadline)
        reminder = findViewById(R.id.reminder)
        timePick = findViewById(R.id.btn_timePicker)
        spinnerResult = findViewById(R.id.spinnerDisplay)
        val reminderCheck = findViewById<View>(R.id.reminder_checkbox) as Button
        timePick.isEnabled = false

        ArrayAdapter.createFromResource(
            this,
            R.array.tags_todo,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tags.adapter = adapter
        }
        tags.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val text: String = " " + p0?.getItemAtPosition(p2).toString()
                val origin: String = spinnerResult.text.toString()
                spinnerResult.text = (text + origin)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        reminderCheck.setOnClickListener { _ ->
            clicked = !clicked
            timePick.isEnabled = clicked
        }
        pickDate()
    }

    private fun getDateTimeCalender() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        timePick.setOnClickListener {
            getDateTimeCalender()

            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    fun saveAction(view: View) {
        val replyIntent = Intent()
        replyIntent.putExtra(EXTRA_ITEM_DATA_TEXT, editWordView.text.toString())
        replyIntent.putExtra(EXTRA_DEADLINE, deadline.text.toString())
        replyIntent.putExtra(EXTRA_DONE, false)
        replyIntent.putExtra(EXTRA_MORE_DETAILS, details.text.toString())
        replyIntent.putExtra(EXTRA_TIME_TO_REMIND, reminder.text.toString())
        replyIntent.putExtra(EXTRA_TAGS, spinnerResult.text)
        replyIntent.putExtra(EXTRA_REMINDER, clicked)
        replyIntent.putExtra(EXTRA_ID, id)
        replyIntent.putExtra(EXTRA_DAY, savedDay)
        replyIntent.putExtra(EXTRA_MONTH, savedMonth)
        replyIntent.putExtra(EXTRA_YEAR, savedYear)
        replyIntent.putExtra(EXTRA_HOUR, savedHour)
        replyIntent.putExtra(EXTRA_MINUTE, savedMinute)
        setResult(Activity.RESULT_OK, replyIntent)
        Toast.makeText(this, "item saved", Toast.LENGTH_LONG).show()
        finish()
    }

    fun cancelAction(view: View) {
        finish()
    }

    companion object {
        const val EXTRA_ITEM_DATA_TEXT = "itemDataText"
        const val EXTRA_DEADLINE = "deadline"
        const val EXTRA_DONE = "done"
        const val EXTRA_MORE_DETAILS = "more_details"
        const val EXTRA_TIME_TO_REMIND = "time_to_remind"
        const val EXTRA_TAGS = "tags"
        const val EXTRA_REMINDER = "reminder"
        const val EXTRA_ID = "ID"
        const val EXTRA_DAY = "day"
        const val EXTRA_MONTH = "month"
        const val EXTRA_YEAR = "year"
        const val EXTRA_HOUR = "hour"
        const val EXTRA_MINUTE = "minute"
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalender()
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        reminder.text = "$savedDay-$savedMonth-$savedYear\n Hour: $savedHour Minute: $savedMinute"
    }
}