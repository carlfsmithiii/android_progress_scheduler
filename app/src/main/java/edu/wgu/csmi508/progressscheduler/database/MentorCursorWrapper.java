package edu.wgu.csmi508.progressscheduler.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.Mentor;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.MentorTable;

public class MentorCursorWrapper extends CursorWrapper {
    public MentorCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Mentor getMentor() {
        String uuidString = getString(getColumnIndex(MentorTable.Cols.UUID));
        String mentorName = getString(getColumnIndex(MentorTable.Cols.NAME));
        String phone = getString(getColumnIndex(MentorTable.Cols.PHONE));
        String email = getString(getColumnIndex(MentorTable.Cols.EMAIL));

        Mentor mentor = new Mentor(UUID.fromString(uuidString));
        mentor.setName(mentorName);
        mentor.setPhoneNumber(phone);
        mentor.setEmailAddress(email);

        return mentor;
    }
}
