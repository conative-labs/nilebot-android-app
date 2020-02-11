package com.soleeklab.nilebot.features.home.pond.addPond.addPondFormOne;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.soleeklab.nilebot.GetPondsByFarmIDQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.api.AddPondData;
import com.soleeklab.nilebot.data.models.MyCache;
import com.soleeklab.nilebot.features.home.pond.addPond.AddPondFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddPondFragmentOne extends ParentFragment implements AddPondFormOneContract.View {


    @BindView(R.id.edt_pond_name)
    EditText edtPondName;
    @BindView(R.id.txt_error_pond_name)
    TextView txtErrorPondName;
    @BindView(R.id.spinner_pond_type)
    Spinner spinnerPondType;
    @BindView(R.id.txt_error_pond_type)
    TextView txtErrorPondType;
    @BindView(R.id.rdbtn_rectangle)
    RadioButton rdbtnRectangle;
    @BindView(R.id.rdbtn_cylinder)
    RadioButton rdbtnCylinder;
    @BindView(R.id.edt_length)
    EditText edtLength;
    @BindView(R.id.txt_error_pond_length)
    TextView txtErrorPondLength;
    @BindView(R.id.edt_width)
    EditText edtWidth;
    @BindView(R.id.txt_error_pond_width)
    TextView txtErrorPondWidth;
    @BindView(R.id.edt_height)
    EditText edtHeight;
    @BindView(R.id.txt_error_pond_height)
    TextView txtErrorPondHeight;
    @BindView(R.id.rdbtn_yes)
    RadioButton rdbtnYes;
    @BindView(R.id.rdbtn_no)
    RadioButton rdbtnNo;
    @BindView(R.id.txt_error_pond_count)
    TextView txtErrorPondCount;
    @BindView(R.id.btn_next)
    Button btnNext;
    Unbinder unbinder;
    @BindView(R.id.txt_edit_pond)
    TextView txtEditPond;
    PondTypeAdapter pondTypeAdapter;
    @BindView(R.id.lay_length_width)
    LinearLayout layLengthWidth;
    @BindView(R.id.scroll_one)
    ScrollView scrollOne;
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.view_sep)
    RelativeLayout viewSep;
    @BindView(R.id.txt_width)
    TextView txtWidth;

    private String pondType = "";
    private boolean isEquipe = true;
    private boolean isRect = true;

    private Float length = 0f;
    private Float width = 0f;
    private Float height = 0f;

    @Inject
    AddPondFormOneContract.Presenter mPresenter;

    @Inject
    MyCache myCache;

    private boolean isEdit = false;
    private GetPondsByFarmIDQuery.Pond pond;
    private ArrayList<String> pondTypeEditList = new ArrayList<>();


    @Inject
    public AddPondFragmentOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pond_fragment_one, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.registerView(this);
        mPresenter.start();
        txtToolbarTitle.setText(getString(R.string.new_pond));
        isEdit = getActivity().getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            Gson gson = new Gson();
            pond = gson.fromJson(getActivity().getIntent().getStringExtra("pond"), GetPondsByFarmIDQuery.Pond.class);
            initEditFields(pond);
        }

    }

    private void initEditFields(GetPondsByFarmIDQuery.Pond pond) {


        edtPondName.setText(pond.name());

        txtEditPond.setText(getString(R.string.edit_pond));

        txtToolbarTitle.setText(getString(R.string.edit));

        int pos = 0;
        for (int i = 0; i < pondTypeEditList.size(); i++) {
            if (pondTypeEditList.get(i).equals(pond.type().rawValue()))
                pos = i;
        }

        spinnerPondType.setSelection(pos);


        if (pond.length() != null) {
            if (pond.length() != 0) {
                isRect = true;
                rdbtnRectangle.setChecked(true);
                layLengthWidth.setVisibility(View.VISIBLE);

            } else {
                rdbtnCylinder.setChecked(true);
                isRect = false;
                layLengthWidth.setVisibility(View.GONE);
            }
        } else {
            rdbtnCylinder.setChecked(true);
            isRect = false;
            layLengthWidth.setVisibility(View.GONE);
        }


        edtWidth.setText(pond.width() + "");
        if (pond.length() != null) {
            edtLength.setText(pond.length() + "");
        } else {
            edtLength.setText(0 + "");

        }
        edtHeight.setText(pond.depth() + "");

        if (pond.aerationEquipment())
            rdbtnYes.setChecked(true);
        else
            rdbtnNo.setChecked(true);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void removeErrors() {
        txtErrorPondName.setVisibility(View.GONE);
        txtErrorPondType.setVisibility(View.GONE);
        txtErrorPondLength.setVisibility(View.GONE);
        txtErrorPondWidth.setVisibility(View.GONE);
        txtErrorPondHeight.setVisibility(View.GONE);
        txtErrorPondCount.setVisibility(View.GONE);

    }

    @Override
    public void showInvalidPondName() {
        txtErrorPondName.setText(getString(R.string.error_required));
        txtErrorPondName.setVisibility(View.VISIBLE);
        scrollOne.smoothScrollTo(0, (int) edtPondName.getY());
    }

    @Override
    public void showInbalidPondType() {
        txtErrorPondType.setVisibility(View.VISIBLE);
        txtErrorPondType.setText(getString(R.string.error_required));
        scrollOne.smoothScrollTo(0, (int) spinnerPondType.getY());
    }

    @Override
    public void showInvalidLength() {
        txtErrorPondLength.setVisibility(View.VISIBLE);
        txtErrorPondLength.setText(getString(R.string.error_required));
        scrollOne.smoothScrollTo(0, (int) edtLength.getY());

    }

    @Override
    public void showLargeLength() {
        txtErrorPondLength.setVisibility(View.VISIBLE);
        txtErrorPondLength.setText(getString(R.string.error_large));
        scrollOne.smoothScrollTo(0, (int) edtLength.getY());
    }

    @Override
    public void showInvalidWidth() {
        txtErrorPondWidth.setVisibility(View.VISIBLE);
        txtErrorPondWidth.setText(getString(R.string.error_required));
        scrollOne.smoothScrollTo(0, (int) edtWidth.getY());

    }

    @Override
    public void showLargeWidth() {
        txtErrorPondWidth.setVisibility(View.VISIBLE);
        txtErrorPondWidth.setText(getString(R.string.error_large));
        scrollOne.smoothScrollTo(0, (int) edtWidth.getY());
    }

    @Override
    public void showInvalidHeight() {
        txtErrorPondHeight.setVisibility(View.VISIBLE);
        txtErrorPondHeight.setText(getString(R.string.error_required));
        scrollOne.smoothScrollTo(0, (int) edtHeight.getY());

    }

    @Override
    public void showLargeHeight() {
        txtErrorPondHeight.setVisibility(View.VISIBLE);
        txtErrorPondHeight.setText(getString(R.string.error_large));
        scrollOne.smoothScrollTo(0, (int) edtHeight.getY());
    }


    @Override
    public void goToNextForm(AddPondData addPondData) {
//        view
        myCache.setAddPondData(addPondData);
        hideInputType();
        AddPondFragment parentFrag = ((AddPondFragment) AddPondFragmentOne.this.getParentFragment());
        parentFrag.setFragTwo();


    }

    @Override
    public void initPondTypes(ArrayList<String> pondTypesList) {
        pondTypesList.remove(pondTypesList.size() - 1);
        pondTypesList.add(0, getString(R.string.choose_pond_type));

        pondTypeEditList.addAll(pondTypesList);

        pondTypeAdapter = new PondTypeAdapter(getActivity(), R.layout.item_farmtype_spinner, pondTypesList);
        spinnerPondType.setAdapter(pondTypeAdapter);
        spinnerPondType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    pondType = pondTypesList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void setPresenter(AddPondFormOneContract.Presenter presenter) {

    }


    @OnClick({R.id.rdbtn_rectangle, R.id.rdbtn_cylinder, R.id.rdbtn_yes, R.id.rdbtn_no, R.id.btn_next, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rdbtn_rectangle:
                isRect = true;
                txtWidth.setText(getString(R.string.width));
                layLengthWidth.setVisibility(View.VISIBLE);
                break;
            case R.id.rdbtn_cylinder:
                isRect = false;
                txtWidth.setText(getString(R.string.diameter));
                layLengthWidth.setVisibility(View.GONE);
                break;
            case R.id.rdbtn_yes:
                isEquipe = true;
                break;
            case R.id.rdbtn_no:
                isEquipe = false;
                break;
            case R.id.btn_next:
                if (isInternetConnection()) {
                    if (!edtLength.getText().toString().equals(""))
                        length = Float.parseFloat(edtLength.getText().toString());
                    if (!edtWidth.getText().toString().equals(""))
                        width = Float.parseFloat(edtWidth.getText().toString());
                    if (!edtHeight.getText().toString().equals(""))
                        height = Float.parseFloat(edtHeight.getText().toString());

                    mPresenter.nextPond(getActivity().getIntent().getStringExtra("farmId"), edtPondName.getText().toString(), pondType,
                            isRect, length, width, height, isEquipe);
                } else {
                    showNoConnectionAlert();
                }
                break;
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
        }
    }
}
