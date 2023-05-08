package com.example.mentalhub.quiz;

import com.google.firebase.Timestamp;

public class Quiz {
    String name;
    Timestamp timestamp;

    public Quiz() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}