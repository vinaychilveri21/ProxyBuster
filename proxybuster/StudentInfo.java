package com.example.proxybuster;

public class StudentInfo {
    //string variables for storing
    private String stud_name,stud_prn_no,stud_roll,stud_year,stud_dept,stud_div,stud_mob,stud_email,stud_pass;

    // an empty constructor is
    // required when using
    // Firebase Realtime Database.

    public StudentInfo(String stud_name, String stud_prn_no, String stud_roll, String stud_year, String stud_dept, String stud_div, String stud_mob, String stud_email, String stud_pass) {
        this.stud_name = stud_name;
        this.stud_prn_no = stud_prn_no;
        this.stud_roll = stud_roll;
        this.stud_year = stud_year;
        this.stud_dept = stud_dept;
        this.stud_div = stud_div;
        this.stud_mob = stud_mob;
        this.stud_email = stud_email;
        this.stud_pass = stud_pass;
    }


    // created getter and setter methods
    // for all our variables.

    public String getStud_name() {
        return stud_name;
    }

    public void setStud_name(String stud_name) {
        this.stud_name = stud_name;
    }

    public String getStud_prn_no() {
        return stud_prn_no;
    }

    public void setStud_prn_no(String stud_prn_no) {
        this.stud_prn_no = stud_prn_no;
    }

    public String getStud_roll() {
        return stud_roll;
    }

    public void setStud_roll(String stud_roll) {
        this.stud_roll = stud_roll;
    }

    public String getStud_year() {
        return stud_year;
    }

    public void setStud_year(String stud_year) {
        this.stud_year = stud_year;
    }

    public String getStud_dept() {
        return stud_dept;
    }

    public void setStud_dept(String stud_dept) {
        this.stud_dept = stud_dept;
    }

    public String getStud_div() {
        return stud_div;
    }

    public void setStud_div(String stud_div) {
        this.stud_div = stud_div;
    }

    public String getStud_mob() {
        return stud_mob;
    }

    public void setStud_mob(String stud_mob) {
        this.stud_mob = stud_mob;
    }

    public String getStud_email() {
        return stud_email;
    }

    public void setStud_email(String stud_email) {
        this.stud_email = stud_email;
    }

    public String getStud_pass() {
        return stud_pass;
    }

    public void setStud_pass(String stud_pass) {
        this.stud_pass = stud_pass;
    }
}
