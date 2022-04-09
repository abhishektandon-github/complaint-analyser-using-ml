package com.example.user.complaintanalyser;


/**
 * Created by user on 18-04-2021.
 */

public class ComplaintDetails {


    String key, email, title, body, time, output;
    boolean isResolved;

    public ComplaintDetails() {

    }

    public ComplaintDetails(String key, String email, String title, String body, String time, String output, boolean isResolved) {
        this.key = key;
        this.email = email;
        this.title = title;
        this.body = body;
        this.time = time;
        this.output = output;
        this.isResolved = isResolved;
    }

    public String getKey() {
        return key;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getTime() {
        return time;
    }

    public String getOutput() {
        return output;
    }

    public boolean getIsResolved() {
        return isResolved;
    }


}
