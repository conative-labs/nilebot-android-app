package com.soleeklab.nilebot.features.home.settings.devices;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soleeklab.nilebot.GetBotsQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.utils.CustomExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends ParentFragment implements DevicesContract.View,
        BotExpandableAdapter.OnBotsClicks, SwipeRefreshLayout.OnRefreshListener {

    Unbinder unbinder;
    @BindView(R.id.btn_add_bot)
    Button btnAddBot;
    @BindView(R.id.lay_no_data)
    LinearLayout layNoData;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.exp_bot)
    CustomExpandableListView expBot;
    @BindView(R.id.swip_to_refresh_layout)
    SwipeRefreshLayout swipToRefreshLayout;

    Dialog dialog, dialogAddBot, dialogNewBot, dialogEditBot;

    BotExpandableAdapter botExpandableAdapter;

    @Inject
    DevicesContract.Presenter mPresenter;

    private List<GetBotsQuery.Bot> listDataHeader;
    private HashMap<GetBotsQuery.Bot, List<GetBotsQuery.Sensor>> listDataChild;

    Dialog deleteDialog;

    private String name = "";
    private String editName = "";
    private String timeZone = "";
    private String editTimeZone = "";
    private int rate = 0;
    private int editRate = 0;

    @Inject
    public DevicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_serial_numbers, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setPresenter(DevicesContract.Presenter presenter) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        swipToRefreshLayout.setOnRefreshListener(this);
        if (isInternetConnection())
            mPresenter.getBots(true);
        else
            showNoConnectionAlert();
        txtToolbarTitle.setText(getString(R.string.devices));

        initSearch();
    }

    private void initSearch() {
        //change icon color
//        searchView.setIconified(false);
        ImageView searchIcon = searchView.findViewById(R.id.search_button);
        ImageView searchbadge = searchView.findViewById(R.id.search_voice_btn);

        ImageView searchClose = searchView.findViewById(R.id.search_close_btn);


        searchIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_search_black_24dp));
        searchbadge.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_search_black_24dp));

        searchClose.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_white_24dp));

        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.white));
        searchAutoComplete.setTextColor(getResources().getColor(android.R.color.white));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (botExpandableAdapter != null)
                    botExpandableAdapter.filterData(query.trim());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (botExpandableAdapter != null)
                    botExpandableAdapter.filterData(newText.trim());
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_add_bot, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_bot:
//                mPresenter.addBot(new SimpleDateFormat("ZZZZZ", Locale.getDefault()).format(System.currentTimeMillis()));
                addNewBot();
                break;
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
        }
    }

    private void addNewBot() {

        dialogAddBot = new Dialog(getActivity());
        dialogAddBot.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddBot.setCancelable(true);
        dialogAddBot.setContentView(R.layout.dialog_bots_add);
        dialogAddBot.show();

        EditText edtBotName = dialogAddBot.findViewById(R.id.edt_bot_name);
        EditText edtBotRate = dialogAddBot.findViewById(R.id.edt_bot_rate);

        TextView txtErrorBotName = dialogAddBot.findViewById(R.id.txt_error_bot_name);
        TextView txtErrorBotRate = dialogAddBot.findViewById(R.id.txt_error_bot_rate);

        Button btnCancel = dialogAddBot.findViewById(R.id.btn_cancel);
        Button btnAdd = dialogAddBot.findViewById(R.id.btn_add);


        edtBotRate.setText("10");


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddBot.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtErrorBotName.setVisibility(View.GONE);
                txtErrorBotRate.setVisibility(View.GONE);

                name = edtBotName.getText().toString().trim();


                if (edtBotRate.getText().toString().trim().equals("")) {
                    txtErrorBotRate.setText(getString(R.string.error_required));
                    txtErrorBotRate.setVisibility(View.VISIBLE);
                    return;
                }

                rate = Integer.parseInt(edtBotRate.getText().toString().trim());

                if (rate == 0) {
                    txtErrorBotRate.setText(getString(R.string.error_rate));
                    txtErrorBotRate.setVisibility(View.VISIBLE);
                    return;
                }
                if (rate > 999999) {
                    txtErrorBotRate.setText(getString(R.string.error_large));
                    txtErrorBotRate.setVisibility(View.VISIBLE);
                    return;
                }
                mPresenter.addBot(name, rate);
            }
        });


    }

    @Override
    public void initBots(List<GetBotsQuery.Bot> botList) {

        if (botList.size() > 0) {

            listDataHeader = new ArrayList<GetBotsQuery.Bot>();
            listDataChild = new HashMap<GetBotsQuery.Bot, List<GetBotsQuery.Sensor>>();

            ArrayList<GetBotsQuery.Bot> attroo = new ArrayList<>();
            attroo.addAll(botList);

            for (int i = 0; i < attroo.size(); i++) {
                listDataHeader.add(attroo.get(i));
            }

            for (int i = 0; i < attroo.size(); i++) {
                listDataChild.put(listDataHeader.get(i), attroo.get(i).sensors());
            }

            botExpandableAdapter = new BotExpandableAdapter(getActivity(), listDataHeader, listDataChild, this);

            if (expBot != null) {
                expBot.setAdapter(botExpandableAdapter);
                expBot.setChildIndicator(null);
                expBot.setDividerHeight(0);
                expBot.setVisibility(View.VISIBLE);
                layNoData.setVisibility(View.GONE);


            }

        } else {
            expBot.setVisibility(View.GONE);
            layNoData.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void successUpdate() {
        dialog.dismiss();
//        showToast(getString(R.string.sensor_updated));
        if (isInternetConnection())
            mPresenter.getBots(false);
        else
            showNoConnectionAlert();
    }

    @Override
    public void successEditBot() {
        dialogEditBot.dismiss();
        if (isInternetConnection())
            mPresenter.getBots(false);
        else
            showNoConnectionAlert();
    }

    @Override
    public void successDeleteBot() {
        deleteDialog.dismiss();
        if (isInternetConnection())
            mPresenter.getBots(false);
        else
            showNoConnectionAlert();
    }

    @Override
    public void successNewBot(String id, String token) {
        if (isInternetConnection())
            mPresenter.getBots(false);
        else
            showNoConnectionAlert();
        dialogAddBot.dismiss();
        dialogNewBot = new Dialog(getActivity());
        dialogNewBot.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNewBot.setCancelable(true);
        dialogNewBot.setContentView(R.layout.dialog_bot);

        TextView txtId = dialogNewBot.findViewById(R.id.txt_token);
        TextView txtTokn = dialogNewBot.findViewById(R.id.txt_id);

        txtId.setText(id);
        txtTokn.setText(token);

        Button btnAdd = dialogNewBot.findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNewBot.dismiss();
            }
        });
        dialogNewBot.show();
    }

    @Override
    public void delete(String id) {


        deleteDialog = new Dialog(getActivity());
        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteDialog.setCancelable(false);
        deleteDialog.setContentView(R.layout.dialog_sure_to);

        TextView text = deleteDialog.findViewById(R.id.txt_question);
        text.setText(getString(R.string.sure_to_delete_bot));
        TextView txtHeaderTitle = deleteDialog.findViewById(R.id.txt_header_title);
        txtHeaderTitle.setText(getString(R.string.delete));

        Button btnCancel = deleteDialog.findViewById(R.id.btn_cancel);
        Button btnDelete = deleteDialog.findViewById(R.id.btn_delete);

        btnDelete.setText(getString(R.string.delete));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.removeBot(id);
            }
        });

        deleteDialog.show();

    }

    @Override
    public void edit(GetBotsQuery.Bot bot) {
        dialogEditBot = new Dialog(getActivity());
        dialogEditBot.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditBot.setCancelable(true);
        dialogEditBot.setContentView(R.layout.dialog_bots_edits);

        EditText edtBotName = dialogEditBot.findViewById(R.id.edt_bot_name);
        EditText edtBotRate = dialogEditBot.findViewById(R.id.edt_bot_rate);

        TextView txtErrorBotName = dialogEditBot.findViewById(R.id.txt_error_bot_name);
        TextView txtErrorBotRate = dialogEditBot.findViewById(R.id.txt_error_bot_rate);

        Button btnCancel = dialogEditBot.findViewById(R.id.btn_cancel);
        Button btnAdd = dialogEditBot.findViewById(R.id.btn_add);

        edtBotName.setText(bot.name() + "");
        edtBotRate.setText(bot.rate() + "");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditBot.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtErrorBotName.setVisibility(View.GONE);
                txtErrorBotRate.setVisibility(View.GONE);

                editName = edtBotName.getText().toString().trim();

                if (edtBotRate.getText().toString().trim().equals("")) {
                    txtErrorBotRate.setText(getString(R.string.error_required));
                    txtErrorBotRate.setVisibility(View.VISIBLE);

                    return;
                }

                editRate = Integer.parseInt(edtBotRate.getText().toString().trim());

                if (editRate == 0) {
                    txtErrorBotRate.setText(getString(R.string.error_rate));
                    txtErrorBotRate.setVisibility(View.VISIBLE);

                    return;
                }
                if (rate > 999999) {
                    txtErrorBotRate.setText(getString(R.string.error_large));
                    txtErrorBotRate.setVisibility(View.VISIBLE);
                    return;
                }

                mPresenter.editBot(bot.botID(), editName, editRate, editTimeZone);

            }
        });


        dialogEditBot.show();
    }

    @Override
    public void onRefresh() {
        if (isInternetConnection())
            mPresenter.getBots(true);
        else
            showNoConnectionAlert();

        swipToRefreshLayout.setRefreshing(false);
    }
}
