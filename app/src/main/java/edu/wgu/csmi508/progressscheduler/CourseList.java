package edu.wgu.csmi508.progressscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.database.CourseCursorWrapper;
import edu.wgu.csmi508.progressscheduler.database.DbBaseHelper;
import edu.wgu.csmi508.progressscheduler.database.DbSchema;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.CourseTable;

// Singleton class
public class CourseList {
    private static CourseList sCourseList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CourseList getCourseList(Context context) {
        if (sCourseList == null) {
            sCourseList = new CourseList(context);
        }
        return sCourseList;
    }

    private CourseList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbBaseHelper(mContext).getWritableDatabase();
    }

    public void addCourse(Course c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(CourseTable.NAME, null, values);
    }

    public void removeCourse(Course c) {
        String uuidString = c.getCourseId().toString();

        mDatabase.delete(CourseTable.NAME,
                CourseTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public List<Course> getCourseList() {
        List<Course> courses = new ArrayList<>();

        CourseCursorWrapper cursor = queryCourses(null, null, null, CourseTable.Cols.COURSE_CODE + " ASC");

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                courses.add(cursor.getCourse());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return courses;
    }

    public Course getCourse(UUID courseId) {
        CourseCursorWrapper cursor = queryCourses(
                CourseTable.Cols.UUID + " = ?",
                new String[] {courseId.toString()},
                null,
                null
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCourse();
        } finally {
            cursor.close();
        }
    }

    public File getPicture(Course course) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, course.getPhotoFilename());
    }

    public void updateCourse(Course course) {
        String uuidString = course.getCourseId().toString();
        ContentValues values = getContentValues(course);

        mDatabase.update(CourseTable.NAME, values,
                CourseTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private CourseCursorWrapper queryCourses(String whereClause, String[] whereArgs, String groupBy,
                                             String orderBy) {
        Cursor cursor = mDatabase.query(
                CourseTable.NAME,
                null,
                whereClause,
                whereArgs,
                groupBy,
                null,
                orderBy
        );

        return new CourseCursorWrapper(mContext, cursor);
    }

    private static ContentValues getContentValues(Course course) {
        ContentValues values = new ContentValues();
        values.put(CourseTable.Cols.UUID, course.getCourseId().toString());
        values.put(CourseTable.Cols.COURSE_CODE, course.getCourseCode());
        values.put(CourseTable.Cols.COURSE_NAME, course.getTitle());
        if (course.getStartDate() != null) {
            values.put(CourseTable.Cols.START_DATE, course.getStartDate().getTime());
        }
        if (course.getProjectedEndDate() != null) {
            values.put(CourseTable.Cols.END_DATE, course.getProjectedEndDate().getTime());
        }
        values.put(CourseTable.Cols.STATUS, course.getStatus().toString());
        values.put(CourseTable.Cols.START_ALARM, course.isStartAlarm() ? 1 : 0);
        values.put(CourseTable.Cols.END_ALARM, course.isEndAlarm() ? 1 : 0);

        return values;
    }
}
