package com.example.mentalhub.progressChecker;

public class UserProgress {
    private int quizPoints;
    private int breathingPoints;
    private int mindPoints;
    private int cognitivePoints;
    private int journalPoints;
    private int problemSolvingPoints;

    public UserProgress() {
        // Default constructor required by Firebase
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
}
