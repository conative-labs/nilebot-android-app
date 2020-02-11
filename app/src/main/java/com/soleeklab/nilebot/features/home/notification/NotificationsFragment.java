package com.soleeklab.nilebot.features.home.notification;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.NotificationsQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.features.home.HomeActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends ParentFragment implements SwipeRefreshLayout.OnRefreshListener {

    Handler uiHandler = new Handler(Looper.getMainLooper());

    @BindView(R.id.rv_notifications)
    RecyclerView rvNotifications;

    @BindView(R.id.txt_no_data)
    LinearLayout txtNoData;

    Unbinder unbinder;

    NotificationAdapter notificationAdapter;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.swip_to_refresh_layout)
    SwipeRefreshLayout swipToRefreshLayout;
    private int pageCount = 0;


    @Inject
    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isInternetConnection())
            getNotifications(++pageCount);
        else
            showNoConnectionAlert();

        swipToRefreshLayout.setOnRefreshListener(this);
        txtToolbarTitle.setText(getString(R.string.notifications));
        btnBack.setVisibility(View.GONE);
    }


    private void getNotifications(int page) {
        showProgress();
        NotificationsQuery notificationsQuery = NotificationsQuery.builder().page(page).markAsRead(true).build();
        ApolloClientBuilder.getApolloClient(true).query(notificationsQuery).enqueue(new ApolloCallback<>(new ApolloCall.Callback<NotificationsQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<NotificationsQuery.Data> response) {
                hideProgress();
                if (response.data() != null) {
                    if (response.data().notifications() != null) {
                        ArrayList<NotificationsQuery.Notification> notificationArrayList = new ArrayList<>();
                        notificationArrayList.addAll(response.data().notifications().notifications());
                        initNotificationList(notificationArrayList);

                        HomeActivity homeActivity = (HomeActivity)getActivity();
                        homeActivity.updateNotificationCount(response.data().notifications().unreadCount());
                    } else {
                        showAlert(R.string.error_signup);
                    }
                } else {
                    showAlert(R.string.server_error);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

                hideProgress();
                showAlert(R.string.error_occured);
            }
        }, uiHandler));

    }

    private void initNotificationList(ArrayList<NotificationsQuery.Notification> notifications) {
        if (getView() == null)
            return;
        
        if (notifications.size() > 0 && getView() != null) {
            txtNoData.setVisibility(View.GONE);
            rvNotifications.setVisibility(View.VISIBLE);

            if (notifications.size() >= 19)
                notifications.add(null);

            if (pageCount == 1) {
                notificationAdapter = new NotificationAdapter(notifications, getActivity(), new NotificationAdapter.OnLoadMoreLisnter() {
                    @Override
                    public void onLoadMore(int id) {
                        getNotifications(++pageCount);
                    }
                });
                rvNotifications.setAdapter(notificationAdapter);
            }
            if (pageCount > 1) {
                notificationAdapter.removeLastItem();
                notificationAdapter.setLoaded();
                notificationAdapter.insertAll(notifications);
                return;
            }
        } else {
            if (getView() != null) {
                if (pageCount == 1) {
                    txtNoData.setVisibility(View.VISIBLE);
                }
                if (pageCount > 1) {
                    notificationAdapter.removeLastItem();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        pageCount = 0;
        if (isInternetConnection())
            getNotifications(++pageCount);
        else
            showNoConnectionAlert();

        swipToRefreshLayout.setRefreshing(false);
    }
}
