package io.github.turskyi.sqlitetransactionexample

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {
        const val LOG_TAG = "myLogs"
    }

    var dbh: DBHelper? = null
    var db: SQLiteDatabase? = null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "--- onCreate Activity ---")
        dbh = DBHelper(this)
        myActions()
    }

    private fun myActions() {
        db = dbh?.writableDatabase
        "mytable".delete(db)
        db?.beginTransaction()
        "mytable".insert(db, "val1")
        db?.setTransactionSuccessful()
        "mytable".insert(db, "val2")
        db?.endTransaction()
        "mytable".insert(db, "val3")
        "mytable".read(db)
        dbh?.close()
    }

    //void myActions() {
    //    db = dbh.getWritableDatabase();
    //    SQLiteDatabase db2 = dbh.getWritableDatabase();
    //    Log.d(LOG_TAG, "db = db2 - " + db.equals(db2));
    //    Log.d(LOG_TAG, "db open - " + db.isOpen() + ", db2 open - " + db2.isOpen());
    //    db2.close();
    //    Log.d(LOG_TAG, "db open - " + db.isOpen() + ", db2 open - " + db2.isOpen());
    //}
    private fun String.insert(sqLiteDatabase: SQLiteDatabase?, value: String) {
        Log.d(LOG_TAG, "Insert in table $this value = $value")
        val contentValues = ContentValues()
        contentValues.put("val", value)
        sqLiteDatabase?.insert(this, null, contentValues)
    }

    private fun String.read(db: SQLiteDatabase?) {
        Log.d(LOG_TAG, "Read table $this")
        val cursor = db?.query(this, null, null, null,
                null, null, null)
       cursor?.let{
            Log.d(LOG_TAG, "Records count = " + cursor.count)
            if (cursor.moveToFirst()) {
                do {
                    Log.d(LOG_TAG, cursor.getString(cursor.getColumnIndex("val")))
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
    }

    private fun String.delete(db: SQLiteDatabase?) {
        Log.d(LOG_TAG, "Delete all from table $this")
        db?.delete(this, null, null)
    }

    // класс для работы с БД
    inner class DBHelper(context: Context?) : SQLiteOpenHelper(
            context,
            "myDB",
            null,
            1) {
        override fun onCreate(db: SQLiteDatabase) {
            Log.d(LOG_TAG, "--- onCreate database ---")
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "val text"
                    + ");")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    }
}