package com.soleeklab.nilebot.features.home.settings.devices;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soleeklab.nilebot.GetBotsQuery;
import com.soleeklab.nilebot.ParentRecyclerAdapter;
import com.soleeklab.nilebot.ParentRecyclerViewHolder;
import com.soleeklab.nilebot.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BotsAdapter extends ParentRecyclerAdapter<GetBotsQuery.Bot> {

    Context context;
    BotClicks botClicks;

    public BotsAdapter(Context context, List<GetBotsQuery.Bot> data, BotClicks botClicks) {
        super(data);
        this.context = context;
        this.botClicks = botClicks;
    }

    @NonNull
    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_serials, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setOnItemClickListener(getItemClickListener());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentRecyclerViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final GetBotsQuery.Bot bot = getData().get(position);

        viewHolder.txtSerialNumber.setText(bot.name() + " ");

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botClicks.deleteBot(bot.botID());
            }
        });

    }


    class ViewHolder extends ParentRecyclerViewHolder {

        @BindView(R.id.txt_serial_number)
        TextView txtSerialNumber;

        @BindView(R.id.btn_delete)
        ImageView btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setClickableRootView(itemView);
        }
    }


    interface BotClicks {
        void deleteBot(String id);
    }
}
