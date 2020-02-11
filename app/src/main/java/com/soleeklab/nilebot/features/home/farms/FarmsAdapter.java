package com.soleeklab.nilebot.features.home.farms;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.soleeklab.nilebot.GetFarmsQuery;
import com.soleeklab.nilebot.ParentRecyclerAdapter;
import com.soleeklab.nilebot.ParentRecyclerViewHolder;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FarmsAdapter extends ParentRecyclerAdapter<GetFarmsQuery.Farm> implements Filterable {

    Context context;

    FarmsClicks farmsClicks;

    LocalRepo localRepo;

    private List<GetFarmsQuery.Farm> farmListFiltered;

    public FarmsAdapter(Context context, List<GetFarmsQuery.Farm> data, FarmsClicks farmsClicks) {
        super(data);
        this.context = context;
        this.farmsClicks = farmsClicks;
        this.farmListFiltered = data;
        localRepo = new LocalRepoImpl(context);
    }

    @NonNull
    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_farm, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setOnItemClickListener(getItemClickListener());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentRecyclerViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final GetFarmsQuery.Farm farm = farmListFiltered.get(position);

        viewHolder.txtCatName.setText(farm.name() + " ");
        viewHolder.txtFarmAddress.setText(localRepo.getTranslation(farm.type().rawValue()));
        viewHolder.txtFarmPondCount.setText(context.getString(R.string.pond_count) + ": " + farm.ponds().size());


        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farmsClicks.onDeleteFarm(farmListFiltered.get(position));

            }
        });

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farmsClicks.onEditFarmClick(farmListFiltered.get(position));
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    List<GetFarmsQuery.Farm> filteredList = new ArrayList<>();
                    filteredList.addAll(getData());
                    farmListFiltered = filteredList;

                } else {
                    List<GetFarmsQuery.Farm> filteredList = new ArrayList<>();
                    for (GetFarmsQuery.Farm row : getData()) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name().toLowerCase().contains(charString.toLowerCase()) || row.name().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    farmListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = farmListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                farmListFiltered = (ArrayList<GetFarmsQuery.Farm>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return farmListFiltered.size();
    }

    class ViewHolder extends ParentRecyclerViewHolder {

        @BindView(R.id.txt_farm_name)
        TextView txtCatName;

        @BindView(R.id.txt_farm_address)
        TextView txtFarmAddress;
        @BindView(R.id.txt_farm_pond_count)
        TextView txtFarmPondCount;

        @BindView(R.id.btn_delete)
        ImageView btnDelete;
        @BindView(R.id.btn_edit)
        ImageView btnEdit;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setClickableRootView(itemView);
        }
    }


    interface FarmsClicks {

        void onEditFarmClick(GetFarmsQuery.Farm farm);

        void onDeleteFarm(GetFarmsQuery.Farm farm);

    }
}
