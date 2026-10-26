package com.example.exam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Student.db";
    private static final String TABLE_NAME = "students";

    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_STUDENT_ID = "STUDENT_ID";
    private static final String COL_MAJOR = "MAJOR";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                        " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " TEXT, " +
                        COL_STUDENT_ID + " TEXT UNIQUE, " +
                        COL_MAJOR + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addStudent(String name, String studentId, String major) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_STUDENT_ID, studentId);
        values.put(COL_MAJOR, major);

        long result = db.insert(TABLE_NAME, null, values);

        return result != -1;
    }

    public boolean updateStudent(String name, String studentId, String major) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_MAJOR, major);

        int result = db.update(
                TABLE_NAME,
                values,
                COL_STUDENT_ID + "=?",
                new String[]{studentId}
        );

        return result > 0;
    }

    public boolean deleteStudent(String studentId) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_NAME,
                COL_STUDENT_ID + "=?",
                new String[]{studentId}
        );

        return result > 0;
    }

    public Cursor getAllStudents() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_NAME,
                null
        );
    }
}