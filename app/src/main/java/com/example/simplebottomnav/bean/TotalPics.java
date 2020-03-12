package com.example.simplebottomnav.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class TotalPics implements Parcelable {
    private int total;
    private int totalHits;
    private List<Picture> hits;

    public TotalPics() {
    }

    public TotalPics(int total, int totalHits, List<Picture> hits) {
        this.total = total;
        this.totalHits = totalHits;
        this.hits = hits;
    }

    protected TotalPics(Parcel in) {
        total = in.readInt();
        totalHits = in.readInt();
        hits = in.createTypedArrayList(Picture.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeInt(totalHits);
        dest.writeTypedList(hits);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TotalPics> CREATOR = new Creator<TotalPics>() {
        @Override
        public TotalPics createFromParcel(Parcel in) {
            return new TotalPics(in);
        }

        @Override
        public TotalPics[] newArray(int size) {
            return new TotalPics[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public List<Picture> getHits() {
        return hits;
    }

    public void setHits(ArrayList<Picture> hits) {
        this.hits = hits;
    }
}
