package org.app.ymdev.todokt

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.app.ymdev.todokt.common.Constant.DBProvider.colCompleted
import org.app.ymdev.todokt.common.Constant.DBProvider.colContent
import org.app.ymdev.todokt.common.Constant.DBProvider.colId
import org.app.ymdev.todokt.common.Constant.DBProvider.tableName
import org.app.ymdev.todokt.data.Task
import org.app.ymdev.todokt.utility.toBoolean

class TaskDBAdapter(context: Context) {
    private val db: SQLiteDatabase
    private val taskDB: TaskDBHelper

    init {
        taskDB = TaskDBHelper(context)
        db = taskDB.writableDatabase
    }

    fun getAllTasks(): List<Task> {
        var tasks: MutableList<Task> = ArrayList()
        val cursor: Cursor = db.query(tableName, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val task = Task(
                    cursor.getInt(cursor.getColumnIndex(colId)),
                    cursor.getString(cursor.getColumnIndex(colContent)),
                    cursor.getInt(cursor.getColumnIndex(colCompleted)).toBoolean()
                )
                tasks.add(task)
            } while(cursor.moveToNext())
        }
        return tasks
    }

    fun createTask(content: String) {
        val values = ContentValues()
        values.put(colContent, content)

        db.insertOrThrow(tableName, null, values)
    }

    fun updateTask(task: Task) {
        val values = ContentValues()
        values.put(colContent, task.content)
        values.put(colCompleted, task.completed)

        db.update(tableName, values, "$colId = ?", arrayOf(task.id.toString()))
    }

    fun deleteTask(id: Int) {
        db.delete(tableName, "$colId = ?", arrayOf(id.toString()))
    }
}