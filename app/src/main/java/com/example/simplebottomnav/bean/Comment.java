package com.example.simplebottomnav.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Objects;

public class Comment implements Parcelable {
    private int c_id;
    private int p_id;
    private int uid;
    private int likes;
    private int dislikes;
    private Date create_time;
    private String content;
    private String profile_picture;
    private String username;

    public Comment() {
    }

    public Comment(int p_id, int uid, int likes, int dislikes, Date create_time, String content, String profile_picture, String username) {
        this.p_id = p_id;
        this.uid = uid;
        this.likes = likes;
        this.dislikes = dislikes;
        this.create_time = create_time;
        this.content = content;
        this.profile_picture = profile_picture;
        this.username = username;
    }


    protected Comment(Parcel in) {
        c_id = in.readInt();
        p_id = in.readInt();
        uid = in.readInt();
        likes = in.readInt();
        dislikes = in.readInt();
        content = in.readString();
        profile_picture = in.readString();
        username = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(c_id);
        dest.writeInt(p_id);
        dest.writeInt(uid);
        dest.writeInt(likes);
        dest.writeInt(dislikes);
        dest.writeString(content);
        dest.writeString(profile_picture);
        dest.writeString(username);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public String toString() {
        return "Comment{" +
                "c_id=" + c_id +
                ", p_id=" + p_id +
                ", uid=" + uid +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", create_time=" + create_time +
                ", content='" + content + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return p_id == comment.p_id &&
                uid == comment.uid &&
                likes == comment.likes &&
                dislikes == comment.dislikes &&
                Objects.equals(create_time, comment.create_time) &&
                Objects.equals(content, comment.content) &&
                Objects.equals(profile_picture, comment.profile_picture) &&
                Objects.equals(username, comment.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p_id, uid, likes, dislikes, create_time, content, profile_picture, username);
    }

}
