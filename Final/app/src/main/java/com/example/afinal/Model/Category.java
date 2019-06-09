package com.example.afinal.Model;

public class  Category {
    private String Date;
    private String Explanation;
    private String Title;
    private String Url;

    public Category() {
    }

    public Category(String date, String explanation, String title, String url) {
        Date = date;
        Explanation = explanation;
        Title = title;
        Url = url;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getExplanation() {
        return Explanation;
    }

    public void setExplanation(String explanation) {
        Explanation = explanation;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}

