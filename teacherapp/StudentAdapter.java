package com.example.teacherapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;

    public StudentAdapter(Context context, List<StudentInfo> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    private List<StudentInfo> datalist;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Glide.with(context).load(datalist.get(position).getStud_roll()).into(holder.txt_roll);
//        holder.txt_roll.setText(datalist.get(position).getStud_roll());
//        holder.txt_name.setText(datalist.get(position).getStud_name());
//        holder.txt_attendance.setText("P");

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ScannerActivity.class);
                intent.putExtra("roll",datalist.get(holder.getAdapterPosition()).getStud_roll());
                intent.putExtra("name",datalist.get(holder.getAdapterPosition()).getStud_name());
                intent.putExtra("attendance","P");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{

    TextView txt_roll,txt_name,txt_attendance;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_roll=itemView.findViewById(R.id.txt_roll);
        txt_name=itemView.findViewById(R.id.txt_name);
        txt_attendance=itemView.findViewById(R.id.txt_attendance);
    }
}
