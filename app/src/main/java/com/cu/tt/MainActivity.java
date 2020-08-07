package com.cu.tt;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.LocaleList;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView date,time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}