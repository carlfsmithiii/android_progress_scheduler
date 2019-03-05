package edu.wgu.csmi508.progressscheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class PictureActivity extends SingleFragmentActivity {
    private static final String EXTRA_COURSE_PICTURE =
            "edu.wgu.csmi508.progressscheduler.course_picture";

    public static Intent newIntent(Context packageContext, UUID courseId) {
        Intent intent = new Intent(packageContext, PictureActivity.class);
        intent.putExtra(EXTRA_COURSE_PICTURE, courseId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID courseId = (UUID) getIntent().getSerializableExtra(EXTRA_COURSE_PICTURE);
        PictureFragment fragment = PictureFragment.newInstance(courseId);
        return fragment;
    }
}
