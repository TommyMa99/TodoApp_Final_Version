package edu.neu.khoury.madsea.majianqing

import android.app.Activity
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import java.util.concurrent.TimeUnit

import android.widget.LinearLayout
import android.widget.SearchView


class MainActivity : AppCompatActivity(), UpdateandDelete {

    private lateinit var itemSummary: String
    private lateinit var deadline: String
    private lateinit var details: String
    private lateinit var tags: String
    private lateinit var time_to_remind: String
    private var savedDay: Int = 0
    private var savedYear: Int = 0
    private var savedMonth: Int = 0
    private var savedHour: Int = 0
    private var savedMinute: Int = 0
    private var done: Boolean? = null
    private var id: Int = 0
    private var reminderCheck: Boolean? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TodoListAdapter
    private lateinit var searchView: SearchView

    private val newWordActivityRequestCode = 1
    private val editActivityCode = 2
    private val todoViewModel: TodoViewModel by viewModels {
        WordViewModelFactory((application as TodoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        searchView = findViewById<SearchView>(R.id.searchMe)
        adapter = TodoListAdapter(this as UpdateandDelete)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<TextView>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewItemActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

        todoViewModel.users.observe(this, Observer { users ->
            users?.let { adapter.submitList(it) }
        })


        val searchManager : SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                filter(p0)
                return true
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewItemActivity.EXTRA_ITEM_DATA_TEXT)?.let { reply ->
                itemSummary = reply
            }
            data?.getStringExtra(NewItemActivity.EXTRA_DEADLINE)?.let { reply ->
                deadline = reply
            }
            data?.getExtras()?.getBoolean(NewItemActivity.EXTRA_DONE).let { reply ->
                done = reply
            }
            data?.getStringExtra(NewItemActivity.EXTRA_MORE_DETAILS)?.let { reply ->
                details = reply
            }
            data?.getExtras()?.getBoolean(NewItemActivity.EXTRA_REMINDER).let { reply ->
                reminderCheck = reply
            }
            data?.getStringExtra(NewItemActivity.EXTRA_TAGS)?.let { reply ->
                tags = reply
            }
            data?.getStringExtra(NewItemActivity.EXTRA_TIME_TO_REMIND)?.let { reply ->
                time_to_remind = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_YEAR)?.let { reply ->
                savedYear = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_MONTH)?.let { reply ->
                savedMonth = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_DAY)?.let { reply ->
                savedDay = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_HOUR)?.let { reply ->
                savedHour = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_MINUTE)?.let { reply ->
                savedMinute = reply
            }
            val todo: TodoEntity = TodoEntity(
                itemSummary,
                details,
                tags,
                deadline,
                reminderCheck as Boolean,
                time_to_remind,
                done as Boolean
            )
            todoViewModel.insert(todo)
            if (reminderCheck as Boolean) {
                val c = Calendar.getInstance()
                c.set(savedYear, savedMonth, savedDay, savedHour, savedMinute, 0)
                val today = Calendar.getInstance()
                val diff = (c.timeInMillis / 1000L) - (today.timeInMillis / 1000L)

                val myWorkRequest = OneTimeWorkRequestBuilder<TodoWorker>()
                    .addTag(itemSummary)
                    .setInitialDelay(diff, TimeUnit.SECONDS)
                    .setInputData(
                        workDataOf(
                            "title" to itemSummary.toString(),
                            "message" to "Reminder!"
                        )
                    )
                    .build()
                WorkManager.getInstance(this).enqueue(myWorkRequest)
            }
        } else if (requestCode == editActivityCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(EditActivity.EXTRA_ITEM_DATA_TEXT)?.let { reply ->
                itemSummary = reply
            }
            data?.getStringExtra(EditActivity.EXTRA_DEADLINE)?.let { reply ->
                deadline = reply
            }
            data?.getExtras()?.getBoolean(EditActivity.EXTRA_DONE).let { reply ->
                done = reply
            }
            data?.getStringExtra(EditActivity.EXTRA_MORE_DETAILS)?.let { reply ->
                details = reply
            }
            data?.getExtras()?.getBoolean(EditActivity.EXTRA_REMINDER).let { reply ->
                reminderCheck = reply
            }
            data?.getStringExtra(EditActivity.EXTRA_TAGS)?.let { reply ->
                tags = reply
            }
            data?.getStringExtra(EditActivity.EXTRA_TIME_TO_REMIND)?.let { reply ->
                time_to_remind = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_ID)?.let { reply ->
                id = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_YEAR)?.let { reply ->
                savedYear = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_MONTH)?.let { reply ->
                savedMonth = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_DAY)?.let { reply ->
                savedDay = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_HOUR)?.let { reply ->
                savedHour = reply
            }
            data?.getExtras()?.getInt(EditActivity.EXTRA_MINUTE)?.let { reply ->
                savedMinute = reply
            }
            val todo: TodoEntity = TodoEntity(
                itemSummary,
                details,
                tags,
                deadline,
                reminderCheck as Boolean,
                time_to_remind,
                done as Boolean
            )
            todo.id = id
            todoViewModel.update(todo)
            WorkManager.getInstance(this).cancelAllWorkByTag(itemSummary)
            if (reminderCheck as Boolean) {
                val c = Calendar.getInstance()
                c.set(savedYear, savedMonth, savedDay, savedHour, savedMinute, 0)
                val today = Calendar.getInstance()
                val diff = (c.timeInMillis / 1000L) - (today.timeInMillis / 1000L)

                val myWorkRequest = OneTimeWorkRequestBuilder<TodoWorker>()
                    .addTag(itemSummary)
                    .setInitialDelay(diff, TimeUnit.SECONDS)
                    .setInputData(
                        workDataOf(
                            "title" to itemSummary.toString(),
                            "message" to "Reminder!"
                        )
                    )
                    .build()
                WorkManager.getInstance(this).enqueue(myWorkRequest)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun modifyItem(item: TodoEntity) {
        item.done = !item.done
        todoViewModel.update(item)
    }

    override fun onItemDelete(item: TodoEntity) {
        WorkManager.getInstance(this).cancelAllWorkByTag(item.itemDataText)
        todoViewModel.delete(item)

    }

    override fun onItemEdit(item: TodoEntity) {
        val intent = Intent(this@MainActivity, EditActivity::class.java)
        intent.putExtra("item", item.id)
        startActivityForResult(intent, editActivityCode)
    }

    override fun onOptionsMenuClicked(position: Int, item: TodoEntity) {
        // create object of PopupMenu and pass context and view where we want
        // to show the popup menu
        val popupMenu = PopupMenu(this, recyclerView[position].findViewById(R.id.options))
        // add the menu
        popupMenu.inflate(R.menu.menu_main)
        // implement on menu item click Listener
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item2: MenuItem?): Boolean {
                when (item2?.itemId) {
                    R.id.close -> {
                        MaterialAlertDialogBuilder(this@MainActivity)
                            .setTitle("Alert")
                            .setMessage("Are you sure you want to delete this task?")
                            .setNegativeButton("No") { dialog, which ->
                                Toast.makeText(
                                    this@MainActivity,
                                    "Request Cancelled",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            .setPositiveButton("Yes") { dialog, which ->
                                onItemDelete(item)
                                adapter.notifyDataSetChanged()
                                Toast.makeText(this@MainActivity, "Item Deleted", Toast.LENGTH_LONG)
                                    .show()
                            }
                            .show()
                        return true
                    }
                    R.id.view -> {
                        var done: String = "Unfinished"
                        if (item.done) {
                            done = "Finished"
                        }

                        var remind: String = "No reminder"
                        if (item.reminder) {
                            remind = "Reminder set for this task"
                        }

                        val layout = LinearLayout(this@MainActivity)
                        layout.orientation = LinearLayout.VERTICAL

                        val description = TextView(this@MainActivity)
                        description.text = "Description: "
                        description.setTextColor(Color.RED)
                        description.setTextSize(17F)
                        layout.addView(description)

                        val describe = TextView(this@MainActivity)
                        describe.text = item.itemDataText
                        layout.addView(describe)

                        val detail = TextView(this@MainActivity)
                        detail.text = "Details: "
                        detail.setTextColor(Color.RED)
                        detail.setTextSize(17F)
                        layout.addView(detail)

                        val details = TextView(this@MainActivity)
                        details.text = item.moreDetails
                        layout.addView(details)

                        val tag = TextView(this@MainActivity)
                        tag.text = "Tags: "
                        tag.setTextColor(Color.RED)
                        tag.setTextSize(17F)
                        layout.addView(tag)

                        val tags = TextView(this@MainActivity)
                        tags.text = item.tags
                        layout.addView(tags)

                        val dead = TextView(this@MainActivity)
                        dead.text = "Deadline: "
                        dead.setTextColor(Color.RED)
                        dead.setTextSize(17F)
                        layout.addView(dead)

                        val deadline = TextView(this@MainActivity)
                        deadline.text = item.deadline
                        layout.addView(deadline)

                        val remindMe = TextView(this@MainActivity)
                        remindMe.text = "Reminder: "
                        remindMe.setTextColor(Color.RED)
                        remindMe.setTextSize(17F)
                        layout.addView(remindMe)

                        val reminder = TextView(this@MainActivity)
                        reminder.text = remind
                        layout.addView(reminder)
                        val alertDialog = AlertDialog.Builder(this@MainActivity)
                        alertDialog.setView(layout)
                        alertDialog.show()
                        return true
                    }
                    R.id.edit -> {
                        onItemEdit(item)
                        adapter.notifyDataSetChanged()
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

    fun filter(query: String?) {
        val res : MutableList<TodoEntity> = mutableListOf()
        if (query != null) {
            if(query.isEmpty()){
                todoViewModel.users.value?.let { res.addAll(it) }
            } else {
                for(task in todoViewModel.users.value!!) {
                    if(task.itemDataText.toString().lowercase().contains(query.toString().lowercase())) {
                        res.add(task)
                    }
                }
            }
        }
        adapter.submitList(res)
    }

}