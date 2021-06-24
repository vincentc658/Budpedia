package com.gajikaryawan.gajiq.database
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.gajikaryawan.gajiq.model.Absence
import com.gajikaryawan.gajiq.model.Admin
import com.gajikaryawan.gajiq.model.PaymentRoll
import com.gajikaryawan.gajiq.model.Staff
import com.gajikaryawan.gajiq.util.Constants
import com.gajikaryawan.gajiq.util.Constants.Companion.ABSENCE_DATE
import com.gajikaryawan.gajiq.util.Constants.Companion.ABSENCE_ID
import com.gajikaryawan.gajiq.util.Constants.Companion.ABSENCE_ID_PAYMENT_ROLL
import com.gajikaryawan.gajiq.util.Constants.Companion.ABSENCE_IS_ATTEND
import com.gajikaryawan.gajiq.util.Constants.Companion.ABSENCE_IS_PAID
import com.gajikaryawan.gajiq.util.Constants.Companion.ADMIN_EMAIL
import com.gajikaryawan.gajiq.util.Constants.Companion.ADMIN_ID
import com.gajikaryawan.gajiq.util.Constants.Companion.ADMIN_NAME
import com.gajikaryawan.gajiq.util.Constants.Companion.ADMIN_PASSWORD
import com.gajikaryawan.gajiq.util.Constants.Companion.ADMIN_PHONE
import com.gajikaryawan.gajiq.util.Constants.Companion.DATABASE_NAME
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_DATE
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_END_DATE
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_ID
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_ID_STAFF
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_PAID
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_START_DATE
import com.gajikaryawan.gajiq.util.Constants.Companion.PAYMENT_ROLL_STATUS
import com.gajikaryawan.gajiq.util.Constants.Companion.STAFF_ADMIN_ID
import com.gajikaryawan.gajiq.util.Constants.Companion.STAFF_ID
import com.gajikaryawan.gajiq.util.Constants.Companion.STAFF_IS_PER_MONTH
import com.gajikaryawan.gajiq.util.Constants.Companion.STAFF_NAME
import com.gajikaryawan.gajiq.util.Constants.Companion.STAFF_PHONE
import com.gajikaryawan.gajiq.util.Constants.Companion.STAFF_SALARY
import com.gajikaryawan.gajiq.util.Constants.Companion.TABLE_ABSENCE
import com.gajikaryawan.gajiq.util.Constants.Companion.TABLE_ADMIN
import com.gajikaryawan.gajiq.util.Constants.Companion.TABLE_PAYMENT_ROLL
import com.gajikaryawan.gajiq.util.Constants.Companion.TABLE_STAFF
import com.gajikaryawan.gajiq.util.SessionManager

