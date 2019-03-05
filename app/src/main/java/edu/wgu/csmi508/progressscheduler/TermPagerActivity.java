package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class TermPagerActivity extends AppCompatActivity {

    private static final String EXTRA_TERM_ID =
            "edu.wgu.csmi508.progressscheduler.term_id";

    private ViewPager mViewPager;
    private List<Term> mTerms;

    public static Intent newIntent(Context packageContext, UUID termId) {
        Intent intent = new Intent(packageContext, TermPagerActivity.class);
        intent.putExtra(EXTRA_TERM_ID, termId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        UUID termId = (UUID) getIntent().getSerializableExtra(EXTRA_TERM_ID);

        mViewPager = findViewById(R.id.view_pager);

        mTerms = TermList.getTermList(this).getList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Term term = mTerms.get(position);
                return TermFragment.newInstance(term.getTermId());
            }

            @Override
            public int getCount() {
                return mTerms.size();
            }
        });

        for (int i = 0; i < mTerms.size(); i++) {
            if (mTerms.get(i).getTermId().equals(termId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }
}
