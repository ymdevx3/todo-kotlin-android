package org.app.ymdev.todokt.common

object Constant {

    object DBProvider {
        const val DATABASE_NAME = "todo.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "task"

        const val COL_ID = "id"
        const val COL_CONTENT = "content"
        const val COL_COMPLETED = "completed"
    }
}

enum class EMenuType {
    all, active, completed
}