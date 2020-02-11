package com.soleeklab.nilebot.features.home.pond.addPond.AddPondFormTwo;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soleeklab.nilebot.GetPondsByFarmIDQuery;
import com.soleeklab.nilebot.ParentRecyclerAdapter;
import com.soleeklab.nilebot.ParentRecyclerViewHolder;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MyCache;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FishTypeAdapter extends ParentRecyclerAdapter<String> {


    @NonNull
    private OnItemCheckListener onItemCheckListener;

    @Inject
    MyCache myCache;

    private Context context;
    private ArrayList<GetPondsByFarmIDQuery.FishType> selectedFishTypes = new ArrayList<>();

    LocalRepo localRepo;

    public FishTypeAdapter(Context context, List<String> data, @NonNull OnItemCheckListener onItemCheckListener, ArrayList<GetPondsByFarmIDQuery.FishType> selectedFishTypes) {
        super(data);
        this.onItemCheckListener = onItemCheckListener;
        this.selectedFishTypes = selectedFishTypes;
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

        final String fishType = getData().get(position);

        viewHolder.txtChoiseName.setText(localRepo.getTranslation(fishType));


        for (int i = 0; i < selectedFishTypes.size(); i++) {
            if (selectedFishTypes.get(i).type().rawValue().equals(fishType)) {
                viewHolder.checkBox.setChecked(true);
                viewHolder.layFishCount.setVisibility(View.VISIBLE);
                viewHolder.edtPondFishCount.setText(selectedFishTypes.get(i).count() + "");
                onItemCheckListener.onItemCheck(fishType, selectedFishTypes.get(i).count());

            }
        }


        viewHolder.edtPondFishCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    onItemCheckListener.onItemCheck(fishType, Integer.parseInt(s.toString()));
                } else {
                    onItemCheckListener.onItemCheck(fishType, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewHolder.layChoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    viewHolder.layFishCount.setVisibility(View.GONE);
                    viewHolder.checkBox.setChecked(false);
                    onItemCheckListener.onItemUncheck(fishType, 0);
                } else {
                    viewHolder.layFishCount.setVisibility(View.VISIBLE);
                    viewHolder.checkBox.setChecked(true);
                    onItemCheckListener.onItemCheck(fishType, 0);
                }
            }
        });


        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    viewHolder.layFishCount.setVisibility(View.VISIBLE);
                    viewHolder.checkBox.setChecked(true);
                    onItemCheckListener.onItemCheck(fishType, 0);

                } else {
                    viewHolder.layFishCount.setVisibility(View.GONE);
                    viewHolder.checkBox.setChecked(false);
                    onItemCheckListener.onItemUncheck(fishType, 0);
                }
            }
        });

    }


    public void checkCell(String cond) {

    }

    class ViewHolder extends ParentRecyclerViewHolder {


        @BindView(R.id.checkbox)
        CheckBox checkBox;

        @BindView(R.id.lay_choise)
        LinearLayout layChoise;

        @BindView(R.id.txt_brand_name)
        TextView txtChoiseName;

        @BindView(R.id.edt_pond_fish_count)
        EditText edtPondFishCount;

        @BindView(R.id.lay_fish_count)
        LinearLayout layFishCount;

        ViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            ButterKnife.bind(this, itemView);
            setIsRecyclable(false);

        }
    }

    interface OnItemCheckListener {
        void onItemCheck(String item, int count);

        void onItemUncheck(String item, int count);
    }
}
