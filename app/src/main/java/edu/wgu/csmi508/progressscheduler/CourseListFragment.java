package edu.wgu.csmi508.progressscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseListFragment extends Fragment {

    private RecyclerView mCourseRecyclerView;
    private CourseAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mCourseRecyclerView = view.findViewById(R.id.titled_recycler_view);
        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_course_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.new_course:
                Course course = new Course(getActivity());
                CourseList.getCourseList(getActivity()).addCourse(course);
                Intent intent = CourseEditActivity.newIntent(getActivity(), course.getCourseId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        CourseList courseList = CourseList.getCourseList(getActivity());
        List<Course> courses = courseList.getCourseList();

        mAdapter = new CourseAdapter(courses);
        mCourseRecyclerView.setAdapter(mAdapter);
    }

    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Course mCourse;
        private TextView mCourseCodeTextView;
        private TextView mCourseTitleTextView;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_course, parent, false));
            itemView.setOnClickListener(this);

            mCourseCodeTextView = itemView.findViewById(R.id.course_code_textview);
            mCourseTitleTextView = itemView.findViewById(R.id.course_title_textview);
        }

        public void bind(Course course) {
            mCourse = course;
            mCourseCodeTextView.setText(mCourse.getCourseCode());
            mCourseTitleTextView.setText(mCourse.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = CoursePagerActivity.newIntent(getActivity(), mCourse.getCourseId());
            startActivity(intent);
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
        private List<Course> mCourses;

        public CourseAdapter(List<Course> courses) {
            mCourses = courses;
        }

        @NonNull
        @Override
        public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CourseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
            Course course = mCourses.get(position);
            holder.bind(course);
        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }
    }
}
