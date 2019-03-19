package com.example.hallymfood;

public class comment_Info {
    String name;
    String comment;

    comment_Info(){ }
    comment_Info(String name,String comment){
        this.name = name;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
    public String getName() {
        return name;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setName(String name) {
        this.name = name;
    }
}
