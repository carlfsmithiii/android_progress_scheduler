package edu.wgu.csmi508.progressscheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class NoteListActivity extends SingleFragmentActivity {

    private static final String EXTRA_COURSE_NOTE_LIST =
            "edu.wgu.csmi508.progressscheduler.course_note_list";

    public static Intent newIntent(Context packageContext, UUID courseUUID) {
        Intent intent = new Intent(packageContext, NoteListActivity.class);
        intent.putExtra(EXTRA_COURSE_NOTE_LIST, courseUUID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID courseId = (UUID) getIntent().getSerializableExtra(EXTRA_COURSE_NOTE_LIST);
        NoteListFragment fragment = NoteListFragment.newInstance(courseId);
        return fragment;
    }
}
