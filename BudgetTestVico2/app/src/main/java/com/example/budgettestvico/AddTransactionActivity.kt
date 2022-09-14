package com.example.budgettestvico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.budgettestvico.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_transaction.*


class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        var flag = 0;

        auth = FirebaseAuth.getInstance()
        println("Variable UID");
        println(auth.uid);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)



        labelInput.addTextChangedListener {
            if(it!!.isNotEmpty()){
                labelLayout.error = null
            }
        }

        amountInput.addTextChangedListener {
            if(it!!.count() > 0 ){
                amountLayout.error = null
            }
        }

        addTransactionBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty()) {
                labelLayout.error = "Please enter a valid label"
                flag += 1;
            }

            if (amount == null) {
                amountLayout.error = "Please enter a valid amount"
                flag += 1;
            }

            if(flag == 0){
                // Here is where I gonna code the write operation once I validated that the text box has something
                Toast.makeText(this, "Writting", Toast.LENGTH_LONG).show()

                database = FirebaseDatabase.getInstance().getReference("expenses/")

                val Expense = expenses(label, amount)

                database.child(""  + auth.uid).push().setValue(Expense).addOnSuccessListener {


                    Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()



                }.addOnFailureListener{
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }

            }


        }

        closeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java)
            startActivity(intent)
        }
    }
}