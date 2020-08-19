package com.cu.tt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    public ImageView back;
    public TextInputEditText title,subject;
    public TextView time,date;
    public LinearLayout save;
    public DataBaseHelper helper;
    public TextView action,title_action;
    public ImageView time_icon,date_icon;
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title=findViewById(R.id.title);
        subject=findViewById(R.id.subject);
        time=findViewById(R.id.time);
        date=findViewById(R.id.date);
        time_icon=findViewById(R.id.time_icon);
        date_icon=findViewById(R.id.date_icon);
        time_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTime(time);
            }
        });
        save=findViewById(R.id.save);
        helper=new DataBaseHelper(this);
        date_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               insertDate(date);
            }
        });
        action=findViewById(R.id.action);
        title_action=findViewById(R.id.title_action);
        Intent intent=getIntent();
        String actions=intent.getStringExtra("Note_Action");
        String title_t=intent.getStringExtra("Title");
        String subject_t=intent.getStringExtra("Subject");
        String time_t=intent.getStringExtra("Time");
        String date_t=intent.getStringExtra("Date");
        title_action.setText(actions);
        if(actions.equals("Insert")) {
            action.setText("Insert");
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(title.getText().toString().equals("")|| subject.getText().toString().equals("")|| time.getText().toString().equals("00:00")|| date.getText().toString().equals("00/00/0000")){
                            if(title.getText().toString().equals("")&&subject.getText().toString().equals("")&&time.getText().toString().equals("00:00")&&date.getText().toString().equals("00/00/0000")) {
                                Toast.makeText(getApplicationContext(), "Please Fill Data", Toast.LENGTH_SHORT).show();
                            }else {
                                if(time.getText().toString().equals("00:00")){
                                    Toast.makeText(getApplicationContext(),"Please select time",Toast.LENGTH_SHORT).show();
                                }
                                if(date.getText().toString().equals("00/00/0000")){
                                    Toast.makeText(getApplicationContext(),"Please select date",Toast.LENGTH_SHORT).show();
                                }
                                if(title.getText().toString().equals("")){
                                    Toast.makeText(getApplicationContext(),"Please fill title",Toast.LENGTH_SHORT).show();
                                }
                                if(subject.getText().toString().equals("")){
                                    Toast.makeText(getApplicationContext(),"Please fill subject",Toast.LENGTH_SHORT).show();
                                }
                            }

                        }else {
                            try {
                                boolean i = helper.insertNote(title.getText() + "", subject.getText() + "", time.getText() + "", date.getText() + "");
                                if (i) {
                                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), NoteActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
        }else if(actions.equals("Edit Note")){
            action.setText("Update");
            title.setText(title_t);
            subject.setText(subject_t);
            time.setText(time_t);
            date.setText(date_t);

            final String id=intent.getStringExtra("Id");
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(title.getText().toString().equals("")|| subject.getText().toString().equals("")|| time.getText().toString().equals("00:00")|| date.getText().toString().equals("00/00/0000")){
                        if(time.getText().toString().equals("00:00")){
                            Toast.makeText(getApplicationContext(),"Please select time",Toast.LENGTH_SHORT).show();
                        }
                        if(date.getText().toString().equals("00/00/0000")){
                            Toast.makeText(getApplicationContext(),"Please select date",Toast.LENGTH_SHORT).show();
                        }
                        if(title.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Please fill title",Toast.LENGTH_SHORT).show();
                        }
                        if(subject.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"Please fill subject",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        try {
                            boolean i = helper.updateNote(id, title.getText() + "", subject.getText() + "", time.getText() + "", date.getText() + "");
                            if (i) {
                                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), NoteActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            } else {
                                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        }
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
    public void insertDate(final TextView date){
        Calendar c= Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date.setText(i2+"/"+(i1+1)+"/"+i);
            }
        },year,month,day);
        datePickerDialog.show();
    }

}