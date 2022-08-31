package com.sidmobileapps.gatequiz2;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract() {}

    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_QUESTION_IMAGE = "question_image";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_MARKS = "marks";
        public static final String COLUMN_SOLUTION_IMAGE = "solution_image";
        public static final String COLUMN_SECTION = "section";
        public static final String COLUMN_SUB_SECTION = "sub_section";
    }
}
