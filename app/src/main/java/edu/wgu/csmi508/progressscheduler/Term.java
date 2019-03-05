package edu.wgu.csmi508.progressscheduler;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.database.DbBaseHelper;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.TermCourseTable;

public class Term {
    private UUID mTermId;
    private String mTermName;
    private Date mTermStartDate;
    private Date mTermEndDate;

    private List<Course> mTermCourseList;


    public Term() {
        this(UUID.randomUUID());
    }

    public Term(UUID id) {
        mTermId = id;
        mTermCourseList = new ArrayList<Course>();
    }



    public String getTermName() {
        return mTermName;
    }

    public void setTermName(String termName) {
        mTermName = termName;
    }

    public Date getTermStartDate() {
        return mTermStartDate;
    }

    public void setTermStartDate(Date termStartDate) {
        mTermStartDate = termStartDate;
        setTermEndDate(termStartDate);
    }

    public Date getTermEndDate() {
        return mTermEndDate;
    }

    private Date setTermEndDate(Date termStartDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(termStartDate);
        calendar.add(Calendar.MONTH, 6);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        mTermEndDate = calendar.getTime();
        return calendar.getTime();
    }

    public UUID getTermId() {
        return mTermId;
    }

    @Override
    public String toString() {
        return mTermName;
    }

    public List<Course> getTermCourseList(Context context) {
        mTermCourseList = new ArrayList<>();
        SQLiteDatabase db = new DbBaseHelper(context).getReadableDatabase();
        String selectString = "SELECT " + TermCourseTable.Cols.COURSE + " FROM "  +
                TermCourseTable.NAME + " WHERE " + TermCourseTable.Cols.TERM +
                " = ?";

        Cursor cursor = db.rawQuery(selectString, new String[]{mTermId.toString()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mTermCourseList.add(CourseList.getCourseList(context).getCourse(UUID.fromString(cursor.getString(cursor.getColumnIndex(TermCourseTable.Cols.COURSE)))));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
            db.close();
        }

        return mTermCourseList;
    }
}
