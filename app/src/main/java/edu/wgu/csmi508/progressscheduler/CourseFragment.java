package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CourseFragment extends Fragment {

    private static final String ARG_COURSE_ID = "course_id";

    private static final int REQUEST_EDIT_COURSE = 0;

    private Course mCourse;
    private TextView mCourseTitleTextView;
    private TextView mStartDateTextView;
    private TextView mEndDateTextView;
    private TextView mStatusTextView;
    private ListView mAssessmentListView;
    private ListView mMentorListView;
    private Button mNotesButton;
    private Button mPicturesButton;
    private List<Mentor> mMentorList;
    private List<Assessment> mAssessmentList;

    public static CourseFragment newInstance(UUID courseId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE_ID, courseId);

        CourseFragment fragment = new CourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID courseId = (UUID) getArguments().getSerializable(ARG_COURSE_ID);
        mCourse = CourseList.getCourseList(getActivity()).getCourse(courseId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        mCourseTitleTextView = view.findViewById(R.id.course_title_textview);
        mCourseTitleTextView.setText(mCourse.getCourseCode() + "    " + mCourse.getTitle());

        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");

        mStartDateTextView = view.findViewById(R.id.course_start_date_textview);
        if (mCourse.getStartDate() != null && !mCourse.getStartDate().equals(new Date(0))) {
            mStartDateTextView.setText("Start Date: " + df.format(mCourse.getStartDate()));
        } else {
            mStartDateTextView.setText("Start Date: Not Yet Specified");
        }
        mEndDateTextView = view.findViewById(R.id.course_end_date_textview);
        if (mCourse.getProjectedEndDate() != null && !mCourse.getProjectedEndDate().equals(new Date(0))) {
            mEndDateTextView.setText("End Date: " + df.format(mCourse.getProjectedEndDate()));
        } else {
            mEndDateTextView.setText("End Date: Not Yet Specified");
        }
        mStatusTextView = view.findViewById(R.id.course_status_textview);
        mStatusTextView.setText("Status: " + mCourse.getStatus().getStatusString());

        mAssessmentListView = view.findViewById(R.id.assessment_listview);
        mAssessmentList = mCourse.getAssessmentList();
        ArrayAdapter<Assessment> assessmentAdapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_text_view, mAssessmentList);
        mAssessmentListView.setAdapter(assessmentAdapter);

        mMentorListView = view.findViewById(R.id.mentors_listview);
        mMentorList = mCourse.getMentorList();
        final ArrayAdapter<Mentor> mentorAdapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_text_view, mMentorList);
        mMentorListView.setAdapter(mentorAdapter);


        mNotesButton = view.findViewById(R.id.notes_button);
        mNotesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = NoteListActivity.newIntent(getActivity(), mCourse.getCourseId());
                startActivity(intent);
            }
        });

        mPicturesButton = view.findViewById(R.id.pictures_button);
        final Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager pManager = getActivity().getPackageManager();
        if (takePhoto.resolveActivity(pManager) == null) {
            mPicturesButton.setEnabled(false);
        }
        mPicturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PictureActivity.newIntent(getActivity(), mCourse.getCourseId());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_course, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_course:
                Intent intent = CourseEditActivity.newIntent(getActivity(), mCourse.getCourseId());
                startActivityForResult(intent, REQUEST_EDIT_COURSE);
                return true;
            case R.id.delete_course:
                CourseList courseList = CourseList.getCourseList(getActivity());
                courseList.removeCourse(mCourse);
                getActivity().onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
