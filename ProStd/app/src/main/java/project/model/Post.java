package project.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Post implements Serializable {

    private String pid;
    private String text;
    private String picture;
    private String uid;

    public Post(String text, String picture, String uid) {
        this.text = text;
        this.picture = picture;
        this.uid = uid;
    }

    public Post(String pid, String text, String picture, String uid) {
        this.pid = pid;
        this.text = text;
        this.picture = picture;
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public String toString() {
        return "Post{" +
                "pid='" + pid + '\'' +
                ", text='" + text + '\'' +
                ", picture='" + picture + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
