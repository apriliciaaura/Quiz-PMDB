package com.example.sqlite1.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlite1.`object`.EmpModelClass

class DatabaseHandler(context: Context):
    //fungsi yang digunakan untuk membuat tabel yang dapat menyimpan berbagai data.
    SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDB"
        private val TABLE_CONTACTS = "EmployeeTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_ADDRESS = "address"
    }

    // fungsi yang dipanggil untuk membuat tabel beserta definisi kolomnya
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_ADDRESS + " TEXT,"  + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    //fungsi yang dipanggil bila ditemukan database yang sama namun memiliki versi yang beda, juga digunakan untuk mengubah skema database
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }

    // fungsi yang dipanggil untuk menambahkan data pegawai
    fun addEmployee(emp: EmpModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName)
        contentValues.put(KEY_EMAIL,emp.userEmail )
        contentValues.put(KEY_ADDRESS,emp.userAddress )
        // menambahkan data pada tabel
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    // fungsi yang dipanggil untuk menampilkan data pegawai dari tabel ke UI
    fun viewEmployee():List<EmpModelClass>{
        val empList:ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var userName: String
        var userEmail: String
        var userAddress: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                userAddress = cursor.getString(cursor.getColumnIndex("address"))
                val emp= EmpModelClass(userId = userId, userName = userName, userEmail = userEmail, userAddress = userAddress)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    // fungsi yang digunakan untuk memperbarui data pegawai
    fun updateEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName)
        contentValues.put(KEY_EMAIL,emp.userEmail )
        contentValues.put(KEY_ADDRESS,emp.userAddress )

        // fungsi yang digunakan untuk memperbarui data pegawai
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+emp.userId,null)

        // fungsi yang digunakan untuk menutup koneksi ke database
        db.close()
        return success
    }

    // fungsi yang digunakan untuk menghapus data pegawai
    fun deleteEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        // fungsi yang digunakan untuk menandai employee id dari data pegawai yang akan dihapus
        contentValues.put(KEY_ID, emp.userId)
        // fungsi yang digunakan query untuk menghapus data pegawai
        val success = db.delete(TABLE_CONTACTS,"id="+emp.userId,null)

        // fungsi yang digunakan untuk menutup koneksi ke database
        db.close()
        return success
    }
}