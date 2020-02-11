package com.soleeklab.nilebot.features.home.notification;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.soleeklab.nilebot.NotificationsQuery;
import com.soleeklab.nilebot.ParentRecyclerAdapter;
import com.soleeklab.nilebot.ParentRecyclerViewHolder;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.utils.DateUtil;
import com.soleeklab.nilebot.utils.LocalHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soleeklab.nilebot.utils.DateUtil.dateIn12;

public class NotificationAdapter extends ParentRecyclerAdapter<NotificationsQuery.Notification> {

    Context context;
    private OnLoadMoreLisnter onLoadMoreLisnter;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean loading = false;

    public NotificationAdapter(List<NotificationsQuery.Notification> data, Context context, OnLoadMoreLisnter onLoadMoreLisnter) {
        super(data);
        this.context = context;
        this.onLoadMoreLisnter = onLoadMoreLisnter;
    }

    @NonNull
    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        if (viewType == VIEW_ITEM) {
            view = inflater.inflate(R.layout.item_notification, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.setOnItemClickListener(getItemClickListener());
            return holder;

        } else if (viewType == VIEW_PROG) {
            view = inflater.inflate(R.layout.progress_dialog, parent, false);
            progress holder = new progress(view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentRecyclerViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            final NotificationsQuery.Notification notification = getData().get(position);

            viewHolder.txtNotificationName.setText(notification.txt());



            viewHolder.txtTime.setText(DateUtil.formatDate(notification.time(),"yyy-MM-dd HH:MM:ss",dateIn12, LocalHelper.getLanguage()));
            viewHolder.txtDate.setText(DateUtil.formatDate(notification.time(),"yyy-MM-dd HH:MM:ss","dd MMM yyy", LocalHelper.getLanguage()));

        } else {
            if (!loading) {
                Log.d("loading", "loading" + loading);
                // End has been reached
                // Do something
                if (onLoadMoreLisnter != null) {
                    onLoadMoreLisnter.onLoadMore(position);
                }
                loading = true;
            }
        }
    }

    static class ViewHolder extends ParentRecyclerViewHolder {

        @BindView(R.id.txt_time)
        TextView txtTime;

        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_notification_name)
        TextView txtNotificationName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setClickableRootView(itemView);
        }
    }

    public class progress extends ParentRecyclerViewHolder {
        ProgressBar progressBar;

        public progress(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar2);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return getData().get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void update(ArrayList<NotificationsQuery.Notification> newItems) {
        getData().addAll(newItems);
        notifyDataSetChanged();
    }

    public void add(ArrayList<NotificationsQuery.Notification> products) {
        getData().addAll(products);
        notifyDataSetChanged();
    }

    public interface OnLoadMoreLisnter {
        void onLoadMore(int id);
    }



    public void removeLastItem() {
        getData().remove(getData().size() - 1);
        notifyItemRemoved(getData().size());
    }

    public void setLoaded() {
        loading = false;
    }
}
