package org.app.ymdev.todokt

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.app.ymdev.todokt.common.Constant.DBProvider.DATABASE_NAME
import org.app.ymdev.todokt.common.Constant.DBProvider.DATABASE_VERSION
import org.app.ymdev.todokt.common.Constant.DBProvider.TABLE_NAME

class TaskDBHelper(var context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        val sql = """create table if not exists $TABLE_NAME (
            id integer primary key autoincrement,
            content text not null,
            completed integer not null default 0
         );"""
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val sql ="drop table if exists $TABLE_NAME;"
        db?.execSQL(sql)
        onCreate(db)
    }

}