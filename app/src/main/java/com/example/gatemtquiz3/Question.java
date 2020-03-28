package com.example.gatemtquiz3;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";

    private int id;
    private String question;
    private String questionImage;
    private String answer;
    private String solutionImage;
    private String difficulty;
    private int categoryId;

    public Question() {}

    public Question(String question,String questionImage, String answer, String solutionImage, String difficulty, int categoryId) {
        this.question = question;
        this.questionImage = questionImage;
        this.answer = answer;
        this.solutionImage = solutionImage;
        this.difficulty = difficulty;
        this.categoryId = categoryId;
    }

    protected Question(Parcel in) {
        id = in.readInt();
        question = in.readString();
        questionImage = in.readString();
        answer = in.readString();
        solutionImage = in.readString();
        difficulty = in.readString();
        categoryId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeString(questionImage);
        dest.writeString(answer);
        dest.writeString(solutionImage);
        dest.writeString(difficulty);
        dest.writeInt(categoryId);
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public String getSolutionImage() {
        return solutionImage;
    }

    public void setSolutionImage(String solutionImage) {
        this.solutionImage = solutionImage;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public static String[] getAllDifficultyLevels() {
        return new String[] {
                DIFFICULTY_EASY,
            DIFFICULTY_MEDIUM,
            DIFFICULTY_HARD
        };
    }
}
