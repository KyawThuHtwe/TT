package com.cu.tt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cu.tt.R.layout.votes_layout;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<MyListData> myListData;
    ArrayList<MyVoteData> myVoteData;
    Context context;

    public MyAdapter(ArrayList<MyListData> myListData,ArrayList<MyVoteData> myVoteData,Context context) {
        this.myListData = myListData;
        this.myVoteData=myVoteData;
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

        try{
            holder.subject.setText(myListData.get(position).getSubject());
            holder.start.setText(time(myListData.get(position).getStart()));
            holder.end.setText(time(myListData.get(position).getEnd()));
            if(myVoteData.get(position).getId()==null){
                holder.pid.setText("");
            }else{
                holder.pid.setText(myVoteData.get(position).getId());
            }
        }catch (Exception e){
            //Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
        final String lid=myListData.get(position).getId();
        holder.deletebtnlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String vid=holder.pid.getText().toString();
                    if(vid.equals("")){
                        db.deleteData(lid);
                    }else {
                        boolean i=db.updateVote(vid);
                        if(i){
                            db.updateS(lid);
                        }else {
                            Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                    context.getApplicationContext().startActivity(new Intent(context.getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }catch (Exception e){
                    //Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        ///showhide
        holder.showhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.alldetail.getVisibility()== GONE){
                    holder.alldetail.setVisibility(VISIBLE);
                }else {
                    holder.alldetail.setVisibility(GONE);
                }
            }
        });

        ////Vote
        final Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        try {
            if (myVoteData.get(position).getVote().equals("1") && myVoteData.get(position).getDay().equals(myListData.get(position).getDay())&& myVoteData.get(position).getSsid().equals(myListData.get(position).getId())) {
                int vdate= Integer.parseInt(myVoteData.get(position).getDate().split("/")[0]);
                if(day>0) {
                    if (vdate>0){
                        holder.absent.setVisibility(GONE);
                        holder.present.setVisibility(VISIBLE);
                    }else {
                        holder.present.setVisibility(GONE);
                        holder.absent.setVisibility(VISIBLE);
                    }
                }else if(day>7) {
                    if (vdate>7) {
                        holder.absent.setVisibility(GONE);
                        holder.present.setVisibility(VISIBLE);
                    } else {
                        holder.present.setVisibility(GONE);
                        holder.absent.setVisibility(VISIBLE);
                    }
                }else if(day>14) {
                    if (vdate>14){
                        holder.absent.setVisibility(GONE);
                        holder.present.setVisibility(VISIBLE);
                    }else {
                        holder.present.setVisibility(GONE);
                        holder.absent.setVisibility(VISIBLE);
                    }

                }else if(day>21) {
                    if (vdate>21) {
                        holder.absent.setVisibility(GONE);
                        holder.present.setVisibility(VISIBLE);
                    } else {
                        holder.present.setVisibility(GONE);
                        holder.absent.setVisibility(VISIBLE);
                    }
                }
            }else{
                holder.present.setVisibility(GONE);
                holder.absent.setVisibility(VISIBLE);
            }

           // Toast.makeText(context,myVoteData.get(position).getSsid()+"///"+myListData.get(position).getId(),Toast.LENGTH_SHORT).show();

        }catch (Exception e){
           // Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        ////Roll Call
        holder.rollcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(holder.absent.getVisibility()==VISIBLE){
                    try {
                        AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                        builder.setCancelable(false);
                        builder.setTitle("Present");
                        builder.setMessage("Are you sure you want to Present?");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatePicker datePicker = new DatePicker(context);
                                DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                                boolean result = dataBaseHelper.insertRollcall(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear(), myListData.get(position).getSubject() + "", "1", myListData.get(position).getDay(), myListData.get(position).getId());
                                if (result) {
                                    Intent intent=new Intent(context.getApplicationContext(),MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("PutDay",myListData.get(position).getDay());
                                    context.getApplicationContext().startActivity(intent);
                                    //holder.votes();
                                    //Snackbar snackbar=Snackbar.make(holder.idlayout,"Attendance Present", Snackbar.LENGTH_SHORT);
                                    //snackbar.show();
                                } else {
                                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                                }
                                holder.absent.setVisibility(GONE);
                                holder.present.setVisibility(VISIBLE);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.present.setVisibility(GONE);
                                holder.absent.setVisibility(VISIBLE);
                                //Toast.makeText(context, "Absent", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.create().show();
                    }catch (Exception e){
                        //Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    public String time(String time){
        int hr = 0,min=0;
        String s=null,des=null;
        try {
            hr=Integer.parseInt(time.split(":")[0]);
            min=Integer.parseInt(time.split(":")[1].replaceAll("\\D+","").replaceAll("^0+",""));
            des=null;
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
            Toast.makeText(context,nfe.getMessage().toString(),Toast.LENGTH_SHORT).show();
            if(s==null){
                s="00";
            }
            if (des==null){
                if(hr>12){
                    hr-=12;
                    des=" PM";
                }else {
                    des=" AM";
                }
            }
        }

        return hr+":"+s+des;
    }


    @Override
    public int getItemCount() {
        return myListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout infobtnlayout,editbtnlayout,deletebtnlayout;
        public CardView showinfo;
        public TextView start,end,subject;
        public LinearLayout rollcall;
        public ImageView absent,present;
        public LinearLayout showhide,alldetail;
        public TextView pid;
        public LinearLayout idlayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.start=itemView.findViewById(R.id.rstart);
            this.end=itemView.findViewById(R.id.rend);
            this.subject=itemView.findViewById(R.id.rsubject);
            this.showinfo=itemView.findViewById(R.id.showinfo);
            this.infobtnlayout=itemView.findViewById(R.id.infobtnlayout);
            this.editbtnlayout=itemView.findViewById(R.id.editbtnlayout);
            this.deletebtnlayout=itemView.findViewById(R.id.deletebtnlayout);
            ///RollCall
            this.rollcall=itemView.findViewById(R.id.rollcall);
            this.absent=itemView.findViewById(R.id.absent);
            this.present=itemView.findViewById(R.id.present);
            this.showhide=itemView.findViewById(R.id.showhide);
            this.alldetail=itemView.findViewById(R.id.alldetail);
            this.pid=itemView.findViewById(R.id.pid);
            this.idlayout=itemView.findViewById(R.id.idlayout);
        }
        public void votes(){
            View view=LayoutInflater.from(context).inflate(votes_layout,(ViewGroup)itemView.findViewById(R.id.custom_layout));
            Toast toast=Toast.makeText(context,"",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.setView(view);
            toast.show();
        }
    }
}
