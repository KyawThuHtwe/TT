package com.cu.tt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    ArrayList<NoteListData> noteListData;
    Context context;
    public NoteAdapter(ArrayList<NoteListData> noteListData, Context context) {
        this.noteListData=noteListData;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.custom_note_layout,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.type.setText(noteListData.get(position).getTitle());
        holder.subject.setText(noteListData.get(position).getSubject());
        holder.time.setText(time(noteListData.get(position).getTime()));
        holder.date.setText(noteListData.get(position).getDate());
        String date_time=noteListData.get(position).getDate();
        Calendar c= Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH)+1;
        int day=c.get(Calendar.DAY_OF_MONTH);
        int s_year=Integer.parseInt(date_time.split("/")[2]);
        int s_month=Integer.parseInt(date_time.split("/")[1]);
        int s_day=Integer.parseInt(date_time.split("/")[0]);


        if(s_day==day && s_month==month && s_year==year) {
            holder.day_left.setText("Today");
        }else if(s_day>day && s_month==month && s_year==year){
            int left_day=s_day-day;
            if(left_day==1){
                holder.day_left.setText("Tomorrow");
            }else {
                holder.day_left.setText(left_day+" day left");
            }
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), AddNoteActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Note_Action", "Edit Note");
                    intent.putExtra("Id", noteListData.get(position).getId());
                    intent.putExtra("Title",holder.type.getText().toString());
                    intent.putExtra("Subject",holder.subject.getText().toString());
                    intent.putExtra("Time",holder.time.getText().toString());
                    intent.putExtra("Date",holder.date.getText().toString());
                    context.getApplicationContext().startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Animation animation= AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.fade);
        holder.show_hide.setAnimation(animation);
        holder.show_hide.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                try {
                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                    builder.setCancelable(false);
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure you want to Delete?");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                            int res=dataBaseHelper.deleteNote(noteListData.get(position).getId());
                            if(res==1){
                                Intent intent=new Intent(context.getApplicationContext(),NoteActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.getApplicationContext().startActivity(intent);
                            }else {
                                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();
                }catch (Exception e){
                    //Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;
            }
        });
    }
    public String time(String time){
        int hr = 0,min;
        String s=null,des;
        try {
            hr=Integer.parseInt(time.split(":")[0]);
            min=Integer.parseInt(time.split(":")[1].replaceAll("\\D+","").replaceAll("^0+",""));
            s=min+"";
            if(s.length()==1){
                s="0"+s;
            }
            if(hr>12){
                hr-=12;
                des=" PM";
            }else {
                des=" AM";
            }

        }catch (NumberFormatException nfe){
            Toast.makeText(context,nfe.getMessage(),Toast.LENGTH_SHORT).show();
            if(s==null){
                s="00";
            }
            if(hr>12){
                hr-=12;
                des=" PM";
            }else {
                des=" AM";
            }
        }

        return hr+":"+s+des;
    }

    @Override
    public int getItemCount() {
        return noteListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView type,date,subject,time,day_left;
        public LinearLayout show_hide;
        public ImageView edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.type=itemView.findViewById(R.id.type);
            this.date=itemView.findViewById(R.id.date);
            this.subject=itemView.findViewById(R.id.subject);
            this.time=itemView.findViewById(R.id.time);
            this.edit=itemView.findViewById(R.id.edit);
            this.show_hide=itemView.findViewById(R.id.show_hide);
            this.day_left=itemView.findViewById(R.id.day_left);
        }
    }
}
