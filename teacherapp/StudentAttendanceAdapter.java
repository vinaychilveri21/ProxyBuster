package com.example.teacherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StudentAttendanceAdapter extends BaseAdapter {
    Context context;
    public StudentAttendanceAdapter(Context context){
        this.context= context;
    }
    @Override
    public int getCount() {
        return StudentInfo.collection.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= LayoutInflater.from(context);
        view= inflater.inflate(R.layout.student_list_view,null);
        TextView txt_rollno,txt_name, txt_attendance;
        txt_rollno= view.findViewById(R.id.txt_roll);
        txt_name= view.findViewById(R.id.txt_name);
        txt_attendance= view.findViewById(R.id.txt_attendance);
        txt_rollno.setText(StudentInfo.collection.get(i).getStud_roll());
        txt_name.setText(StudentInfo.collection.get(i).getStud_name());
        txt_attendance.setText(StudentInfo.collection.get(i).getAttendance());
        return view;
    }
}
