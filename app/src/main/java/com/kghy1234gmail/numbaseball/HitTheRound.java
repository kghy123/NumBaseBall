package com.kghy1234gmail.numbaseball;

import android.os.Parcel;
import android.os.Parcelable;

public class HitTheRound implements Parcelable{


    int strikes;
    int balls;
    int outs;
    int num;

    public HitTheRound() {
    }

    public HitTheRound(int strikes, int balls, int outs) {
        this.balls = balls;
        this.strikes = strikes;
        this.outs = outs;
    }

    protected HitTheRound(Parcel in) {
        strikes = in.readInt();
        balls = in.readInt();
        outs = in.readInt();
        num = in.readInt();
    }

    public static final Creator<HitTheRound> CREATOR = new Creator<HitTheRound>() {
        @Override
        public HitTheRound createFromParcel(Parcel in) {
            return new HitTheRound(in);
        }

        @Override
        public HitTheRound[] newArray(int size) {
            return new HitTheRound[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(strikes);
        parcel.writeInt(balls);
        parcel.writeInt(outs);
        parcel.writeInt(num);
    }
}
