package edu.wgu.csmi508.progressscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.database.AssessmentCursorWrapper;
import edu.wgu.csmi508.progressscheduler.database.DbBaseHelper;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.AssessmentTable;

public class AssessmentList {
    private static AssessmentList sAssessmentList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static AssessmentList getAssessmentList(Context context) {
        if (sAssessmentList == null) {
            sAssessmentList = new AssessmentList(context);
        }
        return sAssessmentList;
    }

    private AssessmentList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbBaseHelper(mContext).getWritableDatabase();
    }

    public void addAssessment(Assessment assessment) {
        ContentValues values = getContentValues(assessment);

        mDatabase.insert(AssessmentTable.NAME, null, values);
    }

    public void removeAssessment(Assessment assessment) {
        String uuidString = assessment.getAssessmentId().toString();

        mDatabase.delete(AssessmentTable.NAME,
                AssessmentTable.Cols.ASSESSMENT_UUID + " = ?",
                new String[] { uuidString });
    }

    public List<Assessment> getCourseAssessmentList(UUID courseId) {
        List<Assessment> assessments = new ArrayList<>();

        AssessmentCursorWrapper cursor = queryCourses(AssessmentTable.Cols.COURSE_UUID + " = ?" , new String[] {courseId.toString()}, null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                assessments.add(cursor.getAssessment(mContext));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return assessments;
    }

    public List<Assessment> getFullAssessmentList() {
        List<Assessment> assessments = new ArrayList<>();

        AssessmentCursorWrapper cursor = queryCourses(null , null, null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                assessments.add(cursor.getAssessment(mContext));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return assessments;
    }

    public Assessment getAssessment(UUID assessmentId) {
        AssessmentCursorWrapper cursor = queryCourses(
                AssessmentTable.Cols.ASSESSMENT_UUID + " = ?",
                new String[] {assessmentId.toString()},
                null,
                null
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getAssessment(mContext);
        } finally {
            cursor.close();
        }
    }

    public void updateAssessment(Assessment assessment) {
        String uuidString = assessment.getAssessmentId().toString();
        ContentValues values = getContentValues(assessment);

        mDatabase.update(AssessmentTable.NAME, values,
                AssessmentTable.Cols.ASSESSMENT_UUID + " = ?",
                new String[] { uuidString });
    }

    private AssessmentCursorWrapper queryCourses(String whereClause, String[] whereArgs, String groupBy,
                                           String orderBy) {
        Cursor cursor = mDatabase.query(
                AssessmentTable.NAME,
                null,
                whereClause,
                whereArgs,
                groupBy,
                null,
                orderBy
        );

        return new AssessmentCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Assessment assessment) {
        ContentValues values = new ContentValues();
        values.put(AssessmentTable.Cols.ASSESSMENT_UUID, assessment.getAssessmentId().toString());
        values.put(AssessmentTable.Cols.COURSE_UUID, assessment.getCourseId().toString());
        values.put(AssessmentTable.Cols.ASSESSMENT_NAME, assessment.getAssessmentName());
        values.put(AssessmentTable.Cols.P_OR_O, assessment.getAssessmentType().getType());
        if (assessment.getGoalDate() != null) {
            values.put(AssessmentTable.Cols.DATE, assessment.getGoalDate().getTime());
        } else {
            values.put(AssessmentTable.Cols.DATE, new Date(0).getTime());
        }
        values.put(AssessmentTable.Cols.ALARM, assessment.isGoalAlert() ? 1 : 0);

        return values;
    }
}
