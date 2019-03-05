package edu.wgu.csmi508.progressscheduler;

import android.content.Context;

import java.util.Date;
import java.util.UUID;

public class Assessment {
    private UUID assessmentId;
    private UUID courseId;
    private String mAssessmentName;
    private AssessmentType mAssessmentType;
    private Date mGoalDate;
    private boolean mGoalAlert;
    private Context mContext;

    public enum AssessmentType {
        OBJECTIVE("Objective"), PERFORMANCE("Performance");

        private final String type;

        AssessmentType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return type;
        }
    }


    public Assessment(Context context, UUID courseId) {
        this(context, courseId, UUID.randomUUID());
    }

    public Assessment(Context context, UUID courseId, UUID assessmentID) {
        mContext = context;
        this.assessmentId = assessmentID;
        this.courseId = courseId;
        this.mAssessmentType = AssessmentType.OBJECTIVE;
    }

    public AssessmentType getAssessmentType() {
        return mAssessmentType;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        mAssessmentType = assessmentType;
    }

    public String getAssessmentName() {
        return mAssessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        mAssessmentName = assessmentName;
    }

    public Date getGoalDate() {
        return mGoalDate;
    }

    public void setGoalDate(Date goalDate) {
        mGoalDate = goalDate;
    }

    public boolean isGoalAlert() {
        return mGoalAlert;
    }

    public void setGoalAlert(boolean goalAlert) {
        mGoalAlert = goalAlert;
    }

    public UUID getAssessmentId() {
        return assessmentId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    @Override
    public String toString() {
        return mAssessmentName;
    }
}
