package com.book.cashflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.book.cashflow.R
import com.book.cashflow.model.TransactionType
import com.book.cashflow.model.Type
import com.book.cashflow.ui.DialogType
import com.book.cashflow.ui.InputTransactionActivity
import kotlinx.android.synthetic.main.adapter_type.view.*

class TransactionTypeAdapter(private val dialog: DialogType,
                             private val transactionTypes: List<TransactionType>):
    RecyclerView.Adapter<TransactionTypeAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var nameView: TextView = view.typeNameView
        var typeView: TextView = view.typeView
        var rootView: ConstraintLayout = view.rootLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(dialog.context)
        val view = inflater.inflate(R.layout.adapter_type, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionTypes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transactionType = transactionTypes[position]
        holder.nameView.text = transactionType.name
        if(transactionType.type == Type.CREDIT) {
            holder.typeView.text = "Credit"
            holder.typeView.setTextColor(ContextCompat.getColor(dialog.context!!, R.color.blue))
        }
        else {
            holder.typeView.text = "Debet"
            holder.typeView.setTextColor(ContextCompat.getColor(dialog.context!!, R.color.red))
        }
        holder.rootView.setOnClickListener {
            dialog.setTypeIntoTransaction(transactionType)
        }
    }
}