package edu.wgu.csmi508.progressscheduler.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.Assessment;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.AssessmentTable;

public class AssessmentCursorWrapper extends CursorWrapper {

    public AssessmentCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Assessment getAssessment(Context context) {
        String assessmentUuidString = getString(getColumnIndex(AssessmentTable.Cols.ASSESSMENT_UUID));
        String courseUuidString = getString(getColumnIndex(AssessmentTable.Cols.COURSE_UUID));
        String assessmentName = getString(getColumnIndex(AssessmentTable.Cols.ASSESSMENT_NAME));
        String type = getString(getColumnIndex(AssessmentTable.Cols.P_OR_O));
        long date = getLong(getColumnIndex(AssessmentTable.Cols.DATE));
        int alarm = getInt(getColumnIndex(AssessmentTable.Cols.ALARM));


        Assessment.AssessmentType assessmentType = Assessment.AssessmentType.OBJECTIVE;
        if (type.equals("Performance")) {
            assessmentType = Assessment.AssessmentType.PERFORMANCE;
        }

        Assessment assessment = new Assessment(context, UUID.fromString(courseUuidString), UUID.fromString(assessmentUuidString));
        assessment.setAssessmentName(assessmentName);
        assessment.setAssessmentType(assessmentType);
        assessment.setGoalDate(new Date(date));
        assessment.setGoalAlert(alarm != 0);

        return assessment;
    }
}