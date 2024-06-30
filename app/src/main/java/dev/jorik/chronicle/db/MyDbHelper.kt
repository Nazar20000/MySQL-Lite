package com.example.urok.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper (context : Context) : SQLiteOpenHelper (context, MydbName.DATABASE_NAME,
    null, MydbName.DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase?){
        db?.execSQL(MydbName.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL(MydbName.SQL_DELETE_TABLE)
        onCreate(db)
    }
}
