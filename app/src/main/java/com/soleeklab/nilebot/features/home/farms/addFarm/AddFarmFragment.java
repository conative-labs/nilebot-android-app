package com.soleeklab.nilebot.features.home.farms.addFarm;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.soleeklab.nilebot.GetFarmsQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.Constants;
import com.soleeklab.nilebot.data.models.MessageEvent;
import com.soleeklab.nilebot.data.models.MyCache;
import com.soleeklab.nilebot.features.home.farms.TimeZonesAdapter;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.soleeklab.nilebot.utils.DateUtil.displayTimeZone;


public class AddFarmFragment extends ParentFragment implements AddFarmContract.View {

    @BindView(R.id.edt_pharm_name)
    EditText edtPharmName;

    @BindView(R.id.edt_map)
    EditText edtMap;
    @BindView(R.id.edt_area)
    EditText edtArea;
    @BindView(R.id.btn_add_farm)
    Button btnAddFarm;
    @BindView(R.id.spinner_farmtype)
    Spinner spinnerFarmtype;
    @BindView(R.id.txt_error_farm_name)
    TextView txtErrorFarmName;
    @BindView(R.id.txt_error_farm_type)
    TextView txtErrorFarmType;
    @BindView(R.id.txt_error_address)
    TextView txtErrorAddress;
    @BindView(R.id.txt_error_location)
    TextView txtErrorLocation;
    @BindView(R.id.txt_error_area)
    TextView txtErrorArea;
    Unbinder unbinder;

    @Inject
    MyCache myCache;

    @Inject
    AddFarmContract.Presenter mPresenter;

    FarmTypeAdapter farmTypeAdapter;
    UnitAreaTypeAdapter unitAreaTypeAdapter;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.txt_add_farm)
    TextView txtAddFarm;
    @BindView(R.id.spinner_unit_areas)
    Spinner spinnerUnitAreas;
    @BindView(R.id.txt_error_unit)
    TextView txtErrorUnit;
    @BindView(R.id.spinner_time_zones)
    Spinner spinnerTimeZones;

    private String farmType = "";
    private String unitType = "";
    private String timeZone = "";
    private float area;
    Geocoder geocoder;
    List<Address> addresses;
    private boolean isEdit = false;
    private GetFarmsQuery.Farm farm;
    private ArrayList<String> farmTypeEditList = new ArrayList<>();
    private ArrayList<String> farmAreaUnittypes = new ArrayList<>();
    private ArrayList<String> farmTimeZones = new ArrayList<>();
    private int measureUnit = 0;

    private TimeZonesAdapter timeZonesAdapter;

    private double lastUnitValue;


    @Inject
    public AddFarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_farm, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setPresenter(AddFarmContract.Presenter presenter) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        mPresenter.start();
        initTimeZones();

        txtToolbarTitle.setText(getString(R.string.new_farm));
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        myCache.setLng(0);
        myCache.setLat(0);
        isEdit = getActivity().getIntent().getBooleanExtra("isEdit", false);
        edtMap.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edtMap.setEnabled(false);
                    startActivity(new Intent(getActivity(), MapActivity.class));

                }
            }
        });
        if (isEdit) {
            Gson gson = new Gson();
            farm = gson.fromJson(getActivity().getIntent().getStringExtra("farm"), GetFarmsQuery.Farm.class);
            initEditFields(farm);
        }


    }

    private void initTimeZones() {
        String[] ids = TimeZone.getAvailableIDs();
        ArrayList<String> timeZones = new ArrayList<>();

        for (String id : ids) {
            timeZones.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        Collections.sort(timeZones);
        timeZones.add(0, getString(R.string.choose_time_zone));

        if (spinnerTimeZones != null) {
            farmTimeZones.addAll(timeZones);
            timeZonesAdapter = new TimeZonesAdapter(getActivity(), R.layout.item_farmtype_spinner, timeZones);
            spinnerTimeZones.setAdapter(timeZonesAdapter);
            spinnerTimeZones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        timeZone = timeZones.get(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            for (int i = 0; i < timeZonesAdapter.getCount(); i++) {
                if (timeZonesAdapter.getItem(i).equals(displayTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID())))) {
                    spinnerTimeZones.setSelection(i);
                }
            }
        }
    }

    private void initEditFields(GetFarmsQuery.Farm farm) {
        btnAddFarm.setText(getString(R.string.edit));
        txtToolbarTitle.setText(getString(R.string.edit));
        txtAddFarm.setText(getString(R.string.edit_farm));
        edtPharmName.setText(farm.name());
        if (!farm.latitude().equals("") && !farm.latitude().equals("null")) {
            myCache.setLat(Double.parseDouble(farm.latitude()));
            myCache.setLng(Double.parseDouble(farm.longitude()));
            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(farm.latitude()), Double.parseDouble(farm.longitude()), 1);
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                edtMap.setText(address.replaceAll("null", " "));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            myCache.setLat(Constants.EGYPT_TAHRIR_LAT1);
            myCache.setLng(Constants.EGYPT_TAHRIR_LNG1);
        }
        edtArea.setText(farm.area() + " ");

        farmType = farm.type().rawValue();
        int pos = 0;
        for (int i = 0; i < farmTypeEditList.size(); i++) {
            if (farmTypeEditList.get(i).equals(farm.type().rawValue()))
                pos = i;
        }

        int Unitpos = 0;
        for (int i = 0; i < farmAreaUnittypes.size(); i++) {
            if (farmAreaUnittypes.get(i).equals(farm.areaUnit().rawValue()))
                Unitpos = i;
        }


        spinnerFarmtype.setSelection(pos);
        spinnerUnitAreas.setSelection(Unitpos);


        String[] ids = TimeZone.getAvailableIDs();

        ArrayList<String> timeZones = new ArrayList<>();
        for (String id : ids) {
            timeZones.add(displayTimeZone(TimeZone.getTimeZone(id)));
        }
        Collections.sort(timeZones);
        timeZones.add(0, getString(R.string.choose_time_zone));
        timeZones.add(farm.timezone());

        // bot.timezone()

        if (spinnerTimeZones != null) {
            farmTimeZones.clear();
            timeZonesAdapter.clear();

            farmTimeZones.addAll(timeZones);
            timeZonesAdapter = new TimeZonesAdapter(getActivity(), R.layout.item_farmtype_spinner, timeZones);
            spinnerTimeZones.setAdapter(timeZonesAdapter);
            spinnerTimeZones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {

                        timeZone = timeZones.get(position);
                        if (!farm.timezone().equals(timeZone))
                            timeZone = timeZone.substring(4, 10);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        spinnerTimeZones.setSelection(timeZonesAdapter.getCount());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.unregisterView();
    }

    @OnClick({R.id.edt_map, R.id.btn_add_farm, R.id.btn_back})
    public void onViewClicked(View view) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);

        switch (view.getId()) {
            case R.id.edt_map:
                edtMap.setEnabled(false);
                Intent i = new Intent(getActivity(), MapActivity.class);

                startActivity(i);
                break;
            case R.id.btn_add_farm:
                if (isInternetConnection()) {
                    if (!edtArea.getText().toString().equals("") && !edtArea.getText().toString().equals("null")) {
                        area = Float.parseFloat(edtArea.getText().toString());
                    } else
                        area = 0;
                    if (isEdit) {
                        mPresenter.editFarm(
                                farm.farmID(),
                                edtPharmName.getText().toString(),
                                farmType,
                                area,
                                unitType,
                                myCache.getLat() + "",
                                myCache.getLng() + "",
                                "",
                                timeZone);
                    } else {
                        mPresenter.addFarm(
                                edtPharmName.getText().toString(),
                                farmType,
                                area,
                                unitType,
                                myCache.getLat() + "",
                                myCache.getLng() + "",
                                "",
                                timeZone.substring(4, 10)
                        );
                    }

                } else
                    showNoConnectionAlert();

                break;
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        edtMap.setEnabled(true);
        handleLatLngAddress();
    }

    private void handleLatLngAddress() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myCache.getLng() != 0.0) {
                    try {
                        addresses = geocoder.getFromLocation(myCache.getLat(), myCache.getLng(), 1);
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        edtMap.setText(address.replaceAll("null",""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void removeErrors() {

        txtErrorFarmName.setVisibility(View.GONE);
        txtErrorFarmType.setVisibility(View.GONE);
        txtErrorAddress.setVisibility(View.GONE);
        txtErrorLocation.setVisibility(View.GONE);
        txtErrorArea.setVisibility(View.GONE);
        txtErrorUnit.setVisibility(View.GONE);
    }

    @Override
    public void showInvalidFarmName() {
        txtErrorFarmName.setVisibility(View.VISIBLE);
        txtErrorFarmName.setText(getString(R.string.error_required));
    }

    @Override
    public void showInbalidFarmType() {
        txtErrorFarmType.setVisibility(View.VISIBLE);
        txtErrorFarmType.setText(getString(R.string.error_required));
    }

    @Override
    public void showInvalidFarmAddress() {
        txtErrorAddress.setVisibility(View.VISIBLE);
        txtErrorAddress.setText(getString(R.string.error_required));
    }

    @Override
    public void showInvalidFarmLocation() {
        txtErrorLocation.setVisibility(View.VISIBLE);
        txtErrorLocation.setText(getString(R.string.error_required));
    }

    @Override
    public void showInvalidFarmArea() {
        txtErrorArea.setVisibility(View.VISIBLE);
        txtErrorArea.setText(getString(R.string.error_required));
    }

    @Override
    public void showLargeFarmArea() {
        txtErrorArea.setVisibility(View.VISIBLE);
        txtErrorArea.setText(getString(R.string.error_large));
    }

    @Override
    public void showRequiredunit() {
        txtErrorUnit.setVisibility(View.VISIBLE);
        txtErrorUnit.setText(getString(R.string.error_required_unit));
    }

    @Override
    public void addFarmSuccress() {
        EventBus.getDefault().post(new MessageEvent("refresh_farms"));
        getActivity().onBackPressed();
    }

    @Override
    public void initFarmTypes(ArrayList<String> farmTypesList) {
        farmTypesList.remove(farmTypesList.size() - 1);
        farmTypesList.add(0, getString(R.string.choose_farm_type));
        farmTypeEditList.addAll(farmTypesList);
        farmTypeAdapter = new FarmTypeAdapter(getActivity(), R.layout.item_farmtype_spinner, farmTypesList);
        spinnerFarmtype.setAdapter(farmTypeAdapter);
        spinnerFarmtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    farmType = farmTypesList.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void initAreaUnitTypes(ArrayList<String> areaUnitTypes) {
        areaUnitTypes.remove(areaUnitTypes.size() - 1);
        areaUnitTypes.add(0, getString(R.string.choose_unit_type));

        farmAreaUnittypes.addAll(areaUnitTypes);
        unitAreaTypeAdapter = new UnitAreaTypeAdapter(getActivity(), R.layout.item_farmtype_spinner, areaUnitTypes);
        spinnerUnitAreas.setAdapter(unitAreaTypeAdapter);
        spinnerUnitAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    unitType = areaUnitTypes.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
