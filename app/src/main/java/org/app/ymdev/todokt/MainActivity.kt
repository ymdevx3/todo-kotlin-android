package org.app.ymdev.todokt

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.app.ymdev.todokt.common.EMenuType
import org.app.ymdev.todokt.data.Task
import org.app.ymdev.todokt.utility.changeItemState
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    internal val LOG_TAG = "MainActivity"

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val editText = EditText(this)
            editText.hint = "What needs to be done?"

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Add task")
            dialog.setView(editText)
            dialog.setPositiveButton("Create") {_, _ ->
                val content = editText.text.toString()
                if (content.isNotEmpty()) {
                    // Create Task
                    this.createNewTask(content)
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.task_not_created,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            dialog.setNegativeButton("Cancel", null)

            dialog.show()
        }

        this.reloadTasks()

        list.setOnItemClickListener { _, view, position, _ ->
            // Update Task
            val task = this.taskAdapter.getItem(position) as Task
            task.completed = !task.completed
            this.updateTask(task)

            // Change View
            val layout = view as LinearLayout
            val textView = layout.findViewById<View>(R.id.task_content) as TextView
            changeItemState(textView, true)
        }

        list.setOnItemLongClickListener { _, _, position, _ ->
            val task = this.taskAdapter.getItem(position) as Task

            val editText = EditText(this)
            editText.setText(task.content)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Edit task")
            dialog.setView(editText)
            dialog.setPositiveButton("Update") {_, _ ->
                val content = editText.text.toString()
                if (content.isNotEmpty()) {
                    // Update Task
                    task.content = content
                    this.updateTask(task)
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.task_not_created,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            dialog.setNegativeButton("Cancel", null)
            dialog.show()

            return@setOnItemLongClickListener true
        }
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy()")
        super.onDestroy()
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
            R.id.action_all -> {
                this.reloadTasks()
                return true
            }
            R.id.action_active -> {
                this.reloadTasks(EMenuType.ACTIVE)
                return true
            }
            R.id.action_completed -> {
                this.reloadTasks(EMenuType.COMPLETED)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * リストアイテムの削除ボタンクリックイベントハンドラ
     * @param v View
     */
    fun onClickTaskDelete(v: View) {
        // DBから削除
        val position = v.tag as Int
        val task = this.taskAdapter.getItem(position) as Task
        this.deleteTask(task.id)

        // ListViewから削除
        this.taskAdapter.remove(task)
        this.taskAdapter.notifyDataSetChanged()

        Snackbar.make(list, "Deleted task : " + task.content, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    /**
     * 再読み込み
     * @param menu メニュー種別
     */
    private fun reloadTasks(menu: EMenuType = EMenuType.ALL) {
        try {
            val dbAdapter = TaskDBAdapter(this)
            var tasks = dbAdapter.getAllTasks()

            when (menu) {
                EMenuType.ACTIVE -> {
                    tasks = tasks.filter { x -> !x.completed}
                }
                EMenuType.COMPLETED -> {
                    tasks = tasks.filter { x -> x.completed}
                }
                else -> {}
            }
            // ID降順
            tasks = tasks.sortedByDescending { x -> x.id }

            this.taskAdapter = TaskAdapter(this, tasks.toMutableList())
            list.adapter = this.taskAdapter
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    /**
     * タスク追加
     */
    private fun createNewTask(content: String) {
        try {
            val dbAdapter = TaskDBAdapter(this)
            dbAdapter.createTask(content)
            this.reloadTasks()
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    /**
     * タスク更新
     */
    private fun updateTask(task: Task) {
        try {
            // DB更新
            val dbAdapter = TaskDBAdapter(this)
            dbAdapter.updateTask(task)

            // ListView更新
            this.taskAdapter.notifyDataSetChanged()
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    /**
     * タスク削除
     */
    private fun deleteTask(id: Int) {
        try {
            val dbAdapter = TaskDBAdapter(this)
            dbAdapter.deleteTask(id)
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }
}
