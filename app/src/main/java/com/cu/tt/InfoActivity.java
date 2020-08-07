package com.cu.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    public TextView time,subject,type,room,teacher,contact,note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        final Intent intent=getIntent();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(intent.getStringExtra("Day"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        time=findViewById(R.id.infotime);
        subject=findViewById(R.id.infosubject);
        type=findViewById(R.id.infotype);
        room=findViewById(R.id.inforoom);
        teacher=findViewById(R.id.infoteacher);
        contact=findViewById(R.id.infocontact);
        note=findViewById(R.id.infonote);

        String starttext=intent.getStringExtra("Start");
        String endtext=intent.getStringExtra("End");
        String subjecttext=intent.getStringExtra("Subject");
        String typetext=intent.getStringExtra("Type");
        String roomtext=intent.getStringExtra("Room");
        String teachertext=intent.getStringExtra("Teacher");
        final String contacttext=intent.getStringExtra("Contact");
        String notetext=intent.getStringExtra("Note");

        time.setText(starttext+"--"+endtext);
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
                startActivity(intent1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}