package com.cu.tt;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cu.tt.Alarm.AlarmReceiver;

import java.util.Calendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {

    Button close;
    TextView time;
    TextView subject;

    @Override
    protected void onStart() {
        super.onStart();
        dat();
    }
    @SuppressLint("SetTextI18n")
    public void dat(){
        TimePicker timePicker=new TimePicker(this);
        int hr= timePicker.getCurrentHour();
        int min=timePicker.getCurrentMinute();
        String des;
        String s=min+"";
        if(s.length()==1){
            s="0"+s;
        }
        if(hr>12){
            hr-=12;
            des=" PM";
        }else {
            des=" AM";
        }
        time.setText(hr+":"+s+des);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        time=findViewById(R.id.ctime);
        //day=findViewById(R.id.date);
        subject=findViewById(R.id.subject);
        close=findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                setBootReceiverEnabled(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        });
        getSubject();
        dat();
    }
    public void setBootReceiverEnabled(int componentEnabledState){
        ComponentName componentName=new ComponentName(this,AlarmReceiver.class);
        PackageManager packageManager=getPackageManager();
        packageManager.setComponentEnabledSetting(componentName,componentEnabledState,PackageManager.DONT_KILL_APP);
    }
    public void getSubject(){
        DataBaseHelper myDb=new DataBaseHelper(this);
        try {
            Cursor res = myDb.getAllData();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    int day;
                    try {
                        Calendar calendar=Calendar.getInstance();
                        day=calendar.get(Calendar.DAY_OF_WEEK);
                        Toast.makeText(getApplicationContext(),day+"", Toast.LENGTH_SHORT).show();

                        switch (day){
                            case 2:
                                if(res.getString(9).equals("Monday")){
                                    getSubjectOk(res.getString(1),res.getString(3));
                                }
                                break;
                            case 3:
                                if(res.getString(9).equals("Tuesday")){
                                    getSubjectOk(res.getString(1),res.getString(3));
                                }
                                break;
                            case 4:
                                if(res.getString(9).equals("Wednesday")){
                                    getSubjectOk(res.getString(1),res.getString(3));
                                }
                                break;
                            case 5:
                                if(res.getString(9).equals("Thursday")){
                                    getSubjectOk(res.getString(1),res.getString(3));
                                }
                                break;
                            case 6:
                                if(res.getString(9).equals("Friday")){
                                    getSubjectOk(res.getString(1),res.getString(3));
                                }
                                break;
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }
            myDb.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void getSubjectOk(String time,String subjecttext){
        try {
            int hr=Integer.parseInt(time.split(":")[0]);
            int min=Integer.parseInt(time.split(":")[1].replaceAll("\\D+","").replaceAll("^0+",""));
            TimePicker timePicker=new TimePicker(this);
            int currentHour= timePicker.getCurrentHour();
            int currentMinute=timePicker.getCurrentMinute();
            if(currentHour==hr && currentMinute==min){
                subject.setText(subjecttext);
            }

        }catch (NumberFormatException nfe){
            Toast.makeText(getApplicationContext(), nfe.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

}