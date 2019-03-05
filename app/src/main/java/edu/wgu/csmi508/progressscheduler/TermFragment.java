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
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TermFragment extends Fragment {
    private static final String ARG_TERM_ID = "term_id";
    private static final int REQUEST_EDIT_TERM = 0;


    private Term mTerm;
    private TextView mTermTitleTextView;
    private TextView mTermStartDateTextView;
    private TextView mTermEndDateTextView;
    private ListView mTermCourseListView;
    private List<Course> mCourses = new ArrayList<>();
    private ArrayAdapter mTermCourseListAdapter;

    public static TermFragment newInstance(UUID termId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TERM_ID, termId);

        TermFragment fragment = new TermFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID termId = (UUID) getArguments().getSerializable(ARG_TERM_ID);
        mTerm = TermList.getTermList(getActivity()).getTerm(termId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_term, container, false);

        mTermTitleTextView = view.findViewById(R.id.term_title_textview);
        mTermStartDateTextView = view.findViewById(R.id.term_start_date_textview);
        mTermEndDateTextView = view.findViewById(R.id.term_end_date_textview);
        mTermCourseListView = view.findViewById(R.id.term_course_list_view);
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");

        if (mTerm != null) {
            if (mTerm.getTermName() != null) {
                mTermTitleTextView.setText(mTerm.getTermName());
            }
            if (mTerm.getTermStartDate() != null && !mTerm.getTermStartDate().equals(new Date(0))) {
                mTermStartDateTextView.setText(df.format(mTerm.getTermStartDate())); //
            } else {
                mTermStartDateTextView.setText("Not Yet Selected");
            }
            if (mTerm.getTermEndDate() != null && !mTerm.getTermStartDate().equals(new Date(0))) {
                mTermEndDateTextView.setText(df.format(mTerm.getTermEndDate())); //
            } else {
                mTermEndDateTextView.setText("Not Yet Selected");
            }

            mCourses.addAll(mTerm.getTermCourseList(getActivity()));
            mTermCourseListAdapter = new ArrayAdapter<Course>(getActivity(), android.R.layout.simple_list_item_1, mCourses);

            mTermCourseListView.setAdapter(mTermCourseListAdapter);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_term, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete_term:
                if (mTermCourseListView.getAdapter().isEmpty()) {
                    TermList termList = TermList.getTermList(getActivity());
                    termList.removeTerm(mTerm);
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getActivity(), R.string.term_not_empty_toast,
                            Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.edit_term:
                Intent intent = TermEditActivity.newIntent(getActivity(), mTerm.getTermId());
                startActivityForResult(intent, REQUEST_EDIT_TERM);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_EDIT_TERM) {
            UUID termId = mTerm.getTermId();
            mTerm = TermList.getTermList(getActivity()).getTerm(termId);
            List<Course> courseList = mTerm.getTermCourseList(getActivity());
            mCourses.clear();
            mCourses.addAll(courseList);
            mTermCourseListAdapter.notifyDataSetChanged();

            SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy");

            if (mTerm.getTermName() != null) {
                mTermTitleTextView.setText(mTerm.getTermName());
            }
            if (mTerm.getTermStartDate() != null && !mTerm.getTermStartDate().equals(new Date(0))) {
                mTermStartDateTextView.setText(df.format(mTerm.getTermStartDate())); //
            } else {
                mTermStartDateTextView.setText("Not Yet Selected");
            }
            if (mTerm.getTermEndDate() != null && !mTerm.getTermStartDate().equals(new Date(0))) {
                mTermEndDateTextView.setText(df.format(mTerm.getTermEndDate())); //
            } else {
                mTermEndDateTextView.setText("Not Yet Selected");
            }

        }
    }
}
