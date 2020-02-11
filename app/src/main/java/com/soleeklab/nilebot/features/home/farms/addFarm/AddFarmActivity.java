package com.soleeklab.nilebot.features.home.farms.addFarm;

import androidx.fragment.app.Fragment;
import android.view.View;

import com.soleeklab.nilebot.BaseFragmentActivity;

import javax.inject.Inject;

public class AddFarmActivity extends BaseFragmentActivity {

    @Inject
    AddFarmFragment addFarmFragment;

    @Override
    protected Fragment getFragment() {
        return addFarmFragment;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    @Override
    protected View getView() {
        return null;
    }
}
