package com.gajikaryawan.gajiq.util

class Constants {
    companion object {
        const val DATABASE_NAME = "staff-db"

        const val TABLE_STAFF = "staff"
        const val STAFF_ID = "id"
        const val STAFF_NAME = "name"
        const val STAFF_IS_PER_MONTH = "is_per_month"
        const val STAFF_SALARY = "salary"
        const val STAFF_PHONE = "phone_number"

        const val TABLE_PAYMENT_ROLL = "payment_roll"
        const val PAYMENT_ROLL_ID = "id"
        const val PAYMENT_ROLL_DATE = "payment_date"
        const val PAYMENT_ROLL_ID_STAFF = "id_staff"
        const val PAYMENT_ROLL_STATUS = "status_payment"
        const val PAYMENT_ROLL_START_DATE = "start_date"
        const val PAYMENT_ROLL_END_DATE = "end_date"

        const val TABLE_ABSENCE = "absence"
        const val ABSENCE_ID = "id"
        const val ABSENCE_DATE = "absence_time"
        const val ABSENCE_IS_ATTEND = "is_attend"
        const val ABSENCE_IS_PAID = "is_paid"
        const val ABSENCE_ID_PAYMENT_ROLL = "id_payment_roll"

    }
}