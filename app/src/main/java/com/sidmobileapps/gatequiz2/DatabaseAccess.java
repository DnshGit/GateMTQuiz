package com.sidmobileapps.gatequiz2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sidmobileapps.gatequiz2.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getAllSections() {
        List<String> sectionsList = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT DISTINCT " + QuestionsTable.COLUMN_SECTION +
                " FROM " + QuestionsTable.TABLE_NAME +
                " ORDER BY " + QuestionsTable.COLUMN_SECTION, null);

        if(c.moveToFirst()) {
            do{
                String sectionName = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_SECTION));
                sectionsList.add(sectionName);
            }while (c.moveToNext());
        }

        c.close();
        return sectionsList;
    }

    public List<String> getAllSubSections() {
        List<String> subSectionsList = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT DISTINCT " + QuestionsTable.COLUMN_SUB_SECTION +
                " FROM " + QuestionsTable.TABLE_NAME +
                " WHERE " + QuestionsTable.COLUMN_SECTION +
                " = \"" + MainActivity.sectionName + "\"" +
                " ORDER BY " + QuestionsTable.COLUMN_SUB_SECTION, null);

        if(c.moveToFirst()) {
            do{
                String subSectionName = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_SUB_SECTION));
                subSectionsList.add(subSectionName);
            }while (c.moveToNext());
        }

        c.close();
        return subSectionsList;
    }

    public ArrayList<Question> getQuestions(String sectionName, String subSection) {

        ArrayList<Question> questionList = new ArrayList<>();

        Cursor c = database.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME +
                " WHERE " + QuestionsTable.COLUMN_SECTION +
                " = \"" + sectionName + "\"" +
                " AND " + QuestionsTable.COLUMN_SUB_SECTION +
                " = \"" + subSection + "\"", null);

        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ID)));
                question.setQuestionImage(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION_IMAGE)));
                question.setAnswer(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER)));
                question.setMarks(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_MARKS)));
                questionList.add(question);

            }while (c.moveToNext());
        }

        c.close();
        return questionList;

    }
}
