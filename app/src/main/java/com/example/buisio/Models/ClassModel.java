package com.example.buisio.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ClassModel {
    private String id;
    private String name;
    private String date; // Stored as a String in "yyyy-MM-dd" format
    private String time;
    private String classInstructor;
    private int numOfUsers;
    private Map<String, Boolean> enrolledUsers;



    public ClassModel() {
        // Needed for Firebase
    }

    public ClassModel(String Id,String name, String date,String time,String classInstructor) {
        this.id = Id;
        this.name = name;
        this.classInstructor = classInstructor;
        this.time = time;
        this.date = date;
        this.numOfUsers = 0;
        this.enrolledUsers = new HashMap<>();
}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getClassInstructor() {
        return classInstructor;
    }

    public void setClassInstructor(String classInstructor) {
        this.classInstructor = classInstructor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(int numOfUsers) {
        this.numOfUsers = numOfUsers;
    }


    public Map<String, Boolean> getEnrolledUsers() {
        return enrolledUsers;
    }

    public void setEnrolledUsers(Map<String, Boolean> enrolledUsers) {
        this.enrolledUsers = enrolledUsers;
    }

    @Override
    public String toString() {
        return "ClassModel{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", class Instructor='" + classInstructor + '\'' +
                ", numOfUsers=" + numOfUsers +
                ", enrolledUsers=" + enrolledUsers +
                '}';
    }
}
