package com.example.budgettestvico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgettestvico.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    // variables to calculate the actual budget
    private lateinit var transactions : ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    // variables to auth in firebase
    private lateinit var auth : FirebaseAuth

    // variables to read data

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        //here is where the data from rtdb is going to end

        // starting of querying information
        readData()

        addBtn.setOnClickListener{
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        findViewById<FloatingActionButton>(R.id.logOutBtn).setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, activity_loggin::class.java))
        }

    }

    private fun readData(){
        database = FirebaseDatabase.getInstance().getReference("expenses")
        database.child("" + auth.uid + "").get().addOnSuccessListener {
            // here is the action once the data is obtained

            if(it.exists()){

                transactions = arrayListOf<Transaction>()

                it.children.forEach { child ->
                    var tittle = child.child("description").value.toString()
                    var amount = child.child("amount").value.toString().toDouble()

                    transactions.add(
                        Transaction("" + tittle + "", amount)
                    )

                }



                transactionAdapter = TransactionAdapter(transactions)

                linearLayoutManager = LinearLayoutManager(this)

                recyclerview.apply {
                    adapter = transactionAdapter
                    layoutManager = linearLayoutManager
                }

                updateDashboard();

                /*

                val tittle = it.child("description").value
                val amount = it.child("amount").value.toString().toDouble()

                transactions = arrayListOf(
                    Transaction("" + tittle + "", amount)
                )

                transactionAdapter = TransactionAdapter(transactions)

                linearLayoutManager = LinearLayoutManager(this)

                recyclerview.apply {
                    adapter = transactionAdapter
                    layoutManager = linearLayoutManager
                }

                updateDashboard() */

            }

        }.addOnFailureListener {
            Toast.makeText(this, "Failed getting info", Toast.LENGTH_LONG).show()
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