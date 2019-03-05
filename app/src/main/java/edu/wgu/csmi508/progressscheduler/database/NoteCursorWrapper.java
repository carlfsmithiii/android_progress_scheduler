package edu.wgu.csmi508.progressscheduler.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.Note;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.NoteTable;

public class NoteCursorWrapper extends CursorWrapper {

    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String noteUuidString = getString(getColumnIndex(NoteTable.Cols.NOTE_UUID));
        String courseUuidString = getString(getColumnIndex(NoteTable.Cols.COURSE_UUID));
        String noteString = getString(getColumnIndex(NoteTable.Cols.NOTE));
        long timestamp = getLong(getColumnIndex(NoteTable.Cols.TIMESTAMP));

        Note note = new Note(UUID.fromString(courseUuidString), UUID.fromString(noteUuidString));
        note.setDate(new Date(timestamp));
        note.setNote(noteString);

        return note;
    }
}