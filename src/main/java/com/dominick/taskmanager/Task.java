package com.dominick.taskmanager;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private String name;
    private Date dueDate;
    private String status;

    public Task(String name, Date dueDate, String status) {
        this.name = name;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDueDate() {
        return dueDate;

    }

    public Date setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public String setStatus(String status) {
        this.status = status;
        return status;
    }

    public void markAsComplete() {
        this.status = "Completed";
    }

    public void markAsInProgress() {
        this.status = "In Progress";
    }

    public void displayTaskDetails() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Task Details:");
        System.out.println("Name: " + name);
        System.out.println("Due Date: " + dateFormat.format(dueDate));
        System.out.println("Status: " + status);
    }

    public String getFormattedDueDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dueDate);
    }
}

