package com.cu.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class FriActivity extends AppCompatActivity {

    DataBaseHelper myDb;
    RecyclerView recyclerView;
    ArrayList<MyListData> myListData;
    ArrayList<MyVoteData> myVoteData;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fri_actvity);
        myDb=new DataBaseHelper(this);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddActivity.class);
                intent.putExtra("Day","Friday");
                startActivity(intent);
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        dataLoad();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0 && floatingActionButton.getVisibility()== VISIBLE){
                    floatingActionButton.hide();
                }else if(dy<0 && floatingActionButton.getVisibility()!= VISIBLE){
                    floatingActionButton.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataLoad();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void dataLoad(){
        myListData = new ArrayList<>();
        try {
            Cursor res = myDb.getAllData();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    if(res.getString(9).equals("Friday")){
                        if (!res.getString(3).equals("Subject")) {
                            myListData.add(new MyListData(res.getString(0), res.getString(1), res.getString(2), res.getString(3),
                                    res.getString(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9)));
                        }
                    }
                }
            }
            Vote();
            recyclerView.setAdapter(new MyAdapter(myListData,myVoteData,getApplicationContext()));
            myDb.close();

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void Vote() {
        DataBaseHelper myDb = new DataBaseHelper(this);
        myVoteData = new ArrayList<>();
        try {
            Cursor res = myDb.getVote();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    if (res.getString(4).equals("Friday")) {
                        myVoteData.add(new MyVoteData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5)));
                    }
                }
            }
            myDb.close();

        } catch (NullPointerException e) {
            //Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}