package edu.wgu.csmi508.progressscheduler.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.Term;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.TermTable;

public class TermCursorWrapper extends CursorWrapper {
    public TermCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Term getTerm() {
        String uuidString = getString(getColumnIndex(TermTable.Cols.UUID));
        String termName = getString(getColumnIndex(TermTable.Cols.TERM_NAME));
        long startDate = getLong(getColumnIndex(TermTable.Cols.START_DATE));
//        long endDate = getLong(getColumnIndex(TermTable.Cols.END_DATE));

        Term term = new Term(UUID.fromString(uuidString));
        term.setTermName(termName);
        term.setTermStartDate(new Date(startDate));

        return term;
    }
}