package com.example.urok.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.text.CaseMap
import android.provider.BaseColumns
import java.util.ArrayList

class MyDbManager(context: Context) {
    val myDbHelpery = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = myDbHelpery.writableDatabase
    }

    fun insertToDb(title: String, content: String, uri: String, time:String, data: String){

        val values = ContentValues().apply {

            put(MydbName.COLUMN_NAME_TITLE, title)
            put(MydbName.COLUMN_NAME_CONTENT, content)
            put(MydbName.COLUMN_NAME_IMAGE_URI, uri)
            put(MydbName.COLUMN_NAME_TIME, time)
            put(MydbName.COLUMN_NAME_DATA, data)
        }
        db?.insert(MydbName.TABLE_NAME, null, values)
    }

    fun updateItem(title: String, content: String, uri: String, id: Int, time: String, data: String){
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(MydbName.COLUMN_NAME_TITLE, title)
            put(MydbName.COLUMN_NAME_CONTENT, content)
            put(MydbName.COLUMN_NAME_IMAGE_URI, uri)
            put(MydbName.COLUMN_NAME_TIME, time)
            put(MydbName.COLUMN_NAME_DATA, data)
        }
        db?.update(MydbName.TABLE_NAME, values, selection, null)
    }

    fun removeItemFromDb(id: String){
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MydbName.TABLE_NAME, selection, null)
    }

    @SuppressLint("Range")
    fun readDbData() : ArrayList <ListItem>{
        val dataList = ArrayList <ListItem>()

        val cursor = db?.query(MydbName.TABLE_NAME, null, null,null,
            null,null,null)

            while (cursor?.moveToNext()!!){
                val dataTitle = cursor.getString(cursor.getColumnIndex(MydbName.COLUMN_NAME_TITLE))
                val dataContent = cursor.getString(cursor.getColumnIndex(MydbName.COLUMN_NAME_CONTENT))
                val dataUri = cursor.getString(cursor.getColumnIndex(MydbName.COLUMN_NAME_IMAGE_URI))
                val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                val time = cursor.getString(cursor.getColumnIndex(MydbName.COLUMN_NAME_TIME))
                val data = cursor.getString(cursor.getColumnIndex(MydbName.COLUMN_NAME_DATA))
                val item = ListItem()
                item.title = dataTitle
                item.deck = dataContent
                item.uri = dataUri
                item.id = dataId
                item.time = time
                item.data = data
                dataList.add(item)
            }
        cursor.close()
        return dataList
    }
    fun closeDb(){
        myDbHelpery.close()
    }

    fun updateItem(title: String, content: String, uri: String, id: Int, time: String) {

    }
}
