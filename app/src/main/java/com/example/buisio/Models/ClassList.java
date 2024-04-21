package com.example.buisio.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassList {
    private Date dayName; // Name of the day
    private List<ClassModel> classes; // List of classes on that day


    public ClassList() {

    }

    // Constructor with day name
    public ClassList(Date dayName) {
        this.dayName = dayName;
        this.classes = new ArrayList<>();
    }

    // Getters and setters
    public Date getDayName() {
        return dayName;
    }

    public void setDayName(Date dayName) {
        this.dayName = dayName;
    }

    public List<ClassModel> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassModel> classes) {
        this.classes = classes;
    }

    // Method to add a class to the list
    public void addClass(ClassModel classInfo) {
        this.classes.add(classInfo);
    }
}
