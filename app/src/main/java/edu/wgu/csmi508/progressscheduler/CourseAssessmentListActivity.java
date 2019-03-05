package edu.wgu.csmi508.progressscheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CourseAssessmentListActivity extends SingleFragmentActivity {
    private static final String EXTRA_COURSE_ASSESSMENT_LIST_COURSE_ID =
            "edu.wgu.csmi508.progressscheduler.course_assessment_list_course_id";

    public static Intent newIntent(Context packageContext, UUID courseId) {
        Intent intent = new Intent(packageContext, CourseAssessmentListActivity.class);
        intent.putExtra(EXTRA_COURSE_ASSESSMENT_LIST_COURSE_ID, courseId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID courseId = (UUID) getIntent().getSerializableExtra(EXTRA_COURSE_ASSESSMENT_LIST_COURSE_ID);
        CourseAssessmentListFragment fragment = CourseAssessmentListFragment.newInstance(courseId);
        return fragment;
    }

}
