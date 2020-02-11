package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soleeklab.nilebot.ParentFragment;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.MyCache;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends ParentFragment {


    @BindView(R.id.rv_summary)
    RecyclerView rvSummary;
    Unbinder unbinder;

    SummaryAdapter summaryAdapter;

    @Inject
    MyCache myCache;

    @Inject
    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSummaryList();
    }

    private void initSummaryList() {
        summaryAdapter = new SummaryAdapter(myCache.getSensorSummaryArrayList(), getActivity());
        rvSummary.setAdapter(summaryAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
