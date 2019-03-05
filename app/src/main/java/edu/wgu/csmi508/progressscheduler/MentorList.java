package edu.wgu.csmi508.progressscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.database.DbBaseHelper;
import edu.wgu.csmi508.progressscheduler.database.DbSchema;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.MentorTable;
import edu.wgu.csmi508.progressscheduler.database.MentorCursorWrapper;

// Singleton
public class MentorList {
    private static MentorList sMentorList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static MentorList getMentorList(Context context) {
        if (sMentorList == null) {
            sMentorList = new MentorList(context);
        }
        return sMentorList;
    }

    private MentorList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbBaseHelper(mContext).getWritableDatabase();
    }

    public void addMentor(Mentor mentor) {
        ContentValues values = getContentValues(mentor);

        mDatabase.insert(MentorTable.NAME, null, values);
    }

    public void removeMentor(Mentor mentor) {
        String uuidString = mentor.getMentorId().toString();

        mDatabase.delete(MentorTable.NAME,
                MentorTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public List<Mentor> getMentorList() {
        List<Mentor> mentors = new ArrayList<>();

        MentorCursorWrapper cursor = queryMentors(null, null, null, MentorTable.Cols.NAME + " ASC");

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mentors.add(cursor.getMentor());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mentors;
    }


    public Mentor getMentor(UUID mentorId) {
        MentorCursorWrapper cursor = queryMentors(
                MentorTable.Cols.UUID + " = ?",
                new String[] {mentorId.toString()},
                null,
                null
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getMentor();
        } finally {
            cursor.close();
        }
    }

    public void updateMentor(Mentor mentor) {
        String uuidString = mentor.getMentorId().toString();
        ContentValues values = getContentValues(mentor);

        mDatabase.update(MentorTable.NAME, values,
                MentorTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private MentorCursorWrapper queryMentors(String whereClause, String[] whereArgs, String groupBy,
                                             String orderBy) {
        Cursor cursor = mDatabase.query(
                MentorTable.NAME,
                null,
                whereClause,
                whereArgs,
                groupBy,
                null,
                orderBy
        );

        return new MentorCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Mentor mentor) {
        ContentValues values = new ContentValues();
        values.put(MentorTable.Cols.UUID, mentor.getMentorId().toString());
        values.put(MentorTable.Cols.NAME, mentor.getName());
        values.put(MentorTable.Cols.PHONE, mentor.getPhoneNumber());
        values.put(MentorTable.Cols.EMAIL, mentor.getEmailAddress());

        return values;
    }
}
