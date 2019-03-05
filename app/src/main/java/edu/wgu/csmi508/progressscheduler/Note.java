package edu.wgu.csmi508.progressscheduler;

import java.util.Date;
import java.util.UUID;

public class Note {

    private UUID mNoteId;
    private UUID mCourseId;
    private Date mDate;
    private String mNote;

    public Note(UUID courseId) {
        this(courseId, UUID.randomUUID());
    }

    public Note(UUID courseId, UUID noteId) {
        mCourseId = courseId;
        mNoteId = noteId;
        mDate = new Date();
        mNote = null;
    }

    public UUID getNoteId() {
        return mNoteId;
    }

    public UUID getCourseId() {
        return mCourseId;
    }

    public Date getDate() {
        return mDate;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    @Override
    public String toString() {
        return mNote;
    }
}
