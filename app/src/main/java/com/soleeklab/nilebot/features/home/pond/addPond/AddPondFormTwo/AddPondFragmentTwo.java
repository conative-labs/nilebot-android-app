package com.soleeklab.nilebot.features.home.pond.addPond.AddPondFormTwo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.soleeklab.nilebot.GetPondsByFarmIDQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.data.models.MyCache;
import com.soleeklab.nilebot.features.home.pond.PondTypeAdapter;
import com.soleeklab.nilebot.features.home.pond.addPond.AddPondFragment;
import com.soleeklab.nilebot.type.FishTypeEnum;
import com.soleeklab.nilebot.type.IFishTypes;
import com.soleeklab.nilebot.type.WaterSourceType;
import com.soleeklab.nilebot.utils.DialogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AddPondFragmentTwo extends ParentFragment implements AddPondFormTwoContract.View, WaterSourcesAdapter.OnItemCheckListener {

    @Inject
    AddPondFormTwoContract.Presenter mPresenter;

    @BindView(R.id.rv_water_sources)
    RecyclerView rvWaterSources;
    @BindView(R.id.txt_error_water_source)
    TextView txtErrorWaterSource;
    @BindView(R.id.rv_fish_type)
    RecyclerView rvFishType;
    @BindView(R.id.txt_error_fish_type)
    TextView txtErrorFishType;
    @BindView(R.id.spinner_feed_type)
    Spinner spinnerFeedType;
    @BindView(R.id.txt_error_feed_type)
    TextView txtErrorFeedType;
    @BindView(R.id.btn_add)
    Button btnAdd;

    Unbinder unbinder;

    PondTypeAdapter pondTypeAdapter;

    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.txt_edit_pond)
    TextView txtEditPond;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;
    //    @BindView(R.id.btn_add_more)
//    Button btnAddMore;
    private String fishFeedType = "";
    WaterSourcesAdapter waterSourcesAdapter;
    FishTypeAdapter fishTypeAdapter;

    private ArrayList<String> selectedWaterSources = new ArrayList<>();

    private ArrayList<IFishTypes> selectedFishTypes = new ArrayList<>();


    private boolean isEdit = false;
    private GetPondsByFarmIDQuery.Pond pond;
    private ArrayList<String> pondFeedTypeEditList = new ArrayList<>();

    @Inject
    MyCache myCache;

    @Inject
    public AddPondFragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pond_fragment_two, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        AddPondFragment parentFrag = ((AddPondFragment) AddPondFragmentTwo.this.getParentFragment());
                        parentFrag.setFragOne();
                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        txtToolbarTitle.setText(getString(R.string.new_pond));
        isEdit = getActivity().getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            Gson gson = new Gson();
            pond = gson.fromJson(getActivity().getIntent().getStringExtra("pond"), GetPondsByFarmIDQuery.Pond.class);
