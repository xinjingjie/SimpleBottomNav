package com.example.simplebottomnav.bean;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Picture implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int p_id;
    @ColumnInfo
    private int user_id;
    @ColumnInfo
    private String username;
    @ColumnInfo
    private String update_time;
    @ColumnInfo
    private String location;
    @ColumnInfo
    private int likes;
    @ColumnInfo
    private int comments;
    @ColumnInfo
    private String content;
    @ColumnInfo
    private String tags;
    @ColumnInfo
    private String profile_picture;

    @Ignore
    public Picture() {
    }


    public Picture(int user_id, String username, String update_time, String location, int likes, int comments, String content, String tags, String profile_picture) {
        this.user_id = user_id;
        this.update_time = update_time;
        this.location = location;
        this.likes = likes;
        this.comments = comments;
        this.content = content;
        this.tags = tags;
        this.username = username;
        this.profile_picture = profile_picture;
    }

    protected Picture(Parcel in) {
        p_id = in.readInt();
        user_id = in.readInt();
        username = in.readString();
        update_time = in.readString();
        location = in.readString();
        likes = in.readInt();
        comments = in.readInt();
        content = in.readString();
        tags = in.readString();
        profile_picture = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(p_id);
        dest.writeInt(user_id);
        dest.writeString(username);
        dest.writeString(update_time);
        dest.writeString(location);
        dest.writeInt(likes);
        dest.writeInt(comments);
        dest.writeString(content);
        dest.writeString(tags);
        dest.writeString(profile_picture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getP_id() {
        return p_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getLocation() {
        return location;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "p_id=" + p_id +
                ", user_id=" + user_id +
                ", username='" + username + '\'' +
                ", update_time='" + update_time + '\'' +
                ", location='" + location + '\'' +
                ", likes=" + likes +
                ", comments=" + comments +
                ", content='" + content + '\'' +
                ", tags='" + tags + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return user_id == picture.user_id &&
                likes == picture.likes &&
                comments == picture.comments &&
                Objects.equals(username, picture.username) &&
                Objects.equals(update_time, picture.update_time) &&
                Objects.equals(location, picture.location) &&
                Objects.equals(content, picture.content) &&
                Objects.equals(tags, picture.tags) &&
                Objects.equals(profile_picture, picture.profile_picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p_id, user_id, username, update_time, location, likes, comments, content, tags, profile_picture);
    }
}
