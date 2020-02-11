package com.soleeklab.nilebot;

import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.soleeklab.nilebot.utils.FragmentNavigationHelper;


public abstract class BaseFragmentActivity extends BaseActivity {


    public Toolbar toolbar;

    ConstraintLayout layoutToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);
        layoutToolbar = (ConstraintLayout) findViewById(R.id.toolbar_layout);

        if (getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container) == null) {
            FragmentNavigationHelper.navigateToFragment(this, getFragment(), getFragment().getClass().getName(), true, false, false);
//            GeneralUtils.addFragmentToActivity(getSupportFragmentManager(), getFragment(), R.id.fragment_container);
        }
        setToolBar(getToolbarTitle());
    }

    protected abstract Fragment getFragment();

    protected Fragment getAlreadyAddedFragment() {
        return getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager()
                .getBackStackEntryCount() <= 1)
            finish();
        else
            super.onBackPressed();
    }

    public abstract int getToolbarTitle();


    protected void setToolBar(int title) {

        if (title != 0) {
            toolbar = findViewById(R.id.toolbar);
            layoutToolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);
            ((TextView) findViewById(R.id.toolbarTitle)).setText(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
//            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }
}
