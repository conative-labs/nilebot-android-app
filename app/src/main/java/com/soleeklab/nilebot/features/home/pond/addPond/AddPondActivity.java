package com.soleeklab.nilebot.features.home.pond.addPond;

import androidx.fragment.app.Fragment;
import android.view.View;

import com.soleeklab.nilebot.BaseFragmentActivity;

import javax.inject.Inject;

public class AddPondActivity extends BaseFragmentActivity {


    @Inject
    AddPondFragment addPondFragment;

    @Override
    protected Fragment getFragment() {
        return addPondFragment;
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
