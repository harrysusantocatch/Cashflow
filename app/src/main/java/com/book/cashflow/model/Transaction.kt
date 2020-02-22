package com.book.cashflow.model

import java.io.Serializable

class Transaction(val date: String,
                  val description: String,
                  val amount: Double,
                  val type: TransactionType): Serializable