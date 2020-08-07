package com.cu.tt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<MyListData> myListData;
    Context context;

    public MyAdapter(ArrayList<MyListData> myListData,Context context) {
        this.myListData = myListData;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.custom_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.subject.setText(myListData.get(position).getSubject());
        holder.start.setText(myListData.get(position).getStart());
        holder.end.setText(myListData.get(position).getEnd());
        /////
        final Intent infointent = new Intent(context.getApplicationContext(), InfoActivity.class);
        infointent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        infointent.putExtra("Start",myListData.get(position).getStart());
        infointent.putExtra("End",myListData.get(position).getEnd());
        infointent.putExtra("Subject",myListData.get(position).getSubject());
        infointent.putExtra("Type",myListData.get(position).getType());
        infointent.putExtra("Room",myListData.get(position).getRoom());
        infointent.putExtra("Teacher",myListData.get(position).getTeacher());
        infointent.putExtra("Contact",myListData.get(position).getContact());
        infointent.putExtra("Note",myListData.get(position).getNotes());
        infointent.putExtra("Day",myListData.get(position).getDay());

        holder.infobtnlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.getApplicationContext().startActivity(infointent);
            }
        });
        /////
        final Intent editintent = new Intent(context.getApplicationContext(), UpdateActivity.class);
        editintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        editintent.putExtra("Id",myListData.get(position).getId());
        editintent.putExtra("Start",myListData.get(position).getStart());
        editintent.putExtra("End",myListData.get(position).getEnd());
        editintent.putExtra("Subject",myListData.get(position).getSubject());
        editintent.putExtra("Type",myListData.get(position).getType());
        editintent.putExtra("Room",myListData.get(position).getRoom());
        editintent.putExtra("Teacher",myListData.get(position).getTeacher());
        editintent.putExtra("Contact",myListData.get(position).getContact());
        editintent.putExtra("Note",myListData.get(position).getNotes());
        editintent.putExtra("Day",myListData.get(position).getDay());

        holder.editbtnlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.getApplicationContext().startActivity(editintent);
            }
        });

        final DataBaseHelper db=new DataBaseHelper(context);
        final String id=myListData.get(position).getId();
        holder.deletebtnlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    db.deleteData(id);
                    context.startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }catch (Exception e){
                    Toast.makeText(context,e.getMessage().toString()+id,Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return myListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout infobtnlayout,editbtnlayout,deletebtnlayout;
        public CardView showinfo;
        public TextView start,end,subject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.start=itemView.findViewById(R.id.rstart);
            this.end=itemView.findViewById(R.id.rend);
            this.subject=itemView.findViewById(R.id.rsubject);
            this.showinfo=itemView.findViewById(R.id.showinfo);
            this.infobtnlayout=itemView.findViewById(R.id.infobtnlayout);
            this.editbtnlayout=itemView.findViewById(R.id.editbtnlayout);
            this.deletebtnlayout=itemView.findViewById(R.id.deletebtnlayout);
        }
    }
}
