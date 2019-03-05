package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class TermEditActivity extends SingleFragmentActivity {
    private static final String EXTRA_TERM_ID =
            "edu.wgu.csmi508.progressscheduler.term_id";

    public static Intent newIntent(Context packageContext, UUID termId) {
        Intent intent = new Intent(packageContext, TermEditActivity.class);
        intent.putExtra(EXTRA_TERM_ID, termId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID termId = (UUID) getIntent().getSerializableExtra(EXTRA_TERM_ID);
        TermEditFragment fragment = TermEditFragment.newInstance(termId);
        return fragment;
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK, null);
        super.onBackPressed();
    }
}
