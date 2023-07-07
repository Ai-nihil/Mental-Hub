package com.example.mentalhub.PsychologistSide;

public class User {
    private String name;
    private String email;
    private String userType;
    private String userId;
    private int quizPoints;
    private int breathingPoints;
    private int mindPoints;
    private int cognitivePoints;
    private int journalPoints;
    private int problemSolvingPoints;

    public User() {
    }

    public User(String userId, String name, String email, String userType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.userType = userType;
    }



    public String getUserType() {
        return userType;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    public int getQuizPoints() {
        return quizPoints;
    }

    public void setQuizPoints(int quizPoints) {
        this.quizPoints = quizPoints;
    }

    public int getBreathingPoints() {
        return breathingPoints;
    }

    public void setBreathingPoints(int breathingPoints) {
        this.breathingPoints = breathingPoints;
    }

    public int getMindPoints() {
        return mindPoints;
    }

    public void setMindPoints(int mindPoints) {
        this.mindPoints = mindPoints;
    }

    public int getCognitivePoints() {
        return cognitivePoints;
    }

    public void setCognitivePoints(int cognitivePoints) {
        this.cognitivePoints = cognitivePoints;
    }

    public int getJournalPoints() {
        return journalPoints;
    }

    public void setJournalPoints(int journalPoints) {
        this.journalPoints = journalPoints;
    }

    public int getProblemSolvingPoints() {
        return problemSolvingPoints;
    }

    public void setProblemSolvingPoints(int problemSolvingPoints) {
        this.problemSolvingPoints = problemSolvingPoints;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
