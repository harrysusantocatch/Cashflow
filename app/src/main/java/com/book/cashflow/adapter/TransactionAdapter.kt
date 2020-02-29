package com.book.cashflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.book.cashflow.R
import com.book.cashflow.model.Transaction
import com.book.cashflow.model.TransactionType
import com.book.cashflow.model.Type
import com.book.cashflow.repository.SqLiteHandler
import kotlinx.android.synthetic.main.adapter_transaction.view.*
import java.text.NumberFormat

class TransactionAdapter(private val context: Context,
                         private val transactions: List<Transaction>):
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var dayView: TextView = view.dayView
        var monthView: TextView = view.monthView
        var yearView: TextView = view.yearView
        var descriptionView: TextView = view.descriptionView
        var amountView: TextView = view.amountView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.adapter_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        val dateArr = transaction.date.split("-")
        var day = dateArr[2].trim()
        var month = dateArr[1].trim()
        val year = dateArr[0].trim()
        if(day.length == 1) day = "0${day}"
        if(month.length == 1) month = "0${month}"
        holder.dayView.text = day
        holder.monthView.text = month
        holder.yearView.text = year
        holder.descriptionView.text = transaction.description
        holder.amountView.text = formatPrice(transaction.amount.toDouble())
        val trxType = getTransactionType(transaction.typeID)
        trxType?.let {
            if( it.type == Type.CREDIT){
            holder.amountView.setTextColor(ContextCompat.getColor(context, R.color.blue))
            }else{
                holder.amountView.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
    }

    private fun getTransactionType(id: String): TransactionType?{
        val transactionTypes = SqLiteHandler(context).listTransactionType
        for (trxType in transactionTypes){
            if(trxType.id == id)
                return trxType
        }
        return null
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