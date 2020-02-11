package com.soleeklab.nilebot.features.home.settings.phones;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soleeklab.nilebot.GetPhonesQuery;
import com.soleeklab.nilebot.ParentRecyclerAdapter;
import com.soleeklab.nilebot.ParentRecyclerViewHolder;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.type.LanguageType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhonesAdapter extends ParentRecyclerAdapter<GetPhonesQuery.Phone> {

    PhonesClicks phonesClicks;
    Context context;
    LocalRepo localRepo;

    public PhonesAdapter(Context context, List<GetPhonesQuery.Phone> data, PhonesClicks phonesClicks) {
        super(data);
        this.context = context;
        this.phonesClicks = phonesClicks;
        localRepo = new LocalRepoImpl(context);
    }

    @NonNull
    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_phone, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setOnItemClickListener(getItemClickListener());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentRecyclerViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final GetPhonesQuery.Phone phone = getData().get(position);

        viewHolder.txtPhone.setText(phone.number().prefix() + phone.number().number());


        if (phone.language().equals(LanguageType.ARABIC)) {
            viewHolder.txtLanguage.setText(context.getString(R.string.arabic));
        } else {
            viewHolder.txtLanguage.setText(context.getString(R.string.english));
        }

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonesClicks.onDeletePhone(getData().get(position));

            }
        });

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonesClicks.onEditPhoneClick(getData().get(position));
            }
        });
    }


    class ViewHolder extends ParentRecyclerViewHolder {

        @BindView(R.id.txt_phone)
        TextView txtPhone;

        @BindView(R.id.txt_language)
        TextView txtLanguage;

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


    interface PhonesClicks {

        void onEditPhoneClick(GetPhonesQuery.Phone phone);

        void onDeletePhone(GetPhonesQuery.Phone phone);

    }
}

