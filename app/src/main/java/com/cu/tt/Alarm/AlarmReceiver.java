package com.cu.tt.Alarm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.cu.tt.AlarmActivity;
import com.cu.tt.MainActivity;
import androidx.legacy.content.WakefulBroadcastReceiver;

import static android.content.Intent.getIntent;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //this will update the UI with message
        MainActivity inst = MainActivity.instance();
        //inst.setAlarmText("Alarm! Time up! Time up!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
        if(ringtone.isPlaying()){
            Intent intent1=new Intent(MainActivity.instance().getApplicationContext(), AlarmActivity.class);
            MainActivity.instance().startActivity(intent1);
        }

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

    }
}
