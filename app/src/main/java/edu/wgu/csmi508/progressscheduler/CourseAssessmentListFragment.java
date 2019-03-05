package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;

import java.util.List;
import java.util.UUID;

public class CourseAssessmentListFragment extends Fragment {

    private static final int REQUEST_EDIT_ASSESSMENT = 0;
    private static final int REQUEST_NEW_ASSESSMENT = 1;

    private static final String ARG_COURSE_ID = "course_id";

    private ListView mListView;
    private List<Assessment> mAssessmentList;
    private UUID mCourseId;
    private ArrayAdapter mAdapter;

    public static CourseAssessmentListFragment newInstance(UUID courseId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE_ID, courseId);

        CourseAssessmentListFragment fragment = new CourseAssessmentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCourseId = (UUID) getArguments().getSerializable(ARG_COURSE_ID);
        mAssessmentList = AssessmentList.getAssessmentList(getActivity()).getCourseAssessmentList(mCourseId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = view.findViewById(R.id.list_view);

        mAdapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_text_view, mAssessmentList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assessment assessment = mAssessmentList.get(position);
                Intent intent = CourseAssessmentActivity.newIntent(getActivity(), assessment.getAssessmentId());
                startActivityForResult(intent, REQUEST_EDIT_ASSESSMENT);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_EDIT_ASSESSMENT || requestCode == REQUEST_NEW_ASSESSMENT) {
            List<Assessment> assessmentList = AssessmentList.getAssessmentList(getActivity()).getCourseAssessmentList(mCourseId);
            mAssessmentList.clear();
            mAssessmentList.addAll(assessmentList);
//            mAdapter.notifyDataSetChanged();
            mAdapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_text_view, mAssessmentList);
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_course_assessment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.new_assessment:
                Assessment assessment = new Assessment(getActivity(), mCourseId);
                AssessmentList.getAssessmentList(getActivity()).addAssessment(assessment);

                Intent intent = CourseAssessmentActivity.newIntent(getActivity(), assessment.getAssessmentId());
                startActivityForResult(intent, REQUEST_NEW_ASSESSMENT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
