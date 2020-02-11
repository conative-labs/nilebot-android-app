package com.soleeklab.nilebot.features.auth.login;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.soleeklab.nilebot.BaseFragmentActivity;

import javax.inject.Inject;

public class LoginActivity extends BaseFragmentActivity {


    @Inject
    LoginFragment loginFragment;

    @Override
    protected View getView() {
        return null;
    }

    @Override
    protected Fragment getFragment() {
        return loginFragment;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }


}
