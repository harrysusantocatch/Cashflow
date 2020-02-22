package com.book.cashflow.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.book.cashflow.R
import com.book.cashflow.adapter.TransactionTypeAdapter
import com.book.cashflow.model.TransactionType
import com.book.cashflow.model.Type
import kotlinx.android.synthetic.main.dialog_type.*
import kotlinx.android.synthetic.main.dialog_type.view.*

class DialogType: DialogFragment() {

    private var types = arrayListOf<TransactionType>()
    private lateinit var adapter: TransactionTypeAdapter
    private var stateAdd = false

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.dialog_type, container, false)
        setOnCLickListener(rootView)
        adapter = TransactionTypeAdapter(context!!, types)
        rootView.typeView.layoutManager = LinearLayoutManager(context)
        rootView.typeView.adapter = adapter
        loadTypes()
        setStyle(STYLE_NO_FRAME, R.style.MyDialog)
        return rootView
    }

    private fun setOnCLickListener(rootView: View) {
        rootView.btnClose.setOnClickListener {
            if (stateAdd) {
                stateAdd = false
                showLayout(rootView)
                rootView.btnClose.setImageDrawable(context?.getDrawable(R.drawable.ic_clear))
            } else
                dismiss()
        }
        rootView.imgAddType.setOnClickListener {
            stateAdd = true
            showLayout(rootView)
            rootView.btnClose.setImageDrawable(context?.getDrawable(R.drawable.ic_arrow_back))
        }
        rootView.btnSave.setOnClickListener {
            val typeName = inputTypeName.text.toString()
            if(typeName.isEmpty())
                Toast.makeText(context, "Please fill type name", Toast.LENGTH_SHORT).show()
            else{
                val selectedId = radioType.checkedRadioButtonId
                val radioButtonSelected = rootView.findViewById<RadioButton>(selectedId)
                var type = Type.DEBET
                if(radioButtonSelected.text.toString() == "Credit")
                    type = Type.CREDIT
                val transactionType = TransactionType(typeName, type)
            }
        }
    }

    override fun onResume() {
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window!!.attributes = params as android.view.WindowManager.LayoutParams
        super.onResume()
    }

    private fun loadTypes() {
        val tp1 = TransactionType("Test 1", Type.CREDIT)
        val tp2 = TransactionType("Test 2", Type.CREDIT)
        val tp3 = TransactionType("Test 3", Type.DEBET)
        val tp4 = TransactionType("Test 4", Type.DEBET)
        types.add(tp1)
        types.add(tp2)
        types.add(tp3)
        types.add(tp4)
        adapter.notifyDataSetChanged()
    }

    private fun showLayout(rootView: View){
        if(stateAdd){
            rootView.layoutAdd.visibility = View.VISIBLE
            rootView.typeView.visibility = View.GONE
        }else{
            rootView.layoutAdd.visibility = View.GONE
            rootView.typeView.visibility = View.VISIBLE
        }
    }
}