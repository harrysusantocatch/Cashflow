package com.book.cashflow.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.book.cashflow.R
import com.book.cashflow.model.Balance
import com.book.cashflow.model.Transaction
import com.book.cashflow.model.TransactionType
import com.book.cashflow.model.Type
import com.book.cashflow.repository.SqLiteHandler
import kotlinx.android.synthetic.main.activity_input_transaction.*
import java.util.*

class InputTransactionActivity : FragmentActivity(),
    View.OnClickListener {

    private lateinit var trxType: TransactionType
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_transaction)
        btnBack.setOnClickListener(this)
        imgTypeView.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        imgCalendarView.setOnClickListener(this)
    }

    fun setTypeFromDialog(type: TransactionType){
        trxType = type
        typeView.setText("${type.name} - ${type.type.name}")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBack -> onBackPressed()
            R.id.imgTypeView ->{
                showType()
            }
            R.id.btnSave -> saveTransaction()
            R.id.imgCalendarView -> showCalendar()
        }
    }

    private fun showCalendar() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            var selectedMonth = (month+1).toString()
            if(selectedMonth.length == 1){
                selectedMonth = "0${selectedMonth}"
            }
            var selectedDay = dayOfMonth.toString()
            if(selectedDay.length == 1){
                selectedDay = "0${selectedDay}"
            }
            dateView.setText("${year}-${selectedMonth}-${selectedDay}")
        }, year, month, day).show()
    }

    private fun showType() {
        val dialog = DialogType(this)
        dialog.retainInstance = true
        dialog.show(supportFragmentManager, "Type")
    }

    private fun saveTransaction() {
        var date = dateView.text.toString()
        if("Today" == date){
            val dateStr = DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString()
            date = dateStr
        }
        val desc = descriptionView.text.toString()
        val amount = amountView.text.toString()
        val typeStr = typeView.text.toString()
        if(validateInput(desc, amount, typeStr)){
            val trx = Transaction(null, date, desc, amount, trxType.id!!)
            val id = SqLiteHandler(this).insertTransaction(trx)
            if(id != null){
                updateBalance(trx, trxType)
                showToast("Transaction has been saved")
                goToHome()
            }else
                showToast("Transaction failed to save")
        }
    }

    private fun updateBalance(
        trx: Transaction,
        trxType: TransactionType
    ) {
        val currentBalance = SqLiteHandler(this).getBalance
        var income = "0"
        var expense = "0"
        if(trxType.type == Type.CREDIT){
            income = trx.amount
        }else{
            expense = trx.amount
        }
        if(currentBalance == null){
            val balanceModel = Balance(null, trx.amount, income, expense)
            SqLiteHandler(this).insertBalance(balanceModel)
        }else{
            val totalIncome = currentBalance.totalIncome.toInt() + income.toInt()
            val totalExpense = currentBalance.totalExpense.toInt() + expense.toInt()
            val totalBalance = totalIncome - totalExpense

            currentBalance.totalIncome = totalIncome.toString()
            currentBalance.totalExpense = totalExpense.toString()
            currentBalance.totalBalance = totalBalance.toString()
            SqLiteHandler(this).updateBalance(currentBalance)
        }
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun validateInput(
        desc: String,
        amount: String,
        typeStr: String
    ): Boolean {
        return when {
            desc.isEmpty() -> {
                showToast("Please fill description !!")
                false
            }
            amount.isEmpty() -> {
                showToast("Please fill amount !!")
                false
            }
            typeStr.isEmpty() -> {
                showToast("Please select type !!")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
