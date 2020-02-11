package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soleeklab.nilebot.GetSensorsReadingByPondIDQuery;
import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MyCache;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PondSensorsReadingsFragment extends ParentFragment {


    @BindView(R.id.rv_sensors_readings)
    RecyclerView rvSensorsReadings;
    Unbinder unbinder;

    ParentSensorAdapter parentSensorAdapter;

    @Inject
    MyCache myCache;


    @Inject
    public PondSensorsReadingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pond_sensors_readings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(myCache.getMap());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initList(Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map) {
        if (map.size() > 0) {
            rvSensorsReadings.setVisibility(View.VISIBLE);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parentSensorAdapter = new ParentSensorAdapter(map, getActivity());
                    rvSensorsReadings.setAdapter(parentSensorAdapter);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
