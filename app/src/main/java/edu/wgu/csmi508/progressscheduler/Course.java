package edu.wgu.csmi508.progressscheduler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.database.DbBaseHelper;
import edu.wgu.csmi508.progressscheduler.database.DbSchema;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.CourseMentorTable;

public class Course {
    private UUID mId;
    private String mCourseCode;
    private String mTitle;
    private Date mStartDate;
    private Date mProjectedEndDate;
    private Status mStatus;
    private boolean mStartAlarm;
    private boolean mEndAlarm;
    private Context mContext;
    private List<Assessment> mAssessmentList = new ArrayList<>();
    private List<Mentor> mMentorList = new ArrayList<>();
    private List<String> mNoteList = new ArrayList<>();



    public enum Status {
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        DROPPED("Dropped"),
        PLAN_TO_TAKE("Plan to Take");

        private final String statusString;

        Status(String statusString) {
            this.statusString = statusString;
        }

        public String getStatusString() {
            return statusString;
        }

        @Override
        public String toString() {
            return statusString;
        }
    }


    public Course(Context context) {
        this(context, UUID.randomUUID());
    }

    public Course(Context context, UUID id) {
        mContext = context;
        mId = id;
        mStatus = Status.PLAN_TO_TAKE;
        mNoteList = new ArrayList<String>();
    }

    public UUID getCourseId() {
        return mId;
    }

    public String getCourseCode() {
        return mCourseCode;
    }

    public void setCourseCode(String courseCode) {
        mCourseCode = courseCode;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String courseName) {
        mTitle = courseName;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getProjectedEndDate() {
        return mProjectedEndDate;
    }

    public void setProjectedEndDate(Date projectedEndDate) {
        mProjectedEndDate = projectedEndDate;
    }

    public Status getStatus() {
        return mStatus;
    }


    public void setStatus(Status status) {
        mStatus = status;
    }

    public List<Assessment> getAssessmentList() {
        return AssessmentList.getAssessmentList(mContext).getCourseAssessmentList(mId);
    }


    public List<Mentor> getMentorList() {
        mMentorList = new ArrayList<>();
        SQLiteDatabase db = new DbBaseHelper(mContext).getReadableDatabase();
        String selectString = "SELECT " + CourseMentorTable.Cols.MENTOR + " FROM "  +
                CourseMentorTable.NAME + " WHERE "  + CourseMentorTable.Cols.COURSE +
                " = ?"; // + mId.toString();

        Cursor cursor = db.rawQuery(selectString, new String[]{mId.toString()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mMentorList.add(MentorList.getMentorList(mContext).getMentor(UUID.fromString(cursor.getString(cursor.getColumnIndex(CourseMentorTable.Cols.MENTOR)))));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
            db.close();
        }

        return mMentorList;
    }

    public List<String> getNoteList() {
        return mNoteList;
    }

    public boolean isStartAlarm() {
        return mStartAlarm;
    }

    public void setStartAlarm(boolean startAlarm) {
        mStartAlarm = startAlarm;
    }

    public boolean isEndAlarm() {
        return mEndAlarm;
    }

    public void setEndAlarm(boolean endAlarm) {
        mEndAlarm = endAlarm;
    }


    public String getPhotoFilename() {
        return "IMG_" + getCourseCode().toString() + ".jpg";
    }

    @Override
    public String toString() {
        return mCourseCode + ": " + mTitle;
    }
}
