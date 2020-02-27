package com.book.cashflow.model

import java.io.Serializable

class TransactionType(val id: String?,
                      val name: String,
                      val type: Type): Serializable

enum class Type(val code: Int){
    DEBET(0), CREDIT(1)
}