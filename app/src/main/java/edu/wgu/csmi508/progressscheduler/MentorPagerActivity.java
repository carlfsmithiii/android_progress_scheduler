package edu.wgu.csmi508.progressscheduler;

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

public class MentorPagerActivity extends AppCompatActivity {

    private static final String EXTRA_MENTOR_ID =
            "edu.wgu.csmi508.progressscheduler.mentor_id";

    private ViewPager mViewPager;
    private List<Mentor> mMentors;

    public static Intent newIntent(Context packageContext, UUID mentorId) {
        Intent intent = new Intent(packageContext, MentorPagerActivity.class);
        intent.putExtra(EXTRA_MENTOR_ID, mentorId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        UUID mentorId = (UUID) getIntent().getSerializableExtra(EXTRA_MENTOR_ID);

        mViewPager = findViewById(R.id.view_pager);

        mMentors = MentorList.getMentorList(this).getMentorList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Mentor mentor = mMentors.get(position);
                return MentorFragment.newInstance(mentor.getMentorId());
            }

            @Override
            public int getCount() {
                return mMentors.size();
            }
        });

        for (int i = 0; i < mMentors.size(); i++) {
            if (mMentors.get(i).getMentorId().equals(mentorId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
