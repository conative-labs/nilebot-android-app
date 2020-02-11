package com.soleeklab.nilebot.features.home.pond;

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

import com.soleeklab.nilebot.GetPondsByFarmIDQuery;
import com.soleeklab.nilebot.ParentRecyclerAdapter;
import com.soleeklab.nilebot.ParentRecyclerViewHolder;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PondsAdapter extends ParentRecyclerAdapter<GetPondsByFarmIDQuery.Pond> implements Filterable {

    Context context;
    PondsClicks pondsClicks;
    LocalRepo localRepo;

    private List<GetPondsByFarmIDQuery.Pond> pondListFiltered;


    public PondsAdapter(Context context, List<GetPondsByFarmIDQuery.Pond> data, PondsClicks pondsClicks) {
        super(data);
        this.context = context;
        this.pondsClicks = pondsClicks;
        this.pondListFiltered = data;

        localRepo = new LocalRepoImpl(context);
    }

    @NonNull
    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_pond, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setOnItemClickListener(getItemClickListener());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentRecyclerViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final GetPondsByFarmIDQuery.Pond pond = pondListFiltered.get(position);

        viewHolder.txtCatName.setText(pond.name() + " ");
        viewHolder.txtFarmAddress.setText(localRepo.getTranslation(pond.type().rawValue()) + " ");

        switch (pond.type()) {
            case CONCRETEPOND:
                viewHolder.imgPondType.setImageResource(R.drawable.ic_farm);
                break;
            case GEOMEMBRANEPOND:
                viewHolder.imgPondType.setImageResource(R.drawable.ic_farm);
                break;
            case EARTHENPOND:
                viewHolder.imgPondType.setImageResource(R.drawable.ic_farm);
                break;
            case FISHCAGE:
                viewHolder.imgPondType.setImageResource(R.drawable.ic_farm);
                break;
            case TANK:
                viewHolder.imgPondType.setImageResource(R.drawable.ic_farm);
                break;
            case IPA:
                viewHolder.imgPondType.setImageResource(R.drawable.ic_farm);
                break;
            default:
                viewHolder.imgPondType.setImageResource(R.drawable.ic_farm);
                break;
        }

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pondsClicks.onDeletePond(pondListFiltered.get(position));

            }
        });

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pondsClicks.onEditPondClick(pondListFiltered.get(position));
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                String keyword = "";
                String pondType = "";
                String[] parts = charString.split("#");
                keyword = parts[0];
                pondType = parts[1];

                if (pondType.equals("All") || pondType.equals("الكل")) {
                    List<GetPondsByFarmIDQuery.Pond> filteredList = new ArrayList<>();
                    for (GetPondsByFarmIDQuery.Pond row : getData()) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name().toLowerCase().contains(keyword.toLowerCase()) || row.name().contains(keyword)) {
                            filteredList.add(row);
                        }
                    }
                    pondListFiltered = filteredList;
                } else {

                    List<GetPondsByFarmIDQuery.Pond> filteredList = new ArrayList<>();
                    for (GetPondsByFarmIDQuery.Pond row : getData()) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.type().rawValue().equals(pondType)) {
                            filteredList.add(row);
                        }
                    }
                    List<GetPondsByFarmIDQuery.Pond> filteredListKeyWord = new ArrayList<>();

                    for (GetPondsByFarmIDQuery.Pond row : filteredList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name().toLowerCase().contains(keyword.toLowerCase()) || row.name().contains(keyword)) {
                            filteredListKeyWord.add(row);
                        }
                    }
                    pondListFiltered = filteredListKeyWord;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = pondListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pondListFiltered = (ArrayList<GetPondsByFarmIDQuery.Pond>) filterResults.values;
                pondsClicks.onSearchResult(pondListFiltered.size());
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return pondListFiltered.size();
    }

    class ViewHolder extends ParentRecyclerViewHolder {

        @BindView(R.id.txt_farm_name)
        TextView txtCatName;

        @BindView(R.id.txt_farm_address)
        TextView txtFarmAddress;

        @BindView(R.id.btn_delete)
        ImageView btnDelete;

        @BindView(R.id.img_pond_type)
        ImageView imgPondType;

        @BindView(R.id.btn_edit)
        ImageView btnEdit;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setClickableRootView(itemView);
        }
    }

    interface PondsClicks {

        void onEditPondClick(GetPondsByFarmIDQuery.Pond pond);

        void onDeletePond(GetPondsByFarmIDQuery.Pond pond);

        void onSearchResult(int size);

    }
}
