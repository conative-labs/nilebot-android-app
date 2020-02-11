package com.soleeklab.nilebot.features.home.pond.addPond.AddPondFormTwo;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soleeklab.nilebot.ParentRecyclerAdapter;
import com.soleeklab.nilebot.ParentRecyclerViewHolder;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.type.WaterSourceType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WaterSourcesAdapter extends ParentRecyclerAdapter<String> {


    @NonNull
    private OnItemCheckListener onItemCheckListener;

    private ArrayList<WaterSourceType> selectedWaterSources = new ArrayList<>();
    private Context context;

    LocalRepo localRepo;

    public WaterSourcesAdapter(Context context, List<String> data, @NonNull OnItemCheckListener onItemCheckListener, ArrayList<WaterSourceType> selectedWaterSources) {
        super(data);
        this.onItemCheckListener = onItemCheckListener;
        this.selectedWaterSources = selectedWaterSources;
        this.context = context;

        localRepo = new LocalRepoImpl(context);
    }


    @NonNull
    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_multi_select, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setOnItemClickListener(getItemClickListener());

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentRecyclerViewHolder holder, int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;

        final String waterSource = getData().get(position);

        viewHolder.txtChoiseName.setText(localRepo.getTranslation(waterSource));


        if (selectedWaterSources.contains(WaterSourceType.safeValueOf(waterSource))) {
            viewHolder.checkBox.setChecked(true);
            onItemCheckListener.onItemCheck(waterSource);

        }

        viewHolder.layChoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    viewHolder.checkBox.setChecked(false);
                    onItemCheckListener.onItemUncheck(waterSource);

                } else {
                    viewHolder.checkBox.setChecked(true);
                    onItemCheckListener.onItemCheck(waterSource);
                }
            }
        });

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    viewHolder.checkBox.setChecked(true);
                    onItemCheckListener.onItemCheck(waterSource);

                } else {
                    viewHolder.checkBox.setChecked(false);
                    onItemCheckListener.onItemUncheck(waterSource);
                }
            }
        });

    }


    class ViewHolder extends ParentRecyclerViewHolder {


        @BindView(R.id.checkbox)
        CheckBox checkBox;

        @BindView(R.id.lay_choise)
        LinearLayout layChoise;

        @BindView(R.id.txt_brand_name)
        TextView txtChoiseName;

        ViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            ButterKnife.bind(this, itemView);

        }
    }

    interface OnItemCheckListener {
        void onItemCheck(String item);

        void onItemUncheck(String item);
    }


}
