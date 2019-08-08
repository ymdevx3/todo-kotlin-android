package org.app.ymdev.todokt.common

object Constant {

    object DBProvider {
        const val databaseName = "todo.db"
        const val databaseVersion = 1

        const val tableName = "task"

        const val colId = "id"
        const val colContent = "content"
        const val colCompleted = "completed"
    }
}

enum class EMenuType {
    ALL, ACTIVE, COMPLETED
}