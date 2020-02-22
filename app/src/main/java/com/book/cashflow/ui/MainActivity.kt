package com.book.cashflow.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.book.cashflow.R
import com.book.cashflow.model.Transaction
import com.book.cashflow.adapter.TransactionAdapter
import com.book.cashflow.model.TransactionType
import com.book.cashflow.model.Type
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(),
View.OnClickListener{

    private var transactions = arrayListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgAddTrx.setOnClickListener(this)
        transactionAdapter = TransactionAdapter(this, transactions)
        transactionView.layoutManager = LinearLayoutManager(this)
        transactionView.adapter = transactionAdapter
        loadTransaction()
    }

    private fun loadTransaction() {
        val newTransactions = getTransaction()
        if(newTransactions.size > 0){
            layoutNoContent.visibility = View.GONE
            transactionView.visibility = View.VISIBLE
            transactions.clear()
            transactions.addAll(newTransactions)
            transactionAdapter.notifyDataSetChanged()
        }else{
            layoutNoContent.visibility = View.VISIBLE
            transactionView.visibility = View.GONE
        }
    }

    private fun getTransaction(): ArrayList<Transaction> {
        val trx = arrayListOf<Transaction>()
        val tx1 = Transaction(
            "2-2-2020",
            "Pembelian alat tulis",
            20000.0,
            TransactionType("Pembelian", Type.DEBET)
        )
        val tx2 = Transaction(
            "4-2-2020",
            "Pendanaan dari mars.inc",
            2000000.0,
            TransactionType("Pendanaan", Type.CREDIT)
        )
        val tx3 = Transaction(
            "8-2-2020",
            "Pembelian sapu lidi",
            8000.0,
            TransactionType("Pembelian", Type.DEBET)
        )
        val tx4 = Transaction(
            "11-2-2020",
            "Pembelian keyboard",
            240000.0,
            TransactionType("Pembelian", Type.DEBET)
        )
        val tx5 = Transaction(
            "11-2-2020",
            "Pendanaan keyboard",
            200000.0,
            TransactionType("Pendanaan", Type.CREDIT)
        )
        trx.add(tx1)
        trx.add(tx2)
        trx.add(tx3)
        trx.add(tx4)
        trx.add(tx5)
        return trx
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.imgAddTrx -> {
                startActivity(Intent(this, InputTransactionActivity::class.java))
            }
        }
    }
}
