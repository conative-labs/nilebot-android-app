package com.soleeklab.nilebot.features.auth.forgotpassword.resetPassword;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.soleeklab.nilebot.BaseFragmentActivity;

import javax.inject.Inject;

public class ResetPasswordActivity extends BaseFragmentActivity {


    @Inject
    ResetPasswordFragment resetPasswordFragment;

    @Override
    protected Fragment getFragment() {
        return resetPasswordFragment;
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
