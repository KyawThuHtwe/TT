package com.cu.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static android.view.View.VISIBLE;

public class NoteActivity extends AppCompatActivity {

    RecyclerView today_recyclerView,tomorrow_recyclerView,other_day_recyclerView;
    ArrayList<NoteListData> other_day_noteListData,tomorrow_noteListData,today_noteListData;
    ImageView addNote;
    CardView table;
    TextView today,tomorrow,other_day;
    NestedScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        today=findViewById(R.id.today);
        tomorrow=findViewById(R.id.tomorrow);
        other_day=findViewById(R.id.other_day);
        scrollView=findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY>0 && table.getVisibility()== VISIBLE){
                    table.setVisibility(View.GONE);
                }else if(scrollY==0 && table.getVisibility()!= VISIBLE){
                    table.setVisibility(View.VISIBLE);
                }
            }
        });
        table=findViewById(R.id.table);
        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        addNote=findViewById(R.id.addNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddNoteActivity.class);
                intent.putExtra("Note_Action","Insert");
                startActivity(intent);
            }
        });
        try {
            today_recyclerView = findViewById(R.id.today_recyclerView);
            today_recyclerView.setHasFixedSize(true);
            tomorrow_recyclerView = findViewById(R.id.tomorrow_recyclerView);
            tomorrow_recyclerView.setHasFixedSize(true);
            other_day_recyclerView = findViewById(R.id.other_day_recyclerView);
            other_day_recyclerView.setHasFixedSize(true);

            LinearLayoutManager manager_today = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            today_recyclerView.setLayoutManager(manager_today);
            LinearLayoutManager manager_other_day = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
            other_day_recyclerView.setLayoutManager(manager_other_day);
            LinearLayoutManager manager_tomorrow = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
            tomorrow_recyclerView.setLayoutManager(manager_tomorrow);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
        ///notes
        noteLoad();


    }

    private void noteLoad() {
        DataBaseHelper helper=new DataBaseHelper(this);
        other_day_noteListData=new ArrayList<>();
        today_noteListData=new ArrayList<>();
        tomorrow_noteListData=new ArrayList<>();
        tomorrow_noteListData.clear();
        other_day_noteListData.clear();
        today_noteListData.clear();
        String time;
        try {
            Cursor res = helper.getNote();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    time=res.getString(4);
                    Calendar c= Calendar.getInstance();
                    int year=c.get(Calendar.YEAR);
                    int month=c.get(Calendar.MONTH)+1;
                    int day=c.get(Calendar.DAY_OF_MONTH);
                    int s_year=Integer.parseInt(time.split("/")[2]);
                    int s_month=Integer.parseInt(time.split("/")[1]);
                    int s_day=Integer.parseInt(time.split("/")[0]);
                    if(s_day==day && s_month==month && s_year==year) {
                        today.setVisibility(VISIBLE);
                        today_noteListData.add(new NoteListData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
                    }else if(s_day>day && s_month==month && s_year==year){
                        int left_day=s_day-day;
                        if(left_day==1){
                            tomorrow.setVisibility(VISIBLE);
                            tomorrow_noteListData.add(new NoteListData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
                        }else {
                            other_day.setVisibility(VISIBLE);
                            other_day_noteListData.add(new NoteListData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
                        }
                    }else if(s_day>day && s_month>=month && s_year>=year) {
                        other_day.setVisibility(VISIBLE);
                        other_day_noteListData.add(new NoteListData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
                    }else if(s_day<day && s_month<=month && s_year<=year) {
                       /* other_day.setVisibility(View.VISIBLE);
                        today.setVisibility(View.VISIBLE);
                        tomorrow.setVisibility(View.VISIBLE);
                        */
                    }else {
                        other_day.setVisibility(View.GONE);
                        today.setVisibility(View.GONE);
                        tomorrow.setVisibility(View.GONE);
                    }
                }
            }
            today_recyclerView.setAdapter(new NoteAdapter(today_noteListData,getApplicationContext()));
            tomorrow_recyclerView.setAdapter(new NoteAdapter(tomorrow_noteListData,getApplicationContext()));
            other_day_recyclerView.setAdapter(new NoteAdapter(other_day_noteListData,getApplicationContext()));
            helper.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}