package com.example.day3_1_youlou.entity;

/**
 * Created by mwg on 2017/12/7.
 */

public class Conversation {
    private int thread_id;
    private String name;// 联系人姓名
    private String address;// 地址
    private String body;// 会话内容
    private long date;// 会话时间
    private String dateStr;// 格式化后的日期
    private int photoId;// 头像的编号
    private int read;// 读取的状态

    public Conversation() {
    }

    public Conversation(int thread_id, String name, String address,
            String body, long date, String dateStr, int photoId, int read) {
        this.thread_id = thread_id;
        this.name = name;
        this.address = address;
        this.body = body;
        this.date = date;
        this.dateStr = dateStr;
        this.photoId = photoId;
        this.read = read;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "thread_id=" + thread_id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", body='" + body + '\'' +
                ", date=" + date +
                ", dateStr='" + dateStr + '\'' +
                ", photoId=" + photoId +
                ", read=" + read +
                '}';
    }
}
