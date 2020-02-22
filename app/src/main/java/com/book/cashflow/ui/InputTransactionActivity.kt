package com.book.cashflow.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.book.cashflow.R
import kotlinx.android.synthetic.main.activity_input_transaction.*

class InputTransactionActivity : FragmentActivity(),
    View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_transaction)
        btnBack.setOnClickListener(this)
        imgTypeView.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBack -> onBackPressed()
            R.id.imgTypeView ->{
                val dialog = DialogType()
                dialog.retainInstance = true
                dialog.show(supportFragmentManager, "Type")
            }
        }
    }
}
