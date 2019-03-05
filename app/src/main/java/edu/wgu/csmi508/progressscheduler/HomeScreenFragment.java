package edu.wgu.csmi508.progressscheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.database.DbBaseHelper;
import edu.wgu.csmi508.progressscheduler.database.DbSchema;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.AssessmentTable;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.CourseTable;

public class HomeScreenFragment extends Fragment {

    private Button mTermsButton;
    private Button mCoursesButton;
    private Button mMentorsButton;
    private Button mAssessmentsButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);

        mTermsButton = v.findViewById(R.id.terms_button);
        mTermsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TermListActivity.class);
                startActivity(intent);
            }
        });

        mCoursesButton = v.findViewById(R.id.courses_button);
        mCoursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CourseListActivity.class);
                startActivity(intent);
            }
        });

        mMentorsButton = v.findViewById(R.id.mentors_button);
        mMentorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MentorListActivity.class);
                startActivity(intent);
            }
        });

        mAssessmentsButton = v.findViewById(R.id.assessments_button);
        mAssessmentsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AssessmentListActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startDates();
        endDates();
        assessments();
    }

    private void startDates() {
        SQLiteDatabase db = new DbBaseHelper(getActivity()).getReadableDatabase();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(format.format(new Date()));
        } catch (ParseException ex ) {}
        String select = "SELECT " + CourseTable.Cols.UUID + " FROM " + CourseTable.NAME +
                " WHERE " + CourseTable.Cols.START_DATE + " = " + date.getTime() + " AND " +
                CourseTable.Cols.START_ALARM + " =1";
        List<String> coursesStartingToday = new ArrayList<>();
        Cursor cursor = db.rawQuery(select, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                coursesStartingToday.add("" + cursor.getString(cursor.getColumnIndex(CourseTable.Cols.UUID)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        for (String uuid : coursesStartingToday) {

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setTitle("Alert");
            dialog.setMessage(CourseList.getCourseList(getActivity()).getCourse(UUID.fromString(uuid)).toString() + " starts today!");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();
        }
    }

    private void endDates() {
        SQLiteDatabase db = new DbBaseHelper(getActivity()).getReadableDatabase();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(format.format(new Date()));
        } catch (ParseException ex ) {}
        String select = "SELECT " + CourseTable.Cols.UUID + " FROM " + CourseTable.NAME +
                " WHERE " + CourseTable.Cols.END_DATE + " = " + date.getTime() + " AND " +
                CourseTable.Cols.END_ALARM + " =1";
        List<String> coursesEndingToday = new ArrayList<>();
        Cursor cursor = db.rawQuery(select, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                coursesEndingToday.add("" + cursor.getString(cursor.getColumnIndex(CourseTable.Cols.UUID)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        for (String uuid : coursesEndingToday) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setTitle("Alert");
            dialog.setMessage(CourseList.getCourseList(getActivity()).getCourse(UUID.fromString(uuid)).toString() + " ends today!");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();
        }
    }

    private void assessments() {
        SQLiteDatabase db = new DbBaseHelper(getActivity()).getReadableDatabase();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(format.format(new Date()));
        } catch (ParseException ex ) {}
        String select = "SELECT " + AssessmentTable.Cols.ASSESSMENT_UUID + " FROM " + AssessmentTable.NAME +
                " WHERE " + AssessmentTable.Cols.DATE + " = " + date.getTime() + " AND " +
                AssessmentTable.Cols.ALARM + " =1";
        List<String> assessmentsToday = new ArrayList<>();
        Cursor cursor = db.rawQuery(select, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                assessmentsToday.add("" + cursor.getString(cursor.getColumnIndex(AssessmentTable.Cols.ASSESSMENT_UUID)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        for (String uuid : assessmentsToday) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setTitle("Alert");
            dialog.setMessage(AssessmentList.getAssessmentList(getActivity()).getAssessment(UUID.fromString(uuid)).toString() + " today!");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();
        }
    }
}
