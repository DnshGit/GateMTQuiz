package com.example.gatemtquiz3;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract() {}

    public static class CategoriesTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_categories";
        public static final String COLUMN_NAME = "name";
    }

    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_QUESTION_IMAGE = "question_image";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_SOLUTION_IMAGE = "solution_image";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_CATEGORY_ID = "category_id";
    }
}
