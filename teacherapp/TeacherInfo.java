package com.example.teacherapp;

public class TeacherInfo {
    private String teacher_name,teacher_mob,teacher_email,teacher_pass;

    public TeacherInfo(String teacher_name, String teacher_mob, String teacher_email, String teacher_pass) {
        this.teacher_name = teacher_name;
        this.teacher_mob = teacher_mob;
        this.teacher_email = teacher_email;
        this.teacher_pass = teacher_pass;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_mob() {
        return teacher_mob;
    }

    public void setTeacher_mob(String teacher_mob) {
        this.teacher_mob = teacher_mob;
    }

    public String getTeacher_email() {
        return teacher_email;
    }

    public void setTeacher_email(String teacher_email) {
        this.teacher_email = teacher_email;
    }

    public String getTeacher_pass() {
        return teacher_pass;
    }

    public void setTeacher_pass(String teacher_pass) {
        this.teacher_pass = teacher_pass;
    }
}
