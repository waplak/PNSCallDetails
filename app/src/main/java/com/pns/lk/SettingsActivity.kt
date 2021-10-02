package com.pns.lk

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup
import java.util.*


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val chipGroup: ChipGroup = findViewById(R.id.chipGroup)
        val replyMsg: TextView = findViewById(R.id.replyMsg)
        val editMsg: ImageButton = findViewById(R.id.editMsg)
        val sharedPreferences = getSharedPreferences("PNS_PREF", MODE_PRIVATE)
        replyMsg.text = sharedPreferences.getString("replyMsg","I will call you later")
        if(sharedPreferences.contains("checkedId")){
            chipGroup.check(sharedPreferences.getInt("checkedId", 0))
        }else {
            chipGroup.check(chipGroup.getChildAt(2).id)
        }
        chipGroup.setOnCheckedChangeListener{group,checkedId:Int ->
            //val chip:Chip? = findViewById(checkedId)
            val myEdit = sharedPreferences.edit()
            myEdit.putInt("checkedId", checkedId)
            val limitCal = Calendar.getInstance()
            if(group.getChildAt(0).id==checkedId){
                myEdit.putInt("durationIndex", 0)
                limitCal.add(Calendar.MONTH, -1)
            }else if(group.getChildAt(1).id==checkedId){
                myEdit.putInt("durationIndex", 1)
                limitCal.add(Calendar.MONTH, -3)
            }
            else if(group.getChildAt(2).id==checkedId){
                myEdit.putInt("durationIndex", 2)
                limitCal.add(Calendar.MONTH, -6)
            }
            else if(group.getChildAt(3).id==checkedId){
                myEdit.putInt("durationIndex", 3)
            }


            myEdit.apply()
        }

        editMsg.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(){
        val sharedPreferences = getSharedPreferences("PNS_PREF", MODE_PRIVATE)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Update message")
        val input = EditText(this)
        input.setText(sharedPreferences.getString("replyMsg","I will call you later"))
        input.hint = "Enter message"
        //input.inputType = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE
        input.isSingleLine = false
        input.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { _, _ ->
            // Here you get get input text from the Edittext
            val msgText = input.text.toString()
            val myEdit = sharedPreferences.edit()
            myEdit.putString("replyMsg", msgText)
            myEdit.apply()
            val replyMsg: TextView = findViewById(R.id.replyMsg)
            replyMsg.text = msgText

            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)

        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)

    }

}


