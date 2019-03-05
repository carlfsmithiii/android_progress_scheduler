package edu.wgu.csmi508.progressscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.database.DbBaseHelper;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.TermTable;
import edu.wgu.csmi508.progressscheduler.database.TermCursorWrapper;

// Singleton
public class TermList {
    private static TermList sTermList;

    private Context mContext;
    private SQLiteDatabase mDatabase;



    public static TermList getTermList(Context context) {
        if (sTermList == null) {
            sTermList = new TermList(context);
        }
        return sTermList;
    }

    private TermList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbBaseHelper(mContext).getWritableDatabase();
    }

    public void addTerm(Term term) {
        ContentValues values = getContentValues(term);

        mDatabase.insert(TermTable.NAME, null, values);
    }

    public void removeTerm(Term term) {
        String uuidString = term.getTermId().toString();

        mDatabase.delete(TermTable.NAME,
                TermTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public List<Term> getList() {
        List<Term> terms = new ArrayList<>();

        TermCursorWrapper cursor = queryTerms(null, null, null, null + " ASC");


        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                terms.add(cursor.getTerm());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return terms;
    }

    public Term getTerm(UUID termId) {
        TermCursorWrapper cursor = queryTerms(
                TermTable.Cols.UUID + " = ?",
                new String[] {termId.toString()},
                null,
                null
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTerm();
        } finally {
            cursor.close();
        }
    }



    public void updateTerm(Term term) {
        String uuidString = term.getTermId().toString();
        ContentValues values = getContentValues(term);

        mDatabase.update(TermTable.NAME, values,
                TermTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private TermCursorWrapper queryTerms(String whereClause, String[] whereArgs, String groupBy,
                                         String orderBy) {
        Cursor cursor = mDatabase.query(
                TermTable.NAME,
                null,
                whereClause,
                whereArgs,
                groupBy,
                null,
                orderBy
        );

        return new TermCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Term term) {
        ContentValues values = new ContentValues();
        values.put(TermTable.Cols.UUID, term.getTermId().toString());
        if (term.getTermName() != null) {
            values.put(TermTable.Cols.TERM_NAME, term.getTermName());
        } else {
            values.put(TermTable.Cols.TERM_NAME, "Term");
        }
        if (term.getTermStartDate() != null) {
            values.put(TermTable.Cols.START_DATE, term.getTermStartDate().getTime());
            values.put(TermTable.Cols.END_DATE, term.getTermEndDate().getTime());
        } else {
            values.put(TermTable.Cols.START_DATE, new Date(0).getTime());
            values.put(TermTable.Cols.END_DATE, new Date(0).getTime());
        }

        return values;
    }
}
