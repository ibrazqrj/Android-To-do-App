package com.example.todoapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class TodoItem implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("time")
    private Date time;
    @SerializedName("isComplete")
    private boolean isComplete;

    public TodoItem(String title, String description, Date time, boolean isComplete){
        this.title = title;
        this.description = description;
        this.time = time;
        this.isComplete = isComplete;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", time='" + time + '\'' +
                ", isComplete=" + isComplete +
                '}';
    }
}
