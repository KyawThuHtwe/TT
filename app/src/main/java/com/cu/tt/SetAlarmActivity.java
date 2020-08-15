package com.cu.tt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cu.tt.Alarm.AlarmReceiver;

import java.util.Calendar;

public class SetAlarmActivity extends AppCompatActivity {

    ImageView back;
    ImageView set;
    Switch alarmToggle;
    TextView clock;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        alarmToggle=findViewById(R.id.alarmToggle);
        clock=findViewById(R.id.clock);
        SharedPreferences sp=getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        clock.setText(sp.getString("Time","00:00"));
        alarmToggle.setChecked(sp.getBoolean("Toggle",false));
        set=findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTime();
                if(alarmToggle.isChecked()){
                    alarmToggle.setChecked(false);
                }
            }
        });
        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    try{
                        int hr=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        int day=Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                        if(hr>8 && hr<16 && day>1 &&day <7) {
                            sendAlarm();
                        }else {
                            alarmManager.cancel(pendingIntent);
                        }
                        SharedPreferences sp=getApplicationContext().getSharedPreferences("data",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putBoolean("Toggle",true);
                        editor.commit();
                   }catch (Exception e){
                       Toast.makeText(getApplicationContext(),e.getMessage()+clock.getText(),Toast.LENGTH_SHORT).show();
                   }

                }else {
                    alarmManager.cancel(pendingIntent);
                }

            }
        });
    }
    public void insertTime(){
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                clock.setText(i+":"+i1);
                SharedPreferences sp=getSharedPreferences("data",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("Time",clock.getText().toString());
                editor.commit();
            }
        },0,0,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void sendAlarm(){
        TimePicker timePicker=new TimePicker(this);
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
        c.set(Calendar.MINUTE,timePicker.getCurrentMinute());
        String timetext=clock.getText().toString();
        int hr= Integer.parseInt(timetext.split(":")[0]);
        int min= Integer.parseInt(timetext.split(":")[1]);
        int ans=(hr*60*60*1000)+(min*60*1000);
        Intent intent=new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),ans,pendingIntent);
        setBootReceiverEnabled(PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
    }
    public void setBootReceiverEnabled(int componentEnabledState){
        ComponentName componentName=new ComponentName(this,AlarmReceiver.class);
        PackageManager packageManager=getPackageManager();
        packageManager.setComponentEnabledSetting(componentName,componentEnabledState,PackageManager.DONT_KILL_APP);
    }
}