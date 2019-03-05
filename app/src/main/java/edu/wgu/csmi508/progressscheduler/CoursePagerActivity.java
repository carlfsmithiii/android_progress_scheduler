package edu.wgu.csmi508.progressscheduler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class CoursePagerActivity extends AppCompatActivity {

    private static final String EXTRA_COURSE_ID =
            "edu.wgu.csmi508.progressscheduler.course_id";

    private ViewPager mViewPager;
    private List<Course> mCourses;

    private MAdapter mAdapter;

    public static Intent newIntent(Context packageContext, UUID courseId) {
        Intent intent = new Intent(packageContext, CoursePagerActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        UUID courseId = (UUID) getIntent().getSerializableExtra(EXTRA_COURSE_ID);

        mViewPager = findViewById(R.id.view_pager);

        mCourses = CourseList.getCourseList(this).getCourseList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mAdapter = new MAdapter(fragmentManager);
        mViewPager.setAdapter(mAdapter);

        for (int i = 0; i < mCourses.size(); i++) {
            if (mCourses.get(i).getCourseId().equals(courseId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAdapter.notifyDataSetChanged();
    }

    private class MAdapter extends FragmentStatePagerAdapter {
        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        public MAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Course course = mCourses.get(position);
            return CourseFragment.newInstance(course.getCourseId());
        }

        @Override
        public int getCount() {
            return mCourses.size();
        }
    }
}