class DatabaseController(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_STAFF_ADMIN = ("CREATE TABLE " + Constants.TABLE_ADMIN + "("
                + Constants.ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.ADMIN_NAME + " TEXT NOT NULL, "
                + Constants.ADMIN_EMAIL + " TEXT NOT NULL, "
                + Constants.ADMIN_PASSWORD + " TEXT NOT NULL, "
                + Constants.ADMIN_PHONE + "  VARCHAR(15) NOT NULL UNIQUE "
                + ")")

        val CREATE_STAFF_TABLE = ("CREATE TABLE " + Constants.TABLE_STAFF + "("
                + Constants.STAFF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.STAFF_ADMIN_ID + " INTEGER NOT NULL, "
                + Constants.STAFF_NAME + " TEXT NOT NULL, "
                + Constants.STAFF_IS_PER_MONTH + " BOOLEAN NOT NULL, "
                + Constants.STAFF_PHONE + "  VARCHAR(15) NOT NULL UNIQUE, "
                + Constants.STAFF_SALARY + " BIGINT  NOT NULL, "
                + "FOREIGN KEY (" + Constants.STAFF_ADMIN_ID + ") REFERENCES " + Constants.TABLE_ADMIN + "(" + Constants.ADMIN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE "
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
                + Constants.ABSENCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.ABSENCE_ID_PAYMENT_ROLL + " INTEGER NOT NULL, "
                + Constants.ABSENCE_IS_ATTEND + " BOOLEAN NOT NULL, "
                + Constants.ABSENCE_IS_PAID + " BOOLEAN  NOT NULL, "
                + Constants.ABSENCE_DATE + " DATE  NOT NULL, "
                + "FOREIGN KEY (" + Constants.ABSENCE_ID_PAYMENT_ROLL + ") REFERENCES " + Constants.TABLE_PAYMENT_ROLL + "(" + Constants.PAYMENT_ROLL_ID + ") ON UPDATE CASCADE ON DELETE CASCADE "
                + ")")

        p0?.execSQL(CREATE_STAFF_ADMIN)
        p0?.execSQL(CREATE_STAFF_TABLE)
        p0?.execSQL(CREATE_PAYMENT_ROLL_TABLE)
        p0?.execSQL(CREATE_ABSENCE_TABLE)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        // Drop older table if existed
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_ADMIN")
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
        response: (id: Long, status: Boolean, message: String?) -> Unit
    ) {
        try {

            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(STAFF_NAME, name)
            contentValues.put(STAFF_IS_PER_MONTH, isPermonth)
            contentValues.put(STAFF_SALARY, salary)
            contentValues.put(STAFF_PHONE, phone)
            contentValues.put(STAFF_ADMIN_ID, SessionManager.id)
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

    fun insertDataAdmin(
        name: String,
        phone: String, email: String, password: String,
        response: (id: Long, status: Boolean, message: String?) -> Unit
    ) {
        try {

            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(ADMIN_NAME, name)
            contentValues.put(ADMIN_EMAIL, email)
            contentValues.put(ADMIN_PASSWORD, password)
            contentValues.put(ADMIN_PHONE, phone)
            val id: Long = db.insert(TABLE_ADMIN, null, contentValues)
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

    fun getAdmin(email: String, password: String, response: (admin: Admin?) -> Unit) {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "select * from $TABLE_ADMIN where $ADMIN_EMAIL =? and  $ADMIN_PASSWORD =?",
            arrayOf(email, password)
        )
        if (res != null
            && res.moveToFirst()
        ) {
            Log.d("data admin id", "${res.getInt(res.getColumnIndex(ADMIN_ID))}")
            val data = Admin()
            data.id = res.getInt(res.getColumnIndex(ADMIN_ID))
            data.name = res.getString(res.getColumnIndex(ADMIN_NAME))
            data.phone = res.getString(res.getColumnIndex(ADMIN_PHONE))
            data.email = res.getString(res.getColumnIndex(ADMIN_EMAIL))
            response(data)
        }
        response(null)
    }

    fun getStaff(): ArrayList<Staff> {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "select * from $TABLE_STAFF where $STAFF_ADMIN_ID =?",
            arrayOf(SessionManager.id)
        )
        val datas = ArrayList<Staff>()
        while (res.moveToNext()) {
            val data = Staff()
            data.id = res.getInt(res.getColumnIndex(STAFF_ID))
            data.salary = res.getInt(res.getColumnIndex(STAFF_SALARY))
            data.name = res.getString(res.getColumnIndex(STAFF_NAME))
            data.phone = res.getString(res.getColumnIndex(STAFF_PHONE))
            data.isPerMonth = res.getInt(res.getColumnIndex(STAFF_IS_PER_MONTH)) == 1
            datas.add(data)
        }
        return datas
    }

    fun getPaymentRol(id: Long, date: String): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PAYMENT_ROLL WHERE $PAYMENT_ROLL_START_DATE >= ? and $PAYMENT_ROLL_ID_STAFF=?  ",
            arrayOf(date, id.toString())
        )

        return cursor != null && cursor.moveToFirst()
    }

    fun insertPaymentRoll(
        idStaff: Long,
        startDate: String,
        endDate: String,
        response: (id: Long, status: Boolean, message: String?) -> Unit
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

    fun insertAbsence(
        idPaymentRoll: Long, date: String,
        response: (status: Boolean, message: String?) -> Unit
    ) {
        try {

            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(ABSENCE_IS_ATTEND, true)
            contentValues.put(ABSENCE_IS_PAID, true)
            contentValues.put(ABSENCE_ID_PAYMENT_ROLL, idPaymentRoll)
            contentValues.put(ABSENCE_DATE, date)
            val id: Long = db.insert(TABLE_ABSENCE, null, contentValues)
            if (id > 0) {
                response(true, null)
            } else {
                response(false, null)
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            response(false, e.message)
        }

    }

    fun getPaymentRollId(date: String, idStaff: String): Int {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT $PAYMENT_ROLL_ID FROM $TABLE_PAYMENT_ROLL where $PAYMENT_ROLL_START_DATE>= ? and $PAYMENT_ROLL_ID_STAFF =?",
            arrayOf(date, idStaff)
        )
        if (res != null && res.moveToFirst()) {
            return res.getInt(res.getColumnIndex(PAYMENT_ROLL_ID))
        }
        return 0
    }

    fun getLastAbsence(idPaymentRoll: String): String {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT $ABSENCE_DATE FROM $TABLE_ABSENCE where $ABSENCE_ID_PAYMENT_ROLL>= ?",
            arrayOf(idPaymentRoll)
        )
        if (res != null && res.moveToLast()) {
            return res.getString(res.getColumnIndex(ABSENCE_DATE))
        }
        return ""
    }

    fun getAbsencePerDay(date: String): ArrayList<Absence> {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT $ABSENCE_DATE,$ABSENCE_IS_ATTEND,$ABSENCE_IS_PAID,$ABSENCE_ID_PAYMENT_ROLL," +
                    "$TABLE_ABSENCE.$ABSENCE_ID,$STAFF_NAME,$STAFF_IS_PER_MONTH,  $TABLE_STAFF.$STAFF_ID as $PAYMENT_ROLL_ID_STAFF FROM $TABLE_ABSENCE" +
                    " inner join $TABLE_PAYMENT_ROLL on $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID = $TABLE_ABSENCE.$ABSENCE_ID_PAYMENT_ROLL" +
                    " inner join $TABLE_STAFF on  $TABLE_STAFF.$STAFF_ID = $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID_STAFF where $ABSENCE_DATE=?",
            arrayOf(date)
        )
        val datas = ArrayList<Absence>()
        while (res.moveToNext()) {
            val data = Absence()
            data.id = res.getInt(res.getColumnIndex(ABSENCE_ID))
            data.idPaymentRoll = res.getInt(res.getColumnIndex(ABSENCE_ID_PAYMENT_ROLL))
            data.idStaff = res.getInt(res.getColumnIndex(PAYMENT_ROLL_ID_STAFF))
            data.isAttend = res.getInt(res.getColumnIndex(ABSENCE_IS_ATTEND)) == 1
            data.isPaid = res.getInt(res.getColumnIndex(ABSENCE_IS_PAID)) == 1
            data.staffName = res.getString(res.getColumnIndex(STAFF_NAME))
            data.isPerMonth = res.getInt(res.getColumnIndex(STAFF_IS_PER_MONTH)) == 1
            datas.add(data)
        }
        return datas
    }

    fun updateStatusAbsenceStaff(
        idAbsence: String,
        cv: ContentValues,
        response: (status: Boolean) -> Unit
    ) {
        val db = this.writableDatabase
        val res = db.update(TABLE_ABSENCE, cv, "$ABSENCE_ID=?", arrayOf(idAbsence))
        if (res > 0) {
            response(true)
        } else {
            response(false)
        }
    }

    fun getTotalWorkingDay(idStaff: String, transactionType: String): Int {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT  count($TABLE_ABSENCE.$ABSENCE_ID) as total_working_day from $TABLE_PAYMENT_ROLL  join $TABLE_ABSENCE on " +
                    "$TABLE_ABSENCE.$ABSENCE_ID_PAYMENT_ROLL= $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID WHERE " +
                    "$TABLE_ABSENCE.$ABSENCE_IS_PAID=1 " +
                    "and $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID_STAFF=? " +
                    "and $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_STATUS=?", arrayOf(idStaff, transactionType)
        )
        if (res != null && res.moveToFirst()) {
            return res.getInt(res.getColumnIndex("total_working_day"))
        }
        return 0
    }

    fun getPaymentRoll(idStaff: String, status: String): ArrayList<PaymentRoll> {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_START_DATE,$TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_END_DATE,$TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID," +
                    " count($TABLE_ABSENCE.$ABSENCE_ID) as total_working_day from $TABLE_PAYMENT_ROLL  join $TABLE_ABSENCE on " +
                    "$TABLE_ABSENCE.$ABSENCE_ID_PAYMENT_ROLL= $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID WHERE " +
                    "$TABLE_ABSENCE.$ABSENCE_IS_PAID=1 " +
                    "and $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_STATUS=?" +
                    "and $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID_STAFF=? group by $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID",
            arrayOf(status, idStaff)
        )
        val datas = ArrayList<PaymentRoll>()
        while (res.moveToNext()) {
            val data = PaymentRoll()
            data.idPaymentRoll = res.getInt(res.getColumnIndex(PAYMENT_ROLL_ID))
            data.startDate = res.getString(res.getColumnIndex(PAYMENT_ROLL_START_DATE))
            data.endDate = res.getString(res.getColumnIndex(PAYMENT_ROLL_END_DATE))
            data.totalWorkingDay = res.getInt(res.getColumnIndex("total_working_day"))
            datas.add(data)
        }
        return datas
    }

    fun getPaymentRollById(idPaymentRoll: Int): PaymentRoll {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_DATE,$TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID_STAFF,$TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_START_DATE,$TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_END_DATE,$TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID," +
                    " count($TABLE_ABSENCE.$ABSENCE_ID) as total_working_day from $TABLE_PAYMENT_ROLL  join $TABLE_ABSENCE on " +
                    "$TABLE_ABSENCE.$ABSENCE_ID_PAYMENT_ROLL= $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID WHERE " +
                    "$TABLE_ABSENCE.$ABSENCE_IS_PAID=1 " +
                    "and $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID=? group by $TABLE_PAYMENT_ROLL.$PAYMENT_ROLL_ID",
            arrayOf(idPaymentRoll.toString())
        )
        val data = PaymentRoll()
        if (res != null && res.moveToFirst()) {
            data.idStaff = res.getInt(res.getColumnIndex(PAYMENT_ROLL_ID))
            data.idPaymentRoll = res.getInt(res.getColumnIndex(PAYMENT_ROLL_ID_STAFF))
            data.startDate = res.getString(res.getColumnIndex(PAYMENT_ROLL_START_DATE))
            data.endDate = res.getString(res.getColumnIndex(PAYMENT_ROLL_END_DATE))
            data.paymentDate = res.getString(res.getColumnIndex(PAYMENT_ROLL_DATE))
            data.totalWorkingDay = res.getInt(res.getColumnIndex("total_working_day"))
        }
        return data
    }

    fun getAbsenceByPaymentRoll(idPaymentRoll: String): ArrayList<Absence> {
        val db = this.writableDatabase
        val res = db.rawQuery(
            "SELECT $ABSENCE_DATE,$ABSENCE_IS_ATTEND,$ABSENCE_IS_PAID FROM $TABLE_ABSENCE" +
                    " where $ABSENCE_ID_PAYMENT_ROLL = ?",
            arrayOf(idPaymentRoll)
        )
        val datas = ArrayList<Absence>()
        while (res.moveToNext()) {
            val data = Absence()
            data.date = res.getString(res.getColumnIndex(ABSENCE_DATE))
            data.isAttend = res.getInt(res.getColumnIndex(ABSENCE_IS_ATTEND)) == 1
            data.isPaid = res.getInt(res.getColumnIndex(ABSENCE_IS_PAID)) == 1
            datas.add(data)
        }
        return datas
    }

    fun updatePaymentRollStatus(
        idPaymentRoll: String, date: String, response: (status: Boolean) -> Unit

    ) {
        try {

            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(PAYMENT_ROLL_STATUS, PAYMENT_ROLL_PAID)
            contentValues.put(PAYMENT_ROLL_DATE, date)
            val id = db.update(
                TABLE_PAYMENT_ROLL,
                contentValues,
                " $PAYMENT_ROLL_ID = ?",
                arrayOf(idPaymentRoll)
            )
            if (id > 0) {
                response(true)
            } else {
                response(false)
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }

    }
    fun deleteStaff(
        idStaff: String,
        response: (status: Boolean, message: String?) -> Unit
    ) {
        try {

            val db = this.writableDatabase
            val id: Int = db.delete(TABLE_STAFF, "$STAFF_ID =?", arrayOf(idStaff))
            if (id > 0) {
                response(true, null)
            } else {
                response( false, null)
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            response( false, e.message)
        }
    }
}