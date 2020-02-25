package com.example.simplebottomnav.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class PhotoItem implements Parcelable {
    private int id;
    private String tags;
    private String webformatURL;
    private String largeImageURL;
    private int likes;
    private int comments;

    protected PhotoItem(Parcel in) {
        id = in.readInt();
        tags = in.readString();
        webformatURL = in.readString();
        largeImageURL = in.readString();
        likes = in.readInt();
        comments = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(tags);
        dest.writeString(webformatURL);
        dest.writeString(largeImageURL);
        dest.writeInt(likes);
        dest.writeInt(comments);
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
                Objects.equals(tags, photoItem.tags) &&
                Objects.equals(webformatURL, photoItem.webformatURL) &&
                Objects.equals(largeImageURL, photoItem.largeImageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tags, webformatURL, largeImageURL, likes, comments);
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

    public void setLargeImageUR(String largeImageUR) {
        this.largeImageURL = largeImageUR;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
