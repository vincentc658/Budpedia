package com.gajikaryawan.gajiq.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.gajikaryawan.gajiq.model.Staff
import com.gajikaryawan.gajiq.util.Constants
import com.gajikaryawan.gajiq.util.Constants.Companion.DATABASE_NAME
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_END_DATE
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_ID_STAFF
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_START_DATE
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_STATUS
import com.gajikaryawan.gajiq.util.Constants.Companion.STAFF_IS_PER_MONTH
import com.gajikaryawan.gajiq.util.Constants.Companion.STAFF_NAME
import com.gajikaryawan.gajiq.util.Constants.Companion.STAFF_PHONE
import com.gajikaryawan.gajiq.util.Constants.Companion.STAFF_SALARY
import com.gajikaryawan.gajiq.util.Constants.Companion.TABLE_ABSENCE
import com.gajikaryawan.gajiq.util.Constants.Companion.TABLE_PAYMENT_ROLL
import com.gajikaryawan.gajiq.util.Constants.Companion.TABLE_STAFF

class DatabaseController(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(p0: SQLiteDatabase?) {

        val CREATE_STAFF_TABLE = ("CREATE TABLE " + Constants.TABLE_STAFF + "("
                + Constants.STAFF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.STAFF_NAME + " TEXT NOT NULL, "
                + Constants.STAFF_IS_PER_MONTH + " BOOLEAN NOT NULL, "
                + Constants.STAFF_PHONE + "  VARCHAR(15) NOT NULL UNIQUE, "
                + Constants.STAFF_SALARY + " BIGINT  NOT NULL "
                + ")")

        val CREATE_PAYMENT_ROLL_TABLE = ("CREATE TABLE " + Constants.TABLE_PAYMENT_ROLL + "("
                + Constants.PAYMENT_ROLL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.PAYMENT_ROLL_DATE + " DATETIME, "
                + Constants.PAYMENT_ROLL_ID_STAFF + " INTEGER NOT NULL, "
                + Constants.PAYMENT_ROLL_STATUS + " INTEGER NOT NULL, "
                + Constants.PAYMENT_ROLL_START_DATE + " DATE  NOT NULL, "
                + Constants.PAYMENT_ROLL_END_DATE + " DATE  NOT NULL, "
                + "FOREIGN KEY (" + Constants.PAYMENT_ROLL_ID_STAFF + ") REFERENCES " + Constants.TABLE_STAFF + "(" + Constants.STAFF_ID + ") ON UPDATE CASCADE ON DELETE CASCADE "
                + ")")

        val CREATE_ABSENCE_TABLE = ("CREATE TABLE " + Constants.TABLE_ABSENCE + "("
                + Constants.ABSENCE_TIME + " DATETIME, "
                + Constants.ABSENCE_ID_PAYMENT_ROLL + " INTEGER NOT NULL, "
                + Constants.ABSENCE_IS_ATTEND + " BOOLEAN NOT NULL, "
                + Constants.ABSENCE_IS_PAID + " BOOLEAN  NOT NULL, "
                + Constants.PAYMENT_ROLL_END_DATE + " DATE  NOT NULL, "
                + "FOREIGN KEY (" + Constants.ABSENCE_ID_PAYMENT_ROLL + ") REFERENCES " + Constants.TABLE_PAYMENT_ROLL + "(" + Constants.PAYMENT_ROLL_ID + ") ON UPDATE CASCADE ON DELETE CASCADE "
                + ")")

        p0?.execSQL(CREATE_STAFF_TABLE)
        p0?.execSQL(CREATE_PAYMENT_ROLL_TABLE)
        p0?.execSQL(CREATE_ABSENCE_TABLE)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        // Drop older table if existed
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_STAFF")
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_PAYMENT_ROLL")
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_ABSENCE")

        // Create tables again

        // Create tables again
        onCreate(p0)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)

        //enable foreign key constraints like ON UPDATE CASCADE, ON DELETE CASCADE
        db!!.execSQL("PRAGMA foreign_keys=ON;")

    }

    fun insertData(
        name: String,
        isPermonth: Boolean,
        salary: Int,
        phone: String,
        response: (id : Long, status: Boolean, message : String?) -> Unit
    ) {
        try {

            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(STAFF_NAME, name)
            contentValues.put(STAFF_IS_PER_MONTH, isPermonth)
            contentValues.put(STAFF_SALARY, salary)
            contentValues.put(STAFF_PHONE, phone)
            val id: Long = db.insert(TABLE_STAFF, null, contentValues)
            if (id > 0) {
                response(id, true, null)
            } else {
                response(id, false, null)
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            response(0, false, e.message)
        }

    }

    fun getStaff(): ArrayList<Staff> {
        val db = this.writableDatabase
        val res = db.rawQuery("select * from " + TABLE_STAFF, null)
        val datas = ArrayList<Staff>()
        while (res.moveToNext()) {
            val data = Staff()
            data.salary = res.getInt(res.getColumnIndex(STAFF_SALARY))
            data.name = res.getString(res.getColumnIndex(STAFF_NAME))
            data.phone = res.getString(res.getColumnIndex(STAFF_PHONE))
            data.isPerMonth = res.getInt(res.getColumnIndex(STAFF_IS_PER_MONTH)) == 1
            datas.add(data)
        }
        return datas
    }
    fun getPaymentRol(id : Long, date :String) : Boolean{
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PAYMENT_ROLL WHERE $PAYMENT_ROLL_START_DATE >= ? and $PAYMENT_ROLL_ID_STAFF=?  ", arrayOf(date, id.toString()))

        return cursor != null && cursor.moveToFirst()
    }
    fun insertPaymentRoll(
        idStaff: Long,
        startDate: String,
        endDate: String,
        response: (id : Long, status: Boolean, message : String?) -> Unit
    ) {
        try {

            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(PAYMENT_ROLL_ID_STAFF, idStaff)
            contentValues.put(PAYMENT_ROLL_STATUS, 1)
            contentValues.put(PAYMENT_ROLL_START_DATE, startDate)
            contentValues.put(PAYMENT_ROLL_END_DATE, endDate)
            val id: Long = db.insert(TABLE_PAYMENT_ROLL, null, contentValues)
            if (id > 0) {
                response(id, true, null)
            } else {
                response(id, false, null)
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            response(0, false, e.message)
        }

    }

}