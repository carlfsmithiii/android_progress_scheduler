package edu.wgu.csmi508.progressscheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CourseEditActivity extends SingleFragmentActivity {

    private static final String EXTRA_COURSE_ID =
            "edu.wgu.csmi508.progressscheduler.course_id";

    public static Intent newIntent(Context packageContext, UUID courseId) {
        Intent intent = new Intent(packageContext, CourseEditActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID courseId = (UUID) getIntent().getSerializableExtra(EXTRA_COURSE_ID);
        CourseEditFragment fragment = CourseEditFragment.newInstance(courseId);
        return fragment;
    }
}
