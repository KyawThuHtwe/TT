package com.cu.tt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    public TextView start,end,subject,type,room,teacher,contact,note,day;
    LinearLayout close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        final Intent intent=getIntent();
        close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        start=findViewById(R.id.start);
        end=findViewById(R.id.end);
        subject=findViewById(R.id.infosubject);
        type=findViewById(R.id.infotype);
        room=findViewById(R.id.inforoom);
        teacher=findViewById(R.id.infoteacher);
        contact=findViewById(R.id.infocontact);
        note=findViewById(R.id.infonote);
        day=findViewById(R.id.day);
        String day_text=intent.getStringExtra("Day");
        String start_text=intent.getStringExtra("Start");
        String end_text=intent.getStringExtra("End");
        String subject_text=intent.getStringExtra("Subject");
        String type_text=intent.getStringExtra("Type");
        String room_text=intent.getStringExtra("Room");
        String teacher_text=intent.getStringExtra("Teacher");
        final String contact_text=intent.getStringExtra("Contact");
        String note_text=intent.getStringExtra("Note");
        day.setText(day_text);
        assert start_text != null;
        start.setText(time(start_text));
        assert end_text != null;
        end.setText(time(end_text));
        subject.setText(subject_text);
        type.setText(type_text);
        room.setText(room_text);
        teacher.setText(teacher_text);
        contact.setText(contact_text);
        note.setText(note_text);
        ImageButton call=findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Intent.ACTION_CALL);
                intent1.setData(Uri.parse("tel:"+contact_text));
                if(ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Please grant the permission to call",Toast.LENGTH_SHORT).show();
                    requestPermission();
                }else {
                    startActivity(intent1);
                }
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
    }

    public String time(String time){
        int hr = 0,min;
        String s=null,des;
        try {
            hr=Integer.parseInt(time.split(":")[0]);
            min=Integer.parseInt(time.split(":")[1].replaceAll("\\D+","").replaceAll("^0+",""));
            s=min+"";
            if(s.length()==1){
                s="0"+s;
            }
            if(hr>12){
                hr-=12;
                des=" PM";
            }else {
                des=" AM";
            }
        }catch (NumberFormatException nfe){
            Toast.makeText(getApplicationContext(), nfe.getMessage(),Toast.LENGTH_SHORT).show();
            if(s==null){
                s="00";
            }
            if(hr>12){
                hr-=12;
                des=" PM";
            }else {
                des=" AM";
            }
        }
        return hr+":"+s+des;
    }

}