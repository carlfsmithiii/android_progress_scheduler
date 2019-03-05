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
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.Assessment.AssessmentType;

public class CourseAssessmentFragment extends Fragment {

    private static final String ARG_ASSESSMENT_ID = "assessment_id";

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;


    private Course mCourse;
    private Assessment mAssessment;
    private UUID mAssessmentId;

    private TextView mTitleEditText;
    private RadioGroup mAssessmentTypeRadioGroup;
    private RadioButton mObjectiveRadioButton;
    private RadioButton mPerformanceRadioButton;
    private CheckBox mAlertCheckBox;
    private TextView mGoalDateTextView;



    public static CourseAssessmentFragment newInstance(UUID assessmentId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ASSESSMENT_ID, assessmentId);

        CourseAssessmentFragment fragment = new CourseAssessmentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAssessmentId = (UUID) getArguments().getSerializable(ARG_ASSESSMENT_ID);
        mAssessment = AssessmentList.getAssessmentList(getActivity()).getAssessment(mAssessmentId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_assessment, container, false);

        mTitleEditText = view.findViewById(R.id.assessment_title_edit_text);
        mAssessmentTypeRadioGroup = view.findViewById(R.id.assessment_type_radio_group);
        mObjectiveRadioButton = view.findViewById(R.id.objective_radio_button);
        mPerformanceRadioButton = view.findViewById(R.id.performance_radio_button);
        mAlertCheckBox = view.findViewById(R.id.assessment_alert_checked_text_view);
        mGoalDateTextView = view.findViewById(R.id.assessment_date_text_view);

        if (mAssessment.getAssessmentName() != null) {
            mTitleEditText.setText(mAssessment.getAssessmentName());
        }
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAssessment.setAssessmentName(s.toString());
                AssessmentList.getAssessmentList(getActivity()).updateAssessment(mAssessment);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        AssessmentType type = mAssessment.getAssessmentType();
        switch(type) {
            case OBJECTIVE:
                mObjectiveRadioButton.toggle();
                break;
            case PERFORMANCE:
                mPerformanceRadioButton.toggle();
                break;
        }
        mObjectiveRadioButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAssessment.setAssessmentType(AssessmentType.OBJECTIVE);
                AssessmentList.getAssessmentList(getActivity()).updateAssessment(mAssessment);
            }
        });
        mPerformanceRadioButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAssessment.setAssessmentType(AssessmentType.PERFORMANCE);
                AssessmentList.getAssessmentList(getActivity()).updateAssessment(mAssessment);
            }
        });


        final SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");
        if (mAssessment.getGoalDate() != null && !mAssessment.getGoalDate().equals(new Date(0))) {
            mGoalDateTextView.setText(df.format(mAssessment.getGoalDate()));
        }

        mAlertCheckBox.setChecked(mAssessment.isGoalAlert());
        mAlertCheckBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAssessment.setGoalAlert(mAlertCheckBox.isChecked());
                AssessmentList.getAssessmentList(getActivity()).updateAssessment(mAssessment);
            }
        });

        mGoalDateTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment();
                Date date = new Date(0);
                try {
                    df.parse(mGoalDateTextView.getText().toString());
                } catch (ParseException ex) {}
                if (date.after(new Date(0))) {
                    dialog = DatePickerFragment.newInstance(date);
                }
                dialog.setTargetFragment(CourseAssessmentFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");
            try {
                date = df.parse(df.format(date));
            } catch (ParseException ex) {}
            mGoalDateTextView.setText(df.format(date));
            mAssessment.setGoalDate(date);
            AssessmentList.getAssessmentList(getActivity()).updateAssessment(mAssessment);
        }
    }

}
