package com.book.cashflow.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.book.cashflow.R
import com.book.cashflow.model.Transaction
import com.book.cashflow.adapter.TransactionAdapter
import com.book.cashflow.model.Balance
import com.book.cashflow.repository.SqLiteHandler
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat

class MainActivity : Activity(),
View.OnClickListener{

    private var transactions = arrayListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBalanceView()
        imgAddTrx.setOnClickListener(this)
        transactionAdapter = TransactionAdapter(this, transactions)
        transactionView.layoutManager = LinearLayoutManager(this)
        transactionView.adapter = transactionAdapter
        loadTransaction()
    }

    private fun setBalanceView() {
        var myBalance = SqLiteHandler(this).getBalance
        if(myBalance == null) myBalance = Balance(null, "0", "0", "0")
        balanceView.text = formatPrice(myBalance.totalBalance.toDouble())
        expenseView.text = formatPrice(myBalance.totalExpense.toDouble())
        incomeView.text = formatPrice(myBalance.totalIncome.toDouble())
        var progressValue = 0.0
        if(myBalance.totalIncome.toInt() > 0)
            progressValue = myBalance.totalBalance.toDouble()/myBalance.totalIncome.toDouble()*100
        progressBar.progress = progressValue.toInt()
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

    private fun formatPrice(amount: Double): String{
        try {
            if(amount == 0.0) return "0"
            val numberFormat = NumberFormat.getCurrencyInstance()
            val price = numberFormat.format(amount)
            return price.replace(
                "$",
                ""
            ).replace(".00", "")
        }catch (ex: Exception){
            return "0"
        }
    }
}
