package edu.wgu.csmi508.progressscheduler.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.Course;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.CourseTable;

public class CourseCursorWrapper extends CursorWrapper {
    private Context mContext;

    public CourseCursorWrapper(Context context, Cursor cursor) {
        super(cursor);
        mContext = context;
    }

    public Course getCourse() {
        String uuidString = getString(getColumnIndex(CourseTable.Cols.UUID));
        String courseCode = getString(getColumnIndex(CourseTable.Cols.COURSE_CODE));
        String courseName = getString(getColumnIndex(CourseTable.Cols.COURSE_NAME));
        long startDate = getLong(getColumnIndex(CourseTable.Cols.START_DATE));
        long endDate = getLong(getColumnIndex(CourseTable.Cols.END_DATE));
        String status = getString(getColumnIndex(CourseTable.Cols.STATUS));
        int startAlarm = getInt(getColumnIndex(CourseTable.Cols.START_ALARM));
        int endAlarm = getInt(getColumnIndex(CourseTable.Cols.END_ALARM));

        Course.Status courseStatus = Course.Status.IN_PROGRESS;
        if (status.equals("Completed")) {
            courseStatus = Course.Status.COMPLETED;
        } else if (status.equals("Dropped")) {
            courseStatus = Course.Status.DROPPED;
        } else if (status.equals("Plan to Take")) {
            courseStatus = Course.Status.PLAN_TO_TAKE;
        }

        Course course = new Course(mContext, UUID.fromString(uuidString));
        course.setCourseCode(courseCode);
        course.setTitle(courseName);
        course.setStartDate(new Date(startDate));
        course.setProjectedEndDate(new Date(endDate));
        course.setStatus(courseStatus);
        course.setStartAlarm(startAlarm != 0);
        course.setEndAlarm(endAlarm != 0);

        return course;
    }
}
