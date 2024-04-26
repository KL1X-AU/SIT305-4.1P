package com.example.taskmanager;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;
public class Task implements Comparator<Task> {
    private int id;
    private String name;
    private String desc;
    private String due_date;
    private long date;

    public Task(int id, String name, String desc, String due_date, long date) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.due_date = due_date;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }
    public String getDue_date() {
        return due_date;
    }
    public long getDate() { return date; }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int compare(Task o1, Task o2) {
        return Long.compare(o1.date,o2.date);
    }
}