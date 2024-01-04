package com.dominick.taskmanager;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

    public Date getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void markAsComplete() {
        this.status = "Completed";
    }

    public void markAsInProgress() {
        this.status = "In Progress";
    }

}

