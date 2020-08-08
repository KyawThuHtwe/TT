package com.cu.tt;

import android.app.AlarmManager;
import android.app.LocalActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cu.tt.Alarm.AlarmReceiver;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static MainActivity inst;
    TextView date,time;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public static MainActivity instance(){
        return inst;
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst=this;
        sendAlarm();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        try {
            dat();
            TabHost tabHost = findViewById(R.id.tabhost);
            LocalActivityManager localActivityManager=new LocalActivityManager(this,false);
            localActivityManager.dispatchCreate(savedInstanceState);
            tabHost.setup(localActivityManager);
            TabHost.TabSpec spec;
            spec=tabHost.newTabSpec("Mon");
            spec.setIndicator("Mon");
            spec.setContent(new Intent(getApplicationContext(), MonActivity.class));
            tabHost.addTab(spec);

            spec=tabHost.newTabSpec("Tue");
            spec.setIndicator("Tue");
            spec.setContent(new Intent(getApplicationContext(), TueActivity.class));
            tabHost.addTab(spec);

            spec=tabHost.newTabSpec("Wed");
            spec.setIndicator("Wed");
            spec.setContent(new Intent(getApplicationContext(), WedActivity.class));
            tabHost.addTab(spec);

            spec=tabHost.newTabSpec("Thu");
            spec.setIndicator("Thu");
            spec.setContent(new Intent(getApplicationContext(), ThuActivity.class));
            tabHost.addTab(spec);

            spec=tabHost.newTabSpec("Fri");
            spec.setIndicator("Fri");
            spec.setContent(new Intent(getApplicationContext(), FriActvity.class));
            tabHost.addTab(spec);

            ////
            try {
                Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_WEEK);
                switch (day){
                    case 2:
                        tabHost.setCurrentTab(0);
                        break;
                    case 3:
                        tabHost.setCurrentTab(1);
                        break;
                    case 4:
                        tabHost.setCurrentTab(2);
                        break;
                    case 5:
                        tabHost.setCurrentTab(3);
                        break;
                    case 6:
                        tabHost.setCurrentTab(4);
                        break;
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String s) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        sendAlarm();


    }

    public void dat(){
        DatePicker datePicker=new DatePicker(this);
        date.setText("Date."+datePicker.getDayOfMonth()+"/"+datePicker.getMonth()+"/"+datePicker.getYear()+"");
        TimePicker timePicker=new TimePicker(this);
        int hr= timePicker.getCurrentHour();
        int min=timePicker.getCurrentMinute();
        String des=null;
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

    public void setAlarmText(String s) {
        inst.setAlarmText("Alarm! Time up! Time up!");
    }
    public void sendAlarm(){
        DataBaseHelper myDb=new DataBaseHelper(this);
        try {
            Cursor res = myDb.getAllData();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    int day = 0;
                    try {
                        Calendar calendar=Calendar.getInstance();
                        day=calendar.get(Calendar.DAY_OF_WEEK);
                        Toast.makeText(getApplicationContext(),day+"", Toast.LENGTH_SHORT).show();

                        switch (day){
                            case 7:
                                if(res.getString(9).equals("Monday")){
                                    sendTimeAlarm(res.getString(1),res.getString(3));
                                }
                                break;
                            case 3:
                                if(res.getString(9).equals("Tuesday")){
                                    sendTimeAlarm(res.getString(1),res.getString(3));
                                }
                                break;
                            case 4:
                                if(res.getString(9).equals("Wednesday")){
                                    sendTimeAlarm(res.getString(1),res.getString(3));
                                }
                                break;
                            case 5:
                                if(res.getString(9).equals("Thursday")){
                                    sendTimeAlarm(res.getString(1),res.getString(3));
                                }
                                break;
                            case 6:
                                if(res.getString(9).equals("Friday")){
                                    sendTimeAlarm(res.getString(1),res.getString(3));
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
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void sendTimeAlarm(String time,String subject){
        try {
            int hr=Integer.parseInt(time.split(":")[0]);
            int min=Integer.parseInt(time.split(":")[1].replaceAll("\\D+","").replaceAll("^0+",""));
            TimePicker timePicker=new TimePicker(this);
            int currentHour= timePicker.getCurrentHour();
            int currentMinute=timePicker.getCurrentMinute();
            if(currentHour==hr && currentMinute==min){
                sendAlarmConfrim(hr,min);
                Toast.makeText(getApplicationContext(),hr+" "+min+":"+currentHour+" "+currentMinute,Toast.LENGTH_SHORT).show();
            }

        }catch (NumberFormatException nfe){
            Toast.makeText(getApplicationContext(),nfe.getMessage().toString(),Toast.LENGTH_SHORT).show();

        }
    }

    private void sendAlarmConfrim(int hr,int min) {
        try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hr);
                calendar.set(Calendar.MINUTE, min);
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(getApplicationContext(),hr+"+"+min+" "+calendar.getTimeInMillis(),Toast.LENGTH_SHORT).show();
            }catch (NumberFormatException nfe){
                Toast.makeText(getApplicationContext(),nfe.getMessage().toString(),Toast.LENGTH_SHORT).show();

            }
    }
}