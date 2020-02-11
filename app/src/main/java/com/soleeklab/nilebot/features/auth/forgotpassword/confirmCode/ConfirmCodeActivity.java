package com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode;

import androidx.fragment.app.Fragment;
import android.view.View;

import com.soleeklab.nilebot.BaseFragmentActivity;

import javax.inject.Inject;

public class ConfirmCodeActivity extends BaseFragmentActivity {


    @Inject
    ConfirmCodeFragment confirmCodeFragment;

    @Override
    protected Fragment getFragment() {
        return confirmCodeFragment;
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
