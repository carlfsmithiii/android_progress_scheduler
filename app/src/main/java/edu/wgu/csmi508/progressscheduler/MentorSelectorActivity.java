package edu.wgu.csmi508.progressscheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class MentorSelectorActivity extends SingleFragmentActivity {
    private static final String EXTRA_COURSE_MENTOR_LIST_ID =
            "edu.wgu.csmi508.progressscheduler.course_mentor_list_id";

    public static Intent newIntent(Context packageContext, UUID courseId) {
        Intent intent = new Intent(packageContext, MentorSelectorActivity.class);
        intent.putExtra(EXTRA_COURSE_MENTOR_LIST_ID, courseId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID courseId = (UUID) getIntent().getSerializableExtra(EXTRA_COURSE_MENTOR_LIST_ID);
        MentorSelectorFragment fragment = MentorSelectorFragment.newInstance(courseId);
        return fragment;
    }
}
