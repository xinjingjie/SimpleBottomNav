package com.example.simplebottomnav.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Objects;

public class Pixabay implements Parcelable {
    private int total;
    private int totalHits;
    private ArrayList<PhotoItem> hits;

    protected Pixabay(Parcel in) {
        total = in.readInt();
        totalHits = in.readInt();
        hits = in.createTypedArrayList(PhotoItem.CREATOR);
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

    public static final Creator<Pixabay> CREATOR = new Creator<Pixabay>() {
        @Override
        public Pixabay createFromParcel(Parcel in) {
            return new Pixabay(in);
        }

        @Override
        public Pixabay[] newArray(int size) {
            return new Pixabay[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public ArrayList<PhotoItem> getHits() {
        return hits;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Pixabay pixabay = (Pixabay) o;
        return total == pixabay.total &&
                totalHits == pixabay.totalHits &&
                Objects.equals(hits, pixabay.hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, totalHits, hits);
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public void setHits(ArrayList<PhotoItem> hits) {
        this.hits = hits;
    }
}

