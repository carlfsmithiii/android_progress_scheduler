package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CourseEditFragment extends Fragment {

    private static final String ARG_COURSE_ID = "course_id";
    private static final String DIALOG_START_DATE = "DialogStartDate";
    private static final String DIALOG_END_DATE = "DialogEndDate";

    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;
    private static final int REQUEST_EDIT_MENTOR = 2;
    private static final int REQUEST_EDIT_ASSESSMENTS = 3;

    private Course mCourse;

    private EditText mCourseCodeEditText;
    private EditText mCourseTitleEditText;
    private Spinner mStatusSpinner;
    private Button mStartDateButton;
    private Button mEndDateButton;
    private Button mAssessmentsButton;
    private Button mMentorsButton;
    private CheckBox mStartDateAlarm;
    private CheckBox mEndDateAlarm;

    public static CourseEditFragment newInstance(UUID courseId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE_ID, courseId);

        CourseEditFragment fragment = new CourseEditFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID courseId = (UUID) getArguments().getSerializable(ARG_COURSE_ID);
        mCourse = CourseList.getCourseList(getActivity()).getCourse(courseId);
    }

    @Override
    public void onPause() {
        super.onPause();

        CourseList.getCourseList(getActivity()).updateCourse(mCourse);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_editor, container, false);

        mCourseCodeEditText = view.findViewById(R.id.course_code_edittext);
        if (mCourse.getCourseCode() != null) {
            mCourseCodeEditText.setText(mCourse.getCourseCode());
        }
        mCourseCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCourse.setCourseCode(s.toString());
                CourseList.getCourseList(getActivity()).updateCourse(mCourse);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // blank
            }
        });

        mCourseTitleEditText = view.findViewById(R.id.course_title_edittext);
        if (mCourse.getTitle() != null) {
            mCourseTitleEditText.setText(mCourse.getTitle());
        }
        mCourseTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCourse.setTitle(s.toString());
                CourseList.getCourseList(getActivity()).updateCourse(mCourse);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // blank
            }
        });

        mStatusSpinner = view.findViewById(R.id.status_spinner);
        mStatusSpinner.setAdapter(new ArrayAdapter<Course.Status>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Course.Status.values()));
        mStatusSpinner.setSelection(mCourse.getStatus().ordinal());
        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCourse.setStatus(Course.Status.values()[position]);
                CourseList.getCourseList(getActivity()).updateCourse(mCourse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");

        mStartDateButton = view.findViewById(R.id.start_date_button);
        if (mCourse.getStartDate() != null && mCourse.getStartDate().after(new Date(0))) {
            mStartDateButton.setText(df.format(mCourse.getStartDate()));
        } else {
            mStartDateButton.setText("Start Date");
        }
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment();
                if (mCourse.getStartDate() != null && !mCourse.getStartDate().equals(new Date(0))) {
                    dialog = DatePickerFragment.newInstance(mCourse.getStartDate());
                } else {
                    dialog = DatePickerFragment.newInstance(new Date());
                }
                dialog.setTargetFragment(CourseEditFragment.this, REQUEST_START_DATE);
                dialog.show(manager, DIALOG_START_DATE);
            }
        });

        mEndDateButton = view.findViewById(R.id.end_date_button);
        if (mCourse.getProjectedEndDate() != null && mCourse.getProjectedEndDate().after(new Date(0))) {
            mEndDateButton.setText(df.format(mCourse.getProjectedEndDate()));
        } else {
            mEndDateButton.setText("End Date");
        }
        mEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment();
                if (mCourse.getProjectedEndDate() != null && !mCourse.getProjectedEndDate().equals(new Date(0))) {
                    dialog = DatePickerFragment.newInstance(mCourse.getProjectedEndDate());
                } else {
                    dialog = DatePickerFragment.newInstance(new Date());
                }
                dialog.setTargetFragment(CourseEditFragment.this, REQUEST_END_DATE);
                dialog.show(manager, DIALOG_END_DATE);
            }
        });

        mStartDateAlarm = view.findViewById(R.id.start_date_alarm);
        mStartDateAlarm.setChecked(mCourse.isStartAlarm());
        mStartDateAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mStartDateAlarm.isChecked()) {
                    mCourse.setStartAlarm(true);
                } else {
                    mCourse.setStartAlarm(false);
                }
                CourseList.getCourseList(getActivity()).updateCourse(mCourse);
            }
        });

        mEndDateAlarm = view.findViewById(R.id.end_date_alarm);
        mEndDateAlarm.setChecked(mCourse.isEndAlarm());
        mEndDateAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mEndDateAlarm.isChecked()) {
                    mCourse.setEndAlarm(true);
                } else {
                    mCourse.setEndAlarm(false);
                }
                CourseList.getCourseList(getActivity()).updateCourse(mCourse);
            }
        });

        mAssessmentsButton = view.findViewById(R.id.view_assessments_button);
        mAssessmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CourseAssessmentListActivity.newIntent(getActivity(), mCourse.getCourseId());
                startActivityForResult(intent, REQUEST_EDIT_ASSESSMENTS);
            }
        });

        mMentorsButton = view.findViewById(R.id.view_mentors_button);
        mMentorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MentorSelectorActivity.newIntent(getActivity(), mCourse.getCourseId());
                startActivityForResult(intent, REQUEST_EDIT_MENTOR);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_START_DATE) {
            SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // SET THE COURSE START DATE
            try {
                date = df.parse(df.format(date));
            } catch (ParseException ex ) {}
            mCourse.setStartDate(date);
            CourseList.getCourseList(getActivity()).updateCourse(mCourse);
            mStartDateButton.setText(df.format(date));
        } else if (requestCode == REQUEST_END_DATE) {
            SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // SET THE COURSE END DATE
            try {
                date = df.parse(df.format(date));
            } catch (ParseException ex ) {}
            mCourse.setProjectedEndDate(date);
            CourseList.getCourseList(getActivity()).updateCourse(mCourse);
            mEndDateButton.setText(df.format(date));
        }
    }
}
