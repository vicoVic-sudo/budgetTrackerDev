package com.example.budgettestvico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var transactions : ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //here is where the data from rtdb is going to end
        transactions = arrayListOf(
            Transaction("Weekend budget", 400.00),
            Transaction("Apples", -30.00),
            Transaction("Cereal", -45.00),
            Transaction("Cheetos", -25.00),
            Transaction("Food", -200.00),
            Transaction("Weekend budget", 400.00)
        )

        transactionAdapter = TransactionAdapter(transactions)
        linearLayoutManager = LinearLayoutManager(this)

        recyclerview.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutManager
        }

        updateDashboard()

        addBtn.setOnClickListener{
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

    }


    private fun updateDashboard(){
        val totalAmount = transactions.map { it.amount }.sum()
        val budgetAmount = transactions.filter { it.amount > 0 }.map{it.amount}.sum()
        val expenseAmount = totalAmount - budgetAmount

        balance.text = "$ %.2f".format(totalAmount)
        budget.text = "$ %.2f".format(budgetAmount)
        expense.text = "$ %.2f".format(expenseAmount)

    }
}