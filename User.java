package com.example.project;

public class User
{
    int numberOfAnswers;
    String badge;
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumberOfAnswers() {
        return numberOfAnswers;
    }

    public void setNumberOfAnswers(int numberOfAnswers) {
        this.numberOfAnswers = numberOfAnswers;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public User()
    {

    }

    public User(int numberOfAnswers, String badge,String username)
    {
        this.numberOfAnswers = numberOfAnswers;
        this.badge = badge;
        this.username = username;

    }
    public User(String username,int numberOfAnswers)
    {
        this.username = username;
        this.numberOfAnswers = numberOfAnswers;
    }
}
