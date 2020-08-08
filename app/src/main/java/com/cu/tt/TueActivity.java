package com.cu.tt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TueActivity extends AppCompatActivity {

    DataBaseHelper myDb;
    RecyclerView recyclerView;
    ArrayList<MyListData> myListData;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tue);
        myDb=new DataBaseHelper(this);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddActivity.class);
                intent.putExtra("Day","Tuesday");
                startActivity(intent);
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        dataLoad();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataLoad();
    }

    public void dataLoad(){
        ArrayList<MyListData> myListData = new ArrayList<>();
        try {
            Cursor res = myDb.getAllData();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    if(res.getString(9).equals("Tuesday")){
                        myListData.add(new MyListData(res.getString(0),res.getString(1),res.getString(2),res.getString(3),
                                res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9)));
                    }
                }
            }
            recyclerView.setAdapter(new MyAdapter(myListData,getApplicationContext()));
            myDb.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

}