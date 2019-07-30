package org.app.ymdev.todokt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import org.app.ymdev.todokt.data.Task
import org.app.ymdev.todokt.utility.changeItemState

class TaskAdapter(context: Context, tasks: MutableList<Task>) : ArrayAdapter<Task>(context, 0, tasks) {

    private var mTasks = tasks

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView
        var holder: TaskViewHolder

        if (view == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.task_item, parent, false)

            val contentTextView = view.findViewById(R.id.task_content) as TextView
            val deleteButton = view.findViewById(R.id.button_delete) as ImageButton
            holder = TaskViewHolder(contentTextView, deleteButton)
            view.tag = holder
        } else {
            holder = view.tag as TaskViewHolder
        }

        val task = getItem(position) as Task
        holder.task_content.text = task.content
        changeItemState(holder.task_content, task.completed)

        holder.button_delete.tag = position

        return view!!
    }

    override fun remove(task: Task?) {
        mTasks.remove(task)
    }
}