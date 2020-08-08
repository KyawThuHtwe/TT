package com.cu.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        String daytext=intent.getStringExtra("Day");
        String starttext=intent.getStringExtra("Start");
        String endtext=intent.getStringExtra("End");
        String subjecttext=intent.getStringExtra("Subject");
        String typetext=intent.getStringExtra("Type");
        String roomtext=intent.getStringExtra("Room");
        String teachertext=intent.getStringExtra("Teacher");
        final String contacttext=intent.getStringExtra("Contact");
        String notetext=intent.getStringExtra("Note");
        day.setText(daytext);
        start.setText(starttext);
        end.setText(endtext);
        subject.setText(subjecttext);
        type.setText(typetext);
        room.setText(roomtext);
        teacher.setText(teachertext);
        contact.setText(contacttext);
        note.setText(notetext);
        ImageButton call=findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Intent.ACTION_CALL);
                intent1.setData(Uri.parse("tel:"+contacttext));
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

}