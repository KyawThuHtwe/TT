package com.cu.tt;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.LocalActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cu.tt.Alarm.AlarmReceiver;

import java.text.DateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private TextView date,time;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private LinearLayout roll_call_btn;
    public TextView text;
    ImageView setting;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp=getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        boolean res=sp.getBoolean("Toggle",false);
        if(res){
            sendAlarm();
        }
    }
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnable,6000);
            dat();
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setBootReceiverEnabled(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        finish();
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setting=findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SettingAcivity.class));
            }
        });
        text=findViewById(R.id.text);
        roll_call_btn=findViewById(R.id.rollcallbtn);
        roll_call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RollCallActivity.class));
            }
        });
        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        try {
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
            spec.setContent(new Intent(getApplicationContext(), FriActivity.class));
            tabHost.addTab(spec);

            ////
            try {
                Intent getday=getIntent();
                String title=getday.getStringExtra("PutDay");
                if(title!=null) {
                    switch (title) {
                        case "Monday":
                            tabHost.setCurrentTab(0);
                            break;
                        case "Tuesday":
                            tabHost.setCurrentTab(1);
                            break;
                        case "Wednesday":
                            tabHost.setCurrentTab(2);
                            break;
                        case "Thursday":
                            tabHost.setCurrentTab(3);
                            break;
                        case "Friday":
                            tabHost.setCurrentTab(4);
                            break;
                    }
                }else {
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
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        DatePicker datePicker=new DatePicker(this);
        if(datePicker.getDayOfMonth()<=7){
            text.setText("1st week");
        }else if(datePicker.getDayOfMonth()<=14){
            text.setText("2nd week");
        }else if(datePicker.getDayOfMonth()<=21){
            text.setText("3rd week");
        }else if(datePicker.getDayOfMonth()<=28){
            text.setText("4th week");
        }
        handler.post(runnable);
        //sendAlarm();
        SharedPreferences sp=getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        boolean res=sp.getBoolean("Toggle",false);
        if(res){
            sendAlarm();
        }
        ///
        DataBaseHelper db=new DataBaseHelper(this);
        try {
            Cursor resc = db.getVote();
            if (resc != null && resc.getCount() > 0) {
                while (resc.moveToNext()) {
                    if(resc.getString(3).equals("0")){
                        String id=resc.getString(0);
                        int i=db.deleteVote(id);
                        if(i==1){
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        ////
        try {
            Cursor r = db.getAllData();
            if (r != null && r.getCount() > 0) {
                while (r.moveToNext()) {
                    if(r.getString(3).equals("Subject")){
                        String id=r.getString(0);
                        int i=db.deleteData(id);
                        if(i==1){
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("SetTextI18n")
    public void dat(){
        Calendar calendar=Calendar.getInstance();
        String curdate= DateFormat.getDateInstance().format(calendar.getTime());
        date.setText(curdate);
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
/*
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
                            case 2:
                                if(res.getString(9).equals("Monday")){
                                    sendTimeAlarm(res.getString(1));
                                }
                                break;
                            case 3:
                                if(res.getString(9).equals("Tuesday")){
                                    sendTimeAlarm(res.getString(1));
                                }
                                break;
                            case 4:
                                if(res.getString(9).equals("Wednesday")){
                                    sendTimeAlarm(res.getString(1));
                                }
                                break;
                            case 5:
                                if(res.getString(9).equals("Thursday")){
                                    sendTimeAlarm(res.getString(1));
                                }
                                break;
                            case 6:
                                if(res.getString(9).equals("Friday")){
                                    sendTimeAlarm(res.getString(1));
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

    public void sendTimeAlarm(String time){
        try {
            int hr=Integer.parseInt(time.split(":")[0]);
            int min=Integer.parseInt(time.split(":")[1].replaceAll("\\D+","").replaceAll("^0+",""));
            TimePicker timePicker=new TimePicker(this);
            int currentHour= timePicker.getCurrentHour();
            int currentMinute=timePicker.getCurrentMinute();
            if(currentHour==hr && currentMinute==min){
                sendAlarmConfrim(hr,min);
            }
            Toast.makeText(getApplicationContext(),currentHour+"="+hr+"/"+currentMinute+"="+min,Toast.LENGTH_SHORT).show();

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
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                setBootReceiverEnabled(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
                Toast.makeText(getApplicationContext(),System.currentTimeMillis()+":"+calendar.getTime(),Toast.LENGTH_SHORT).show();

        }catch (NumberFormatException nfe){
                Toast.makeText(getApplicationContext(),nfe.getMessage().toString(),Toast.LENGTH_SHORT).show();

            }
    }

 */
    public void sendAlarm(){
        SharedPreferences sp=getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        TimePicker timePicker=new TimePicker(this);
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
        c.set(Calendar.MINUTE,timePicker.getCurrentMinute());
        String timetext=sp.getString("Time","0:1");
        assert timetext != null;
        int hr= Integer.parseInt(timetext.split(":")[0]);
        int min= Integer.parseInt(timetext.split(":")[1]);
        int ans=(hr*60*60*1000)+(min*60*1000);
        Intent intent=new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),ans,pendingIntent);
        setBootReceiverEnabled(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);

       // Toast.makeText(getApplicationContext(),ans+"",Toast.LENGTH_SHORT).show();
    }

    public void setBootReceiverEnabled(int componentEnabledState){
        ComponentName componentName=new ComponentName(this,AlarmReceiver.class);
        PackageManager packageManager=getPackageManager();
        packageManager.setComponentEnabledSetting(componentName,componentEnabledState,PackageManager.DONT_KILL_APP);
    }

}