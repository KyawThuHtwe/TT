package com.cu.tt;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    DataBaseHelper myDb;
    public TextView start,end,startview,endview;
    public TextInputEditText subject,type,room,teacher,contact,note;
    public String title,id;
    public TextView day;
    public LinearLayout update;
    public ImageView back;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        myDb=new DataBaseHelper(this);
        Intent intent=getIntent();
        title=intent.getStringExtra("Day");
        day=findViewById(R.id.day);
        day.setText(title);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        startview=findViewById(R.id.startview);
        endview=findViewById(R.id.endview);
        subject=findViewById(R.id.subject);
        type=findViewById(R.id.type);
        room=findViewById(R.id.room);
        teacher=findViewById(R.id.teacher);
        contact=findViewById(R.id.contact);
        note=findViewById(R.id.note);

        update=findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                id=intent.getStringExtra("Id");
                boolean result=myDb.updateData(id+"",startview.getText()+"",endview.getText()+"",
                        subject.getText()+"",type.getText()+"",room.getText()+"",
                        teacher.getText()+"",contact.getText()+"",note.getText()+"");
                if(result){
                    Toast.makeText(getApplicationContext(),"Update successful",Toast.LENGTH_SHORT).show();
                    Intent main=new Intent(getApplicationContext(),MainActivity.class);
                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.putExtra("PutDay",day.getText().toString());
                    main.putExtra("Anim","off");
                    startActivity(main);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        subject.setEnabled(false);
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                time.setText(i+":"+i1);
            }
        },hr,min,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}