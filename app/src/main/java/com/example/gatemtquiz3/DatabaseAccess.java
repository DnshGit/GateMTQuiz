package com.example.gatemtquiz3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gatemtquiz3.QuizContract.*;

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
    public List<Categories> getAllCategories() {
        List<Categories> categoriesList = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if(c.moveToFirst()) {
            do{
                Categories categories = new Categories();
                categories.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                categories.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoriesList.add(categories);
            }while (c.moveToNext());
        }

        c.close();
        return categoriesList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {

        ArrayList<Question> questionList = new ArrayList<>();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};
        Cursor c = database.query(QuestionsTable.TABLE_NAME, null, selection,
                selectionArgs, null, null, null);

        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setQuestionImage(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION_IMAGE)));
                question.setAnswer(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER)));
                question.setSolutionImage(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_SOLUTION_IMAGE)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryId(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);

            }while (c.moveToNext());
        }
        c.close();
        return questionList;

    }
}
