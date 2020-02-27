package com.book.cashflow.model

import java.io.Serializable

class Transaction(val id: String,
                  val date: String,
                  val description: String,
                  val amount: Double,
                  val typeID: String): Serializable