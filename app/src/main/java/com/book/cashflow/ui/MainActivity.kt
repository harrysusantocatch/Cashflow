package com.book.cashflow.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.book.cashflow.R
import com.book.cashflow.model.Transaction
import com.book.cashflow.adapter.TransactionAdapter
import com.book.cashflow.repository.SqLiteHandler
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
        return SqLiteHandler(this).listTransaction
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.imgAddTrx -> {
                startActivity(Intent(this, InputTransactionActivity::class.java))
            }
        }
    }
}
