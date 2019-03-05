package edu.wgu.csmi508.progressscheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CourseAssessmentActivity extends SingleFragmentActivity {
    private static final String EXTRA_COURSE_ASSESSMENT_ASSESSMENT_ID =
            "edu.wgu.csmi508.progressscheduler.course_assessment_assessment_id";

    public static Intent newIntent(Context packageContext, UUID assessmentId) {
        Intent intent = new Intent(packageContext, CourseAssessmentActivity.class);
        intent.putExtra(EXTRA_COURSE_ASSESSMENT_ASSESSMENT_ID, assessmentId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID assessmentId = (UUID) getIntent().getSerializableExtra(EXTRA_COURSE_ASSESSMENT_ASSESSMENT_ID);
        CourseAssessmentFragment fragment = CourseAssessmentFragment.newInstance(assessmentId);
        return fragment;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
