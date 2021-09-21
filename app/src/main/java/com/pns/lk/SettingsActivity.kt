package com.pns.lk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val chipGroup: ChipGroup = findViewById(R.id.chipGroup)
        val sharedPreferences = getSharedPreferences("PNS_PREF", MODE_PRIVATE)
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
    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)

    }

}


