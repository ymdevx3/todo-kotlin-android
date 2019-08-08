package org.app.ymdev.todokt

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.app.ymdev.todokt.common.Constant.DBProvider.databaseName
import org.app.ymdev.todokt.common.Constant.DBProvider.databaseVersion
import org.app.ymdev.todokt.common.Constant.DBProvider.tableName

class TaskDBHelper(var context: Context?) : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

    override fun onCreate(db: SQLiteDatabase?) {

        val sql = """create table if not exists $tableName (
            id integer primary key autoincrement,
            content text not null,
            completed integer not null default 0
         );"""
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val sql ="drop table if exists $tableName;"
        db?.execSQL(sql)
        onCreate(db)
    }

}