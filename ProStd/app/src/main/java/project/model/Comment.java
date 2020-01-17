package project.model;

import java.io.Serializable;

public class Comment implements Serializable {

    private String cid;
    private String text;
    private String pid;
    private String username;
    private String uid;

    public Comment(String text, String pid, String uid) {
        this.text = text;
        this.pid = pid;
        this.uid = uid;
    }

    public Comment(String cid, String text, String pid, String uid, String username) {
        this.cid = cid;
        this.text = text;
        this.pid = pid;
        this.uid = uid;
        this.username = username;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "cid='" + cid + '\'' +
                ", text='" + text + '\'' +
                ", pid='" + pid + '\'' +
                ", uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
