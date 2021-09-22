package com.pns.lk

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


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
            val chip:Chip? = findViewById(checkedId)
            val myEdit = sharedPreferences.edit()
            myEdit.putInt("checkedId", checkedId);
            if(group.getChildAt(0).id==checkedId){
                myEdit.putInt("durationIndex", 0)
            }else if(group.getChildAt(1).id==checkedId){
                myEdit.putInt("durationIndex", 1)
            }
            else if(group.getChildAt(2).id==checkedId){
                myEdit.putInt("durationIndex", 2)
            }
            else if(group.getChildAt(3).id==checkedId){
                myEdit.putInt("durationIndex", 3)
            }

            myEdit.commit();
            Utility.readAlert(this)
            Utility.readMissedCall(this)
        }

        editMsg.setOnClickListener { v ->
            showdialog()
        }
    }

    fun showdialog(){
        val sharedPreferences = getSharedPreferences("PNS_PREF", MODE_PRIVATE)
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Update message")
        val input = EditText(this)
        input.setText(sharedPreferences.getString("replyMsg","I will call you later"))
        input.hint = "Enter message"
        //input.inputType = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE
        input.isSingleLine = false
        input.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            var msgText = input.text.toString()
            val sharedPreferences = getSharedPreferences("PNS_PREF", MODE_PRIVATE)
            val myEdit = sharedPreferences.edit()
            myEdit.putString("replyMsg", msgText)
            myEdit.commit()
            val replyMsg: TextView = findViewById(R.id.replyMsg)
            replyMsg.text = msgText

            finish();
            overridePendingTransition(0, 0);
            startActivity(intent)
            overridePendingTransition(0, 0);

        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)

    }

}


