package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class NoteEditActivity extends SingleFragmentActivity {
    private static final String EXTRA_NOTE_ID =
            "edu.wgu.csmi508.progressscheduler.note_id";

    public static Intent newIntent(Context packageContext, UUID noteUUID) {
        Intent intent = new Intent(packageContext, NoteEditActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteUUID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        NoteEditFragment fragment = NoteEditFragment.newInstance(noteId);
        return fragment;
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }
}