//            btnAddMore.setVisibility(View.GONE);

        }
        mPresenter.start();

        if (isEdit)
            initEditFields(pond);


    }

    private void initEditFields(GetPondsByFarmIDQuery.Pond pond) {

        btnAdd.setText(getString(R.string.edit));
        txtToolbarTitle.setText(getString(R.string.edit));
        txtEditPond.setText(getString(R.string.edit_pond));


        int pos = 0;
        for (int i = 0; i < pondFeedTypeEditList.size(); i++) {
            if (pondFeedTypeEditList.get(i).equals(pond.fishFeedType().rawValue()))
                pos = i;
        }
        spinnerFeedType.setSelection(pos);


    }

    @Override
    public void removeErrors() {
        txtErrorWaterSource.setVisibility(View.GONE);
        txtErrorFishType.setVisibility(View.GONE);
        txtErrorFeedType.setVisibility(View.GONE);


    }

    @Override
    public void showInvalidWaterSource() {
        txtErrorWaterSource.setVisibility(View.VISIBLE);
        txtErrorWaterSource.setText(getString(R.string.error_required));
    }

    @Override
    public void showInvalidFishType() {
        txtErrorFishType.setVisibility(View.VISIBLE);
        txtErrorFishType.setText(getString(R.string.error_required));

    }

    @Override
    public void showInvalidFishTypeCount() {
        txtErrorFishType.setVisibility(View.VISIBLE);
        txtErrorFishType.setText(getString(R.string.fish_count_required));
    }

    @Override
    public void showInvalidFishTypeLarge() {
        txtErrorFishType.setVisibility(View.VISIBLE);
        txtErrorFishType.setText(getString(R.string.error_large));
    }


    @Override
    public void showInvalidFishFeedType() {
        txtErrorFeedType.setVisibility(View.VISIBLE);
        txtErrorFeedType.setText(getString(R.string.error_required));


    }

    @Override
    public void initWaterSources(ArrayList<String> waterSources) {
        waterSources.remove(waterSources.size() - 1);
        if (pond != null) {
            waterSourcesAdapter = new WaterSourcesAdapter(getActivity(), waterSources, this, (ArrayList<WaterSourceType>) pond.waterSources());
        } else
            waterSourcesAdapter = new WaterSourcesAdapter(getActivity(), waterSources, this, new ArrayList<>());
        rvWaterSources.setAdapter(waterSourcesAdapter);
    }

    @Override
    public void initFishTypes(ArrayList<String> fishTypes) {
        fishTypes.remove(fishTypes.size() - 1);

        ArrayList<GetPondsByFarmIDQuery.FishType> fishTypeArrayList = new ArrayList<>();

        if (pond != null) {
            fishTypeArrayList.addAll(pond.fishTypes());
        }
        fishTypeAdapter = new FishTypeAdapter(getActivity(), fishTypes, new FishTypeAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(String item, int count) {

                boolean fishTypeAdded = false;
                int fishTypePos = -1;
                for (int i = 0; i < selectedFishTypes.size(); i++) {
                    if (selectedFishTypes.get(i).type().rawValue().equals(item)) {
                        fishTypeAdded = true;
                        fishTypePos = i;
                        break;
                    }
                }
                if (fishTypeAdded) {
                    selectedFishTypes.remove(fishTypePos);
                }
                IFishTypes iFishTypes = IFishTypes.builder().type(FishTypeEnum.safeValueOf(item)).count(count).build();
                selectedFishTypes.add(iFishTypes);
            }

            @Override
            public void onItemUncheck(String item, int count) {

                int deletedPos = 0;
                for (int i = 0; i < selectedFishTypes.size(); i++) {
                    if (selectedFishTypes.get(i).type().rawValue().equals(item)) {
                        deletedPos = i;
                        break;
                    }
                }
                selectedFishTypes.remove(deletedPos);

            }
        }, fishTypeArrayList);
        rvFishType.setAdapter(fishTypeAdapter);
    }

    @Override
    public void initFishFeedTypes(ArrayList<String> fishFeedTypes) {
        fishFeedTypes.remove(fishFeedTypes.size() - 1);
        fishFeedTypes.add(0, getString(R.string.choose_fish_feed));
        pondFeedTypeEditList.addAll(fishFeedTypes);
        pondTypeAdapter = new PondTypeAdapter(getActivity(), R.layout.item_farmtype_spinner, fishFeedTypes);
        spinnerFeedType.setAdapter(pondTypeAdapter);
        spinnerFeedType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    fishFeedType = fishFeedTypes.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void addPondSuccess(boolean isAddMore) {

        if (isEdit) {
            EventBus.getDefault().post(new MessageEvent("refresh_ponds"));
            getActivity().onBackPressed();

        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogUtil.showSureToDialog(getActivity(), getString(R.string.success), getString(R.string.pond_add_successfully) + "\n" + getString(R.string.more_pond), getString(R.string.text_yes), getString(R.string.text_no), new DialogUtil.DialogClick() {
                        @Override
                        public void onDeleteClick() {
                            AddPondFragment parentFrag = ((AddPondFragment) AddPondFragmentTwo.this.getParentFragment());
                            parentFrag.setFragOne();
                        }

                        @Override
                        public void onCancelClick() {
                            EventBus.getDefault().post(new MessageEvent("refresh_ponds"));
                            getActivity().onBackPressed();

                        }
                    });
                }
            });
        }


    }

    @Override
    public void setPresenter(AddPondFormTwoContract.Presenter presenter) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                AddPondFragment parentFrag = ((AddPondFragment) AddPondFragmentTwo.this.getParentFragment());
                parentFrag.setFragOne();
                break;
            case R.id.btn_add:
                if (isInternetConnection()) {
                    if (isEdit) {
                        mPresenter.editPond(pond.pondID(), selectedWaterSources, selectedFishTypes, fishFeedType);
                    } else {
                        mPresenter.addPond(false, selectedWaterSources, selectedFishTypes, fishFeedType);
                    }
                } else {
                    showNoConnectionAlert();
                }
                break;
//            case R.id.btn_add_more:
//                if (isInternetConnection()) {
//                    mPresenter.addPond(true,selectedWaterSources, selectedFishTypes, fishFeedType);
//                } else {
//                    showNoConnectionAlert();
//                }
//                break;
        }
    }

    @Override
    public void onItemCheck(String item) {
        selectedWaterSources.add(item);
    }

    @Override
    public void onItemUncheck(String item) {
        selectedWaterSources.remove(item);
    }


}
