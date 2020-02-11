package com.soleeklab.nilebot.features.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.AddSessionTokenMutation;
import com.soleeklab.nilebot.BaseActivity;
import com.soleeklab.nilebot.NotificationsQuery;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.models.MyCache;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.features.home.farms.FarmsFragment;
import com.soleeklab.nilebot.features.home.notification.NotificationsFragment;
import com.soleeklab.nilebot.features.home.settings.SettingsFragment;
import com.soleeklab.nilebot.type.DeviceType;
import com.soleeklab.nilebot.type.Status;
import com.soleeklab.nilebot.utils.GeneralUtils;
import com.soleeklab.nilebot.utils.LocalHelper;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

//    @Inject
//    FarmsFragment farmsFragment;

    @Inject
    SettingsFragment settingsFragment;

    @Inject
    LocalRepo localRepo;
    @BindView(R.id.frame_home)
    FrameLayout frameHome;
    BottomNavigationView navView;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    LocalHelper localHelper;

    @Inject
    MyCache myCache;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_settings:
                    //todo replace with settings fragments
                    GeneralUtils.replaceFragmentToActivity(getSupportFragmentManager(), new SettingsFragment(), R.id.frame_home);
                    return true;
                case R.id.navigation_notifications:
                    GeneralUtils.replaceFragmentToActivity(getSupportFragmentManager(), new NotificationsFragment(), R.id.frame_home);
                    return true;
                case R.id.navigation_farms:
                    GeneralUtils.replaceFragmentToActivity(getSupportFragmentManager(), new FarmsFragment(), R.id.frame_home);

                    //todo replace with settings fragments
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        localHelper = LocalHelper.getInstance(HomeActivity.this);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        GeneralUtils.replaceFragmentToActivity(getSupportFragmentManager(), new FarmsFragment(), R.id.frame_home);
        sendToken(localRepo.getUserPushToken());
//        getNotifications(0);




    }

    @Override
    protected void onResume() {
        super.onResume();
        if (localHelper.getLanguage().equalsIgnoreCase(LocalHelper.LANGUAGE_ARABIC)) {
            localHelper.setLanguage(LocalHelper.LANGUAGE_ARABIC);
            LocalHelper.ChangeDesignToRTL(HomeActivity.this);
        } else {
            localHelper.setLanguage(LocalHelper.LANGUAGE_ENGLISH);
            LocalHelper.ChangeDesignToLTR(HomeActivity.this);
        }
    }

    public void navigateToFarms() {
        navView.setSelectedItemId(R.id.navigation_farms);

    }

    @Override
    public void onBackPressed() {
        if (navView.getSelectedItemId() == R.id.navigation_notifications) {
            navView.setSelectedItemId(R.id.navigation_farms);
        } else {
            super.onBackPressed();
        }
//        else if(navView.getSelectedItemId() == R.id.navigation_settings){
//            super.onBackPressed();
//        }else
//            super.onBackPressed();
    }
//        else {
//            Log.e("mycache", myCache.isFarmFragment() + " ");
//
//            if (myCache.isFarmFragment()) {
//                DialogUtil.showSureToExitDialog(HomeActivity.this, getString(R.string.sure_to_exit), new DialogUtil.DialogClick() {
//                    @Override
//                    public void onDeleteClick() {
//                        finish();
//                    }
//                });
//            } else {
//                if (getSupportFragmentManager().getFragments().size() == 1) {
//                    DialogUtil.showSureToExitDialog(HomeActivity.this, getString(R.string.sure_to_exit), new DialogUtil.DialogClick() {
//                        @Override
//                        public void onDeleteClick() {
//                            finish();
//                        }
//                    });
//                } else
//                    super.onBackPressed();
//            }
//
//        }
//
//    }

    @Override
    protected View getView() {
        return null;
    }

    @Override
    public void showToast(String message) {

    }

    private void sendToken(String token) {

        AddSessionTokenMutation addSessionTokenMutation = AddSessionTokenMutation.builder().token(token).deviceType(DeviceType.ANDROID).build();
        ApolloClientBuilder.getApolloClient(true).mutate(addSessionTokenMutation).enqueue(new ApolloCall.Callback<AddSessionTokenMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<AddSessionTokenMutation.Data> response) {
                if (response.data() != null && response.data().setSessionToken() != null) {
                    if (response.data().setSessionToken().equals(Status.SUCCESS)) {
                        localRepo.setNewToken(false);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });
    }

    public void getNotifications(int page) {
//        showProgress();
        NotificationsQuery notificationsQuery = NotificationsQuery.builder().page(page).markAsRead(false).build();
        ApolloClientBuilder.getApolloClient(true).query(notificationsQuery).enqueue(new ApolloCallback<>(new ApolloCall.Callback<NotificationsQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<NotificationsQuery.Data> response) {
//                hideProgress();
                if (response.data() != null) {
                    if (response.data().notifications() != null) {
                        updateNotificationCount(response.data().notifications().unreadCount());
                    } else {
//                        showAlert(R.string.error_signup);
                    }
                } else {
//                    showAlert(R.string.server_error);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

//                hideProgress();
//                showAlert(R.string.error_occured);
            }
        }, uiHandler));
    }

    public void updateNotificationCount(int count) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                BottomNavigationMenuView bottomNavigationMenuView =
                        (BottomNavigationMenuView) navView.getChildAt(0);
                View v = bottomNavigationMenuView.getChildAt(1);
                BottomNavigationItemView itemView = (BottomNavigationItemView) v;

                View badge = LayoutInflater.from(HomeActivity.this)
                        .inflate(R.layout.notification_badge, itemView, true);

                TextView txt = badge.findViewById(R.id.notifications_badge);
                if (count > 0) {
                    txt.setVisibility(View.VISIBLE);
                    txt.setText(count + "");
                } else {
                    txt.setVisibility(View.GONE);
                }
            }
        });
    }


}
