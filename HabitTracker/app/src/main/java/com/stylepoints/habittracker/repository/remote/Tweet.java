package com.stylepoints.habittracker.repository.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by mchauck on 11/23/17.
 */

public class Tweet implements Id {
    // names must match their serialized counterparts!
    // or use @SerializedName("new_serialized_name")

    // to ignore stuff when serializing, don't add an @Expose annotation
    // OR we can add `transient` to the field we don't want to be serialized

    private String id;
    @Expose
    private String date;
    @Expose
    private String message;

    public Tweet(String date, String message) {
        this.date = date;
        this.message = message;
    }

    public Tweet(String id, Map<String, String> fields) {
        this.id = id;
        this.date = fields.get("date");
        this.message = fields.get("message");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
