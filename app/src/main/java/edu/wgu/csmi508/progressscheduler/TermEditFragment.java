package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.database.DbBaseHelper;
import edu.wgu.csmi508.progressscheduler.database.DbSchema;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.TermCourseTable;

public class TermEditFragment extends Fragment {

//    private static final String DIALOG_TERM_NUMBER = "DialogTermNumber";
    private static final String DIALOG_TERM_START_DATE = "DialogTermStartDate";

//    private static final int REQUEST_TERM_NUMBER = 0;
    private static final int REQUEST_TERM_START_DATE = 1;

    private static final String ARG_TERM_ID = "term_id";


    private Term mTerm;
    private EditText mTermNameEditText;
    private TextView mTermNumberLabel;
    private TextView mStartDateTextView;
    private TextView mEndDateTextView;
    private ListView mCourseListView;
    private List<Course> mTermCourseList = new ArrayList<>();

    public static TermEditFragment newInstance(UUID termId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TERM_ID, termId);

        TermEditFragment fragment = new TermEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID termId = (UUID) getArguments().getSerializable(ARG_TERM_ID);
        mTerm = TermList.getTermList(getActivity()).getTerm(termId);
    }

    @Override
    public void onPause() {
        super.onPause();

        TermList.getTermList(getActivity()).updateTerm(mTerm);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_term_edit, container, false);


        mTermNameEditText = view.findViewById(R.id.term_name_edit_text);
        mTermNameEditText.setText(mTerm.getTermName());
        mTermNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // empty
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTerm.setTermName(s.toString());
                TermList.getTermList(getActivity()).updateTerm(mTerm);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // empty
            }
        });

        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");

        mStartDateTextView = view.findViewById(R.id.term_start_date_edit_textview);
        if (mTerm.getTermStartDate() != null && !mTerm.getTermStartDate().equals(new Date(0))) {
            mStartDateTextView.setText(df.format(mTerm.getTermStartDate()));
            System.out.println(mTerm.getTermStartDate());
            System.out.println(df.format(mTerm.getTermStartDate()));
        } else {
            mStartDateTextView.setText("Click to select start date");
        }
        mStartDateTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();

                DatePickerFragment dialog = new DatePickerFragment();
                if (mTerm.getTermStartDate() != null && !mTerm.getTermStartDate().equals(new Date(0))) {
                    dialog = DatePickerFragment.newInstance(mTerm.getTermStartDate());
                } else {
                    dialog = DatePickerFragment.newInstance(new Date());
                }
                dialog.setTargetFragment(TermEditFragment.this, REQUEST_TERM_START_DATE);
                dialog.show(manager, DIALOG_TERM_START_DATE);
            }
        });

        mEndDateTextView = view.findViewById(R.id.term_end_date_edit_textview);
        if (mTerm.getTermEndDate() != null && !mTerm.getTermStartDate().equals(new Date(0))) {
            mEndDateTextView.setText(df.format(mTerm.getTermEndDate()));
        } else {
            mEndDateTextView.setText("The end date will automatically populate");
        }
        mEndDateTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "Please select a term start date.\nThe end date will adjust automatically",
                        Toast.LENGTH_LONG).show();

                FragmentManager manager = getFragmentManager();

                DatePickerFragment dialog = new DatePickerFragment();
                if (mTerm.getTermStartDate() != null && !mTerm.getTermStartDate().equals(new Date(0))) {
                    dialog = DatePickerFragment.newInstance(mTerm.getTermStartDate());
                } else {
                    dialog = DatePickerFragment.newInstance(new Date());
                }
                dialog.setTargetFragment(TermEditFragment.this, REQUEST_TERM_START_DATE);
                dialog.show(manager, DIALOG_TERM_START_DATE);
            }
        });

        mCourseListView = view.findViewById(R.id.term_course_edit_list_view);
        final List<Course> fullCourseList = CourseList.getCourseList(getActivity()).getCourseList();
        mTermCourseList = mTerm.getTermCourseList(getActivity());
        ArrayAdapter<Course> adapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_checked_text_view, fullCourseList);
        mCourseListView.setAdapter(adapter);
        mCourseListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        checkThisTermsCourses(mTermCourseList, fullCourseList);
        mCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView selectedItem = (CheckedTextView) view;
                boolean isChecked = selectedItem.isChecked();
                Course toggledCourse = fullCourseList.get(position);
                if (isChecked) {
                    mTermCourseList.add(toggledCourse);
                    SQLiteDatabase db = new DbBaseHelper(getActivity()).getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(TermCourseTable.Cols.TERM, mTerm.getTermId().toString());
                    values.put(TermCourseTable.Cols.COURSE, toggledCourse.getCourseId().toString());
                    db.insert(TermCourseTable.NAME, null, values);
                    db.close();
                } else {
                    mTermCourseList.remove(toggledCourse);
                    SQLiteDatabase db = new DbBaseHelper(getActivity()).getWritableDatabase();
                    String courseId = toggledCourse.getCourseId().toString();
                    String termId = mTerm.getTermId().toString();
                    db.delete(TermCourseTable.NAME, TermCourseTable.Cols.TERM + "=? and " + TermCourseTable.Cols.COURSE + "=?", new String[]{termId, courseId});
                    db.close();
                }
            }
        });


        return view;
    }

    private void checkThisTermsCourses(List<Course> thisTermsCourses, List<Course> fullCourseList) {
        for (Course course : thisTermsCourses) {
            UUID courseId = course.getCourseId();
            for (int i = 0; i < fullCourseList.size(); i++) {
                if (fullCourseList.get(i).getCourseId().equals(courseId)) {
                    mCourseListView.setItemChecked(i, true);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_TERM_START_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            Date termStartDate = calendar.getTime();
//            mTerm.setTermStartDate(termStartDate);
            mTerm.setTermStartDate(date);
            TermList.getTermList(getActivity()).updateTerm(mTerm);
            SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");
            mStartDateTextView.setText(df.format(mTerm.getTermStartDate()));
            mEndDateTextView.setText(df.format(mTerm.getTermEndDate()));
        }
    }



}
