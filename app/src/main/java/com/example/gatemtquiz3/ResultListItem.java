package com.example.gatemtquiz3;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultListItem implements Parcelable {
    private Integer serialNo;
    private String answerValue;
    private Double currentScore;

    public ResultListItem(Integer serialNo, String answerValue, Double currentScore) {
        this.serialNo = serialNo;
        this.answerValue = answerValue;
        this.currentScore = currentScore;
    }

    protected ResultListItem(Parcel in) {
        if (in.readByte() == 0) {
            serialNo = null;
        } else {
            serialNo = in.readInt();
        }
        answerValue = in.readString();
        if (in.readByte() == 0) {
            currentScore = null;
        } else {
            currentScore = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (serialNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(serialNo);
        }
        dest.writeString(answerValue);
        if (currentScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(currentScore);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResultListItem> CREATOR = new Creator<ResultListItem>() {
        @Override
        public ResultListItem createFromParcel(Parcel in) {
            return new ResultListItem(in);
        }

        @Override
        public ResultListItem[] newArray(int size) {
            return new ResultListItem[size];
        }
    };

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }

    public Double getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(Double currentScore) {
        this.currentScore = currentScore;
    }
}
