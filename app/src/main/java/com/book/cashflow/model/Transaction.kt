package com.book.cashflow.model

import java.io.Serializable

class Transaction(val id: String? = null,
                  val date: String,
                  val description: String,
                  val amount: String,
                  val typeID: String): Serializable