package edu.wgu.csmi508.progressscheduler.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.wgu.csmi508.progressscheduler.database.DbSchema.AssessmentTable;
//import edu.wgu.csmi508.progressscheduler.database.DbSchema.CourseAssessment;
//import edu.wgu.csmi508.progressscheduler.database.DbSchema.CourseMentor;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.CourseMentorTable;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.CourseTable;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.MentorTable;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.NoteTable;
//import edu.wgu.csmi508.progressscheduler.database.DbSchema.TermCourse;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.TermCourseTable;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.TermTable;

public class DbBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "db.db";

//    // table names
//    private static final String TABLE_TERM = TermTable.NAME;
//    private static final String TABLE_COURSE = CourseTable.NAME;
//    private static final String TABLE_MENTOR = MentorTable.NAME;
//    private static final String TABLE_ASSESSMENT = AssessmentTable.NAME;
//    private static final String TABLE_TERM_COURSE = TermCourse.NAME;
//    private static final String TABLE_COURSE_MENTOR = CourseMentor.NAME;
//    private static final String TABLE_COURSE_ASSESSMENT = CourseAssessment.NAME;




    public DbBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TermTable.CREATE);
        db.execSQL(CourseTable.CREATE);
        db.execSQL(MentorTable.CREATE);
        db.execSQL(AssessmentTable.CREATE);
        db.execSQL(TermCourseTable.CREATE);
        db.execSQL(CourseMentorTable.CREATE);
//        db.execSQL(CourseAssessment.CREATE);
        db.execSQL(NoteTable.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
