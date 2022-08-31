package com.sidmobileapps.gatequiz2;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    private int id;
    private String questionImage;
    private String answer;
    private int marks;

    public Question() {}

    public Question(String questionImage, String answer, int marks) {
        this.questionImage = questionImage;
        this.answer = answer;
        this.marks = marks;
    }

    protected Question(Parcel in) {
        id = in.readInt();
        questionImage = in.readString();
        answer = in.readString();
        marks = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(questionImage);
        dest.writeString(answer);
        dest.writeInt(marks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

}
