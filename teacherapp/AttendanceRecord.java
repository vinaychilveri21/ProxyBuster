package com.example.teacherapp;

import java.util.ArrayList;

public class AttendanceRecord {

    private String stud_prn_no;
    private String stud_attendance_date;
    private String stud_roll;
    private String stud_name;
    private String stud_attendance_status;

    public String getStud_attendance_time() {
        return stud_attendance_time;
    }

    public void setStud_attendance_time(String stud_attendance_time) {
        this.stud_attendance_time = stud_attendance_time;
    }

    private String stud_attendance_time;

    public AttendanceRecord(String stud_prn_no, String stud_attendance_date, String stud_roll, String stud_name, String stud_attendance_status, String stud_attendance_time) {
        this.stud_prn_no = stud_prn_no;
        this.stud_attendance_date = stud_attendance_date;
        this.stud_roll = stud_roll;
        this.stud_name = stud_name;
        this.stud_attendance_status = stud_attendance_status;
        this.stud_attendance_time=stud_attendance_time;
    }

    public AttendanceRecord() {

    }
    public String getStud_roll() {
        return stud_roll;
    }

    public void setStud_roll(String stud_roll) {
        this.stud_roll = stud_roll;
    }

    public String getStud_name() {
        return stud_name;
    }

    public void setStud_name(String stud_name) {
        this.stud_name = stud_name;
    }

    public String getStud_attendance_status() {
        return stud_attendance_status;
    }

    public void setStud_attendance_status(String stud_attendance_status) {
        this.stud_attendance_status = stud_attendance_status;
    }

    public String getStud_prn_no() {
        return stud_prn_no;
    }

    public void setStud_prn_no(String stud_prn_no) {
        this.stud_prn_no = stud_prn_no;
    }

    public String getStud_attendance_date() {
        return stud_attendance_date;
    }

    public void setStud_attendance_date(String stud_attendance_date) {
        this.stud_attendance_date = stud_attendance_date;
    }

    public static ArrayList<AttendanceRecord> collection=new ArrayList<>();
}
