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
import edu.wgu.csmi508.progressscheduler.database.NoteCursorWrapper;

public class NoteList {
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public NoteList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbBaseHelper(mContext).getWritableDatabase();
    }

    public void addNote(Note note) {
        ContentValues values = getContentValues(note);

        mDatabase.insert(DbSchema.NoteTable.NAME, null, values);
    }

    public void removeNote(Note note) {
        String uuidString = note.getNoteId().toString();

        mDatabase.delete(DbSchema.NoteTable.NAME,
                DbSchema.NoteTable.Cols.NOTE_UUID + " = ?",
                new String[] { uuidString });
    }

    public List<Note> getNoteList(UUID courseId) {
        List<Note> notes = new ArrayList<>();

        NoteCursorWrapper cursor = queryCourses(DbSchema.NoteTable.Cols.COURSE_UUID + " = ?" , new String[] {courseId.toString()}, null, DbSchema.NoteTable.Cols.TIMESTAMP + " ASC");

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return notes;
    }

    public Note getNote(UUID noteId) {
        NoteCursorWrapper cursor = queryCourses(
                DbSchema.NoteTable.Cols.NOTE_UUID + " = ?",
                new String[] {noteId.toString()},
                null,
                null
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNote();
        } finally {
            cursor.close();
        }
    }

    public void updateNote(Note note) {
        String uuidString = note.getNoteId().toString();
        ContentValues values = getContentValues(note);

        mDatabase.update(DbSchema.NoteTable.NAME, values,
                DbSchema.NoteTable.Cols.NOTE_UUID + " = ?",
                new String[] { uuidString });
    }

    private NoteCursorWrapper queryCourses(String whereClause, String[] whereArgs, String groupBy,
                                           String orderBy) {
        Cursor cursor = mDatabase.query(
                DbSchema.NoteTable.NAME,
                null,
                whereClause,
                whereArgs,
                groupBy,
                null,
                orderBy
        );

        return new NoteCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.NoteTable.Cols.NOTE_UUID, note.getNoteId().toString());
        values.put(DbSchema.NoteTable.Cols.COURSE_UUID, note.getCourseId().toString());
        values.put(DbSchema.NoteTable.Cols.NOTE, note.getNote());
        values.put(DbSchema.NoteTable.Cols.TIMESTAMP, note.getDate().getTime());

        return values;
    }
}
