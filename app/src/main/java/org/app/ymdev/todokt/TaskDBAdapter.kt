package org.app.ymdev.todokt

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.app.ymdev.todokt.common.Constant.DBProvider.COL_COMPLETED
import org.app.ymdev.todokt.common.Constant.DBProvider.COL_CONTENT
import org.app.ymdev.todokt.common.Constant.DBProvider.COL_ID
import org.app.ymdev.todokt.common.Constant.DBProvider.TABLE_NAME
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
        val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val task = Task(
                    cursor.getInt(cursor.getColumnIndex(COL_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_CONTENT)),
                    cursor.getInt(cursor.getColumnIndex(COL_COMPLETED)).toBoolean()
                )
                tasks.add(task)
            } while(cursor.moveToNext())
        }
        return tasks
    }

    fun createTask(content: String) {
        val values = ContentValues()
        values.put(COL_CONTENT, content)

        db.insertOrThrow(TABLE_NAME, null, values)
    }

    fun updateTask(task: Task) {
        val values = ContentValues()
        values.put(COL_CONTENT, task.content)
        values.put(COL_COMPLETED, task.completed)

        db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(task.id.toString()))
    }

    fun deleteTask(id: Int) {
        db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
    }
}