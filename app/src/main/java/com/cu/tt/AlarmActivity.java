package com.cu.tt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmActivity extends AppCompatActivity {

    Button close;
    TextView textView;
    TimePicker timePicker;
    TextView subject;

    @Override
    protected void onStart() {
        super.onStart();
        Time();
    }
    public void Time(){
        int hr=timePicker.getCurrentHour();
        int min=timePicker.getCurrentMinute();
        String dis=null;
        if(hr>12){
            hr-=12;
            dis=" PM";
        }
        else {
            dis=" AM";
        }
        String minn=min+"";
        if(minn.length()==1) {
            textView.setText(hr + ":0" + min + dis);
        }else {
            textView.setText(hr+":"+min+dis);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        textView=findViewById(R.id.text);
        timePicker=findViewById(R.id.time);
        subject=findViewById(R.id.subject);
        close=findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                MainActivity.instance().finish();
                finish();
            }
        });
    }
}