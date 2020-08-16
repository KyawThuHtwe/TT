package com.cu.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class WeeklyReportActivity extends AppCompatActivity {

    ImageView back;
    RecyclerView recyclerView;
    public Spinner spinner;
    String[] week={"1st week","2nd week","3rd week","4th week"};
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
        setContentView(R.layout.activity_weekly_report);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        spinner=findViewById(R.id.spinner);
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,week);
        spinner.setAdapter(arrayAdapter);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        dataBaseHelper=new DataBaseHelper(this);
        calculateRollCalls=new ArrayList<>();

        Calendar calendar=Calendar.getInstance();
        int current_week=calendar.get(Calendar.DAY_OF_MONTH);
        if(current_week<8){
            spinner.setSelection(0);
        }else if(current_week < 15){
            spinner.setSelection(1);
        }else if(current_week < 22){
            spinner.setSelection(2);
        }else {
            spinner.setSelection(3);
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
    public void dataLoad(int selectweek){
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

            recyclerView.setAdapter(new RollCallAdaper(calculateRollCalls,selectweek));
            dataBaseHelper.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    private class RollCallAdaper extends RecyclerView.Adapter<RollCallAdaper.ViewHolder> {
        ArrayList<CalculateRollCall> calculateRollCalls;
        int selectweek;
        public RollCallAdaper(ArrayList<CalculateRollCall> calculateRollCalls,int selectweek) {
            this.calculateRollCalls=calculateRollCalls;
            this.selectweek=selectweek;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
            View view=layoutInflater.inflate(R.layout.rollcall_layout,parent,false);
            return new ViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.subject.setText(calculateRollCalls.get(position).getSubject());
            if(holder.percent.getText()!=null){
                holder.percent.setText("");
            }
            double total=0;
            double count=0;
            DataBaseHelper myDb=new DataBaseHelper(getApplicationContext());
            try {
                Cursor resc = myDb.getVote();
                if (resc != null && resc.getCount() > 0) {
                    while (resc.moveToNext()) {
                        if(resc.getString(2).equals(calculateRollCalls.get(position).getSubject()) && resc.getString(3).equals("1")){
                            try {
                                total=calculateRollCalls.get(position).getTotal();
                                int day=Integer.parseInt(resc.getString(1).split("/")[0]);
                                if(selectweek==0){
                                    if(day<8){
                                        count++;
                                    }
                                }else if(selectweek==1){
                                    if(day>7 && day<15){
                                        count++;
                                    }
                                }else if(selectweek==2){
                                    if(day>14 && day<22){
                                        count++;
                                    }
                                }else if(selectweek==3){
                                    if(day>21){
                                        count++;
                                    }
                                }
                            }catch (NumberFormatException nfe){
                                Toast.makeText(getApplicationContext(), nfe.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                double ans;
                if(count>0 && total>0) {
                    ans=(count/total)*100.0;
                }else {
                    ans=0;
                }
                String s=ans+"";
                String fs;
                if(s.length()>4){
                    fs=s.charAt(0)+""+s.charAt(1)+""+s.charAt(2)+""+s.charAt(3);
                }else {
                    fs=s;
                }
                holder.percent.setText(fs+"%");
                holder.progressBar.setProgress((int) ans);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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