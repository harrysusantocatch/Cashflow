package com.book.cashflow.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.book.cashflow.model.Transaction
import com.book.cashflow.model.TransactionType
import com.book.cashflow.model.Type
import java.util.*

class SqLiteHandler constructor(context: Context?) :
    SQLiteOpenHelper(
        context,
        DB_NAME,
        null,
        DB_VERSION
    ) {

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "db_cashflow"
        // Table Transaction
        private const val TABLE_TRANSACTION = "table_transaction"
        private const val TRANSACTION_KEY_ID = "trx_id"
        private const val TRANSACTION_KEY_DATE = "trx_date"
        private const val TRANSACTION_KEY_DESCRIPTION = "trx_description"
        private const val TRANSACTION_KEY_AMOUNT = "trx_amount"
        private const val TRANSACTION_KEY_TYPE = "trx_type"
        // Table Transaction type
        private const val TABLE_TRANSACTION_TYPE = "table_transaction_type"
        private const val TRANSACTION_TYPE_KEY_ID = "trx_type_id"
        private const val TRANSACTION_TYPE_KEY_NAME = "trx_type_name"
        private const val TRANSACTION_TYPE_KEY_TYPE = "trx_type_type"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableTransaction =
            ("CREATE TABLE " + TABLE_TRANSACTION + "("
                    + TRANSACTION_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TRANSACTION_KEY_DATE + " TEXT,"
                    + TRANSACTION_KEY_DESCRIPTION + " TEXT,"
                    + TRANSACTION_KEY_AMOUNT + " TEXT,"
                    + TRANSACTION_KEY_TYPE + " TEXT" + ")")
        db.execSQL(createTableTransaction)
        val createTableTransactionType =
            ("CREATE TABLE " + TABLE_TRANSACTION_TYPE + "("
                    + TRANSACTION_TYPE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TRANSACTION_TYPE_KEY_NAME + " TEXT,"
                    + TRANSACTION_TYPE_KEY_TYPE + " TEXT" + ")")
        db.execSQL(createTableTransactionType)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTION")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTION_TYPE")
        onCreate(db)
    }

    fun insertTransaction(trx: Transaction): Long {
        val db = this.writableDatabase
        val cValues = ContentValues()
        cValues.put(TRANSACTION_KEY_DATE, trx.date)
        cValues.put(TRANSACTION_KEY_DESCRIPTION, trx.description)
        cValues.put(TRANSACTION_KEY_AMOUNT, trx.amount)
        cValues.put(TRANSACTION_KEY_TYPE, trx.typeID)
        val newRowId = db.insert(TABLE_TRANSACTION, null, cValues)
        db.close()
        return newRowId
    }

    fun insertTransactionType(trxType: TransactionType): Long {
        val db = this.writableDatabase
        val cValues = ContentValues()
        cValues.put(TRANSACTION_TYPE_KEY_NAME, trxType.name)
        cValues.put(TRANSACTION_TYPE_KEY_TYPE, trxType.type.code.toString())
        val newRowId = db.insert(TABLE_TRANSACTION, null, cValues)
        db.close()
        return newRowId
    }

    val listTransactionType: ArrayList<TransactionType>
        @SuppressLint("Recycle")
        get() {
            val db = this.writableDatabase
            val result = arrayListOf<TransactionType>()
            val query =
                ("SELECT " + TRANSACTION_TYPE_KEY_ID + ", " + TRANSACTION_TYPE_KEY_NAME + ", " + TRANSACTION_TYPE_KEY_TYPE
                        + " FROM " + TABLE_TRANSACTION_TYPE + " ORDER BY " + TRANSACTION_TYPE_KEY_TYPE + " DESC")
            val cursor = db.rawQuery(query, null)
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(TRANSACTION_TYPE_KEY_ID))
                val name = cursor.getString(cursor.getColumnIndex(TRANSACTION_TYPE_KEY_NAME))
                val typeStr = cursor.getString(cursor.getColumnIndex(TRANSACTION_TYPE_KEY_TYPE))
                val type = Type.valueOf(typeStr)
                result.add(TransactionType(id, name, type))
            }
            db.close()
            return result
        }

    val listTransaction: ArrayList<Transaction>
        @SuppressLint("Recycle")
        get() {
            val db = this.writableDatabase
            val result = arrayListOf<Transaction>()
            val query =
                ("SELECT " + TRANSACTION_KEY_ID + ", " + TRANSACTION_KEY_DATE + ", " + TRANSACTION_KEY_DESCRIPTION
                        + ", " + TRANSACTION_KEY_AMOUNT + ", " + TRANSACTION_KEY_TYPE
                        + " FROM " + TABLE_TRANSACTION + " ORDER BY " + TRANSACTION_KEY_DATE + " DESC")
            val cursor = db.rawQuery(query, null)
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_ID))
                val date = cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_DATE))
                val description = cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_DESCRIPTION))
                val amount = cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_AMOUNT))
                val type = cursor.getString(cursor.getColumnIndex(TRANSACTION_KEY_TYPE))
                result.add(Transaction(id, date, description, amount.toDouble(), type))
            }
            db.close()
            return result
        }
}