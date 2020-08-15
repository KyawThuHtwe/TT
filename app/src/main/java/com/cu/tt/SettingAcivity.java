package com.cu.tt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cu.tt.Alarm.AlarmReceiver;

import java.util.Calendar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SettingAcivity extends AppCompatActivity {

    RelativeLayout timetable,rollcall,alarm,about;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_acivity);
        alarm=findViewById(R.id.alarm);
        ////////////////////////
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),SetAlarmActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(getApplicationContext(),"Coming Soon",Toast.LENGTH_SHORT).show();
            }
        });
        ///////////////
        about=findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AboutActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        timetable=findViewById(R.id.timetable);
        rollcall=findViewById(R.id.rollcall);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final DataBaseHelper dataBaseHelper=new DataBaseHelper(this);
        rollcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confrim("RollCall");
            }
        });
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confrim("TimeTable");
            }
        });
    }
    public void deleteTimeTable(){
        DataBaseHelper dataBaseHelper=new DataBaseHelper(this);
        try{
            boolean result=dataBaseHelper.deleteTimeTable();
            if(result){
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteRollCallTable(){
        DataBaseHelper dataBaseHelper=new DataBaseHelper(this);
        try{
            boolean result=dataBaseHelper.deleteRollCallTable();
            if(result){
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void confrim(final String table){
        try {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Delete");
            builder.setMessage("Are you sure you want to Delete?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (table){
                        case "TimeTable":
                            deleteTimeTable();
                            deleteRollCallTable();
                            break;
                        case "RollCall":
                            deleteRollCallTable();
                            break;
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setBootReceiverEnabled(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        //startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
    public void setBootReceiverEnabled(int componentEnabledState){
        ComponentName componentName=new ComponentName(this, AlarmReceiver.class);
        PackageManager packageManager=getPackageManager();
        packageManager.setComponentEnabledSetting(componentName,componentEnabledState,PackageManager.DONT_KILL_APP);
    }
}