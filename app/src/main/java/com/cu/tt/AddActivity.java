package com.cu.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    DataBaseHelper myDb;
    public TextView start,end,startview,endview;
    public TextInputEditText subject,type,room,teacher,contact,note;
    public String title,id;
    public String edit="insert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        myDb=new DataBaseHelper(this);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        title=intent.getStringExtra("Day");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startview=findViewById(R.id.startview);
        endview=findViewById(R.id.endview);
        subject=findViewById(R.id.subject);
        type=findViewById(R.id.type);
        room=findViewById(R.id.room);
        teacher=findViewById(R.id.teacher);
        contact=findViewById(R.id.contact);
        note=findViewById(R.id.note);

        String starttext=intent.getStringExtra("Start");
        String endtext=intent.getStringExtra("End");
        String subjecttext=intent.getStringExtra("Subject");
        String typetext=intent.getStringExtra("Type");
        String roomtext=intent.getStringExtra("Room");
        String teachertext=intent.getStringExtra("Teacher");
        String contacttext=intent.getStringExtra("Contact");
        String notetext=intent.getStringExtra("Note");


        startview.setText(starttext);
        endview.setText(endtext);
        subject.setText(subjecttext);
        type.setText(typetext);
        room.setText(roomtext);
        teacher.setText(teachertext);
        contact.setText(contacttext);
        note.setText(notetext);
        if(startview.getText().equals("")){
            startview.setText("00:00");
        }
        if(endview.getText().equals("")){
            endview.setText("00:00");
        }

        start=findViewById(R.id.start);
        end=findViewById(R.id.end);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTime(startview);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTime(endview);
            }
        });

    }
    public void insertTime(final TextView time){
        Calendar c= Calendar.getInstance();
        int hr=c.get(Calendar.HOUR_OF_DAY);
        int min=c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                time.setText(i+":"+i1);
            }
        },hr,min,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
            case R.id.save:
                Intent intent=getIntent();
                title=intent.getStringExtra("Day");

                    insertDatabase(startview.getText().toString(),endview.getText().toString(),
                            subject.getText().toString(),type.getText().toString(),
                            room.getText().toString(),teacher.getText().toString(),
                            contact.getText().toString(),note.getText().toString(),title);

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean insertDatabase(String from, String to,String subject,String type,String room,String teacher,String contact,String note,String day){
        boolean result=myDb.insertData(from,to,subject,type,room,teacher,contact,note,day);
        if(result==true){
            Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show();
            Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            finish();
        }else {
            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show();
        }
        return result;
    }
}