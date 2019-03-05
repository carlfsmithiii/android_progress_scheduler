package edu.wgu.csmi508.progressscheduler;

import android.support.v4.app.Fragment;

public class HomeScreenActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new HomeScreenFragment();
    }
}
