package com.example.simplebottomnav.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class PhotoItem implements Parcelable {
    private int id;
    private String tags;
    private String webformatURL;
    private String largeImageURL;
    private String user;
    private int user_id;
    private String userImageURL;
    private int likes;
    private int comments;
    private int webformatHeight;
    private int webformatWidth;

    protected PhotoItem(Parcel in) {
        id = in.readInt();
        tags = in.readString();
        webformatURL = in.readString();
        largeImageURL = in.readString();
        user = in.readString();
        user_id = in.readInt();
        userImageURL = in.readString();
        likes = in.readInt();
        comments = in.readInt();
        webformatHeight = in.readInt();
        webformatWidth = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(tags);
        dest.writeString(webformatURL);
        dest.writeString(largeImageURL);
        dest.writeString(user);
        dest.writeInt(user_id);
        dest.writeString(userImageURL);
        dest.writeInt(likes);
        dest.writeInt(comments);
        dest.writeInt(webformatHeight);
        dest.writeInt(webformatWidth);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
        @Override
        public PhotoItem createFromParcel(Parcel in) {
            return new PhotoItem(in);
        }

        @Override
        public PhotoItem[] newArray(int size) {
            return new PhotoItem[size];
        }
    };


    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        PhotoItem photoItem = (PhotoItem) o;
        return id == photoItem.id &&
                likes == photoItem.likes &&
                comments == photoItem.comments &&
                webformatHeight == photoItem.webformatHeight &&
                Objects.equals(tags, photoItem.tags) &&
                Objects.equals(webformatURL, photoItem.webformatURL) &&
                Objects.equals(largeImageURL, photoItem.largeImageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tags, webformatURL, largeImageURL, user, user_id, likes, comments, webformatHeight);
    }

    public int getId() {
        return id;
    }

    public String getTags() {
        return tags;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public void setLargeImageURL(String largeImageUR) {
        this.largeImageURL = largeImageUR;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getWebformatHeight() {
        return webformatHeight;
    }

    public void setWebformatHeight(int webformatHeight) {
        this.webformatHeight = webformatHeight;
    }

    public int getWebformatWidth() {
        return webformatWidth;
    }

    public void setWebformatWidth(int webformatWidth) {
        this.webformatWidth = webformatWidth;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }
}
