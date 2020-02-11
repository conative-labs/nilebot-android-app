package com.soleeklab.nilebot.features.auth.forgotpassword;

import androidx.fragment.app.Fragment;
import android.view.View;

import com.soleeklab.nilebot.BaseFragmentActivity;

import javax.inject.Inject;

public class ForgotPasswordActivity extends BaseFragmentActivity {


    @Inject
    ForgotPasswordFragment forgotPasswordFragment;

    @Override
    protected Fragment getFragment() {
        return forgotPasswordFragment;
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
