package com.example.todolistapp

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView and FAB
        recyclerView = findViewById(R.id.recyclerView)
        fabAddTask = findViewById(R.id.fabAddTask)

        // Set up RecyclerView with Adapter
        taskAdapter = TaskAdapter(
            taskList,
            onEditClick = { position ->
                showEditTaskDialog(position)
            },
            onDeleteClick = { position ->
                taskList.removeAt(position)
                taskAdapter.notifyItemRemoved(position)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        // Add new task when FAB is clicked
        fabAddTask.setOnClickListener {
            val newTask = Task("Task ${taskList.size + 1}")
            taskList.add(newTask)
            taskAdapter.notifyItemInserted(taskList.size - 1)
        }
    }

    /**
     * Shows a dialog to edit an existing task.
     */
    private fun showEditTaskDialog(position: Int) {
        val task = taskList[position]

        // Create an EditText for user input
        val editText = EditText(this).apply {
            setText(task.name)
            setSelection(task.name.length) // Place cursor at the end of text
        }

        // Show a dialog box to edit an task
        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val updatedTaskName = editText.text.toString()
                if (updatedTaskName.isNotEmpty()) {
                    // Update the task  list
                    taskList[position] = Task(updatedTaskName)
                    taskAdapter.notifyItemChanged(position)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
