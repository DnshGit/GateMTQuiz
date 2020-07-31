package com.sidmobileapps.gatequizenggmaths;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionGridItem implements Parcelable {
    int answerStatusColor;

    public QuestionGridItem(int answerStatusColor) {
        this.answerStatusColor = answerStatusColor;
    }

    protected QuestionGridItem(Parcel in) {
        answerStatusColor = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(answerStatusColor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionGridItem> CREATOR = new Creator<QuestionGridItem>() {
        @Override
        public QuestionGridItem createFromParcel(Parcel in) {
            return new QuestionGridItem(in);
        }

        @Override
        public QuestionGridItem[] newArray(int size) {
            return new QuestionGridItem[size];
        }
    };

    public int getAnswerStatusColor() {
        return answerStatusColor;
    }

    public void setAnswerStatusColor(int answerStatusColor) {
        this.answerStatusColor = answerStatusColor;
    }
}
