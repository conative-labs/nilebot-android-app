package com.soleeklab.nilebot.features.auth.signup;

import androidx.fragment.app.Fragment;
import android.view.View;

import com.soleeklab.nilebot.BaseFragmentActivity;

import javax.inject.Inject;

public class SignUpActivity extends BaseFragmentActivity {


    @Inject
    SignUpFragment signUpFragment;

    @Override
    protected Fragment getFragment() {
        return signUpFragment;
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
