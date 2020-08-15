package com.cu.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class RollCallActivity extends AppCompatActivity {

    ImageView back;
    RecyclerView recyclerView;
    public Spinner spinner;
    String[] month={"January","February","March","April","May","June","July","August","September","October","November","December"};
    ArrayAdapter<String> arrayAdapter;
    ArrayList<CalculateRollCall> calculateRollCalls;
    DataBaseHelper dataBaseHelper;
    int total=0;
    String subject;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        spinner=findViewById(R.id.spinner);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,month);
        spinner.setAdapter(arrayAdapter);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        dataBaseHelper=new DataBaseHelper(this);
        calculateRollCalls=new ArrayList<>();

        Calendar calendar=Calendar.getInstance();
        int currentmonth=calendar.get(Calendar.MONTH);

        switch (currentmonth){
            case 1:
                spinner.setSelection(1);
                break;
            case 2:
                spinner.setSelection(2);
                break;
            case 3:
                spinner.setSelection(3);
                break;
            case 4:
                spinner.setSelection(4);
                break;
            case 5:
                spinner.setSelection(5);
                break;
            case 6:
                spinner.setSelection(6);
                break;
            case 7:
                spinner.setSelection(7);
                break;
            case 8:
                spinner.setSelection(8);
                break;
            case 9:
                spinner.setSelection(9);
                break;
            case 10:
                spinner.setSelection(10);
                break;
            case 11:
                spinner.setSelection(11);
                break;
            case 12:
                spinner.setSelection(12);
                break;
        }
        dataLoad(spinner.getSelectedItemPosition());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dataLoad(spinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    public void dataLoad(int selectmonth){
           calculateRollCalls.clear();
        try {
            Cursor res = dataBaseHelper.selectTotalCount();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    total=Integer.parseInt(res.getString(1));
                    subject=res.getString(0);
                    calculateRollCalls.add(new CalculateRollCall(total,subject));
                    //Toast.makeText(getApplicationContext(),total+subject,Toast.LENGTH_SHORT).show();
                }
            }

            recyclerView.setAdapter(new RollCallAdaper(calculateRollCalls,selectmonth));
            dataBaseHelper.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

    }

   private class RollCallAdaper extends RecyclerView.Adapter<RollCallAdaper.ViewHolder> {
        ArrayList<CalculateRollCall> calculateRollCalls;
        int selectmonth;
        public RollCallAdaper(ArrayList<CalculateRollCall> calculateRollCalls,int selectmonth) {
            this.calculateRollCalls=calculateRollCalls;
            this.selectmonth=selectmonth;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View view=layoutInflater.inflate(R.layout.rollcall_layout,parent,false);
            ViewHolder viewHolder=new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.subject.setText(calculateRollCalls.get(position).getSubject());
            if(holder.percent.getText()!=null){
                holder.percent.setText("");
            }
            double total=0;
            double count=0;
            if(count>0){
                count=0;
            }
            DataBaseHelper myDb=new DataBaseHelper(getApplicationContext());
            try {
                Cursor resc = myDb.getVote();
                if (resc != null && resc.getCount() > 0) {
                    while (resc.moveToNext()) {
                        if(resc.getString(2).equals(calculateRollCalls.get(position).getSubject()) && resc.getString(3).equals("1")){
                            try {
                                total=calculateRollCalls.get(position).getTotal();
                                int month=Integer.parseInt(resc.getString(1).split("/")[1]);
                                if(selectmonth==month){
                                    count++;
                                }
                            }catch (NumberFormatException nfe){
                                Toast.makeText(getApplicationContext(), nfe.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                double ans=0;
                if(count>0 && total>0) {
                    ans=(count/(total*4)*100.0);
                   // Toast.makeText(getApplicationContext(),ans+"",Toast.LENGTH_SHORT).show();
                }else {
                    ans=0;
                }
                String s=ans+"";
                String fs=null;
                if(s.length()>4){
                    fs=s.charAt(0)+""+s.charAt(1)+""+s.charAt(2)+""+s.charAt(3);
                }else {
                    fs=s;
                }
                holder.percent.setText(fs+"%");
                holder.progressBar.setProgress((int) ans);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return calculateRollCalls.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView subject;
            public ProgressBar progressBar;
            public TextView percent;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.subject=itemView.findViewById(R.id.subject);
                this.progressBar=itemView.findViewById(R.id.circle_progress_bar);
                this.percent=itemView.findViewById(R.id.percent);
            }
        }
    }

}