package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.models.SensorSummary;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

    private Context context;

    private ArrayList<SensorSummary> sensorSummaries;
    private LocalRepo localRepo;

    public SummaryAdapter(ArrayList<SensorSummary> sensorSummaries, Context context) {
        this.sensorSummaries = sensorSummaries;
        this.context = context;
        this.localRepo = new LocalRepoImpl(context);
    }

    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_summary, parent, false);

        return new SummaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SummaryViewHolder holder, int position) {
        SensorSummary sensorSummary = sensorSummaries.get(position);

        holder.sensorName.setText(localRepo.getTranslation(sensorSummary.getSensorName()));
        holder.min.setText(new DecimalFormat("##.##").format(sensorSummary.getMin()) + " "+ sensorSummary.getUnit());
        holder.max.setText(new DecimalFormat("##.##").format(sensorSummary.getMax()) + " "+ sensorSummary.getUnit());
        holder.average.setText(new DecimalFormat("##.##").format(sensorSummary.getAverage()) + " "+ sensorSummary.getUnit());
//
//        holder.min.setText(new DecimalFormat("##.##").format(sensorSummary.getMin()) + " ");
//        holder.max.setText(new DecimalFormat("##.##").format(sensorSummary.getMax()) + " ");
//        holder.average.setText(new DecimalFormat("##.##").format(sensorSummary.getAverage()) + " ");

    }

    @Override
    public int getItemCount() {
        return sensorSummaries.size();
    }

    public class SummaryViewHolder extends RecyclerView.ViewHolder {
        public TextView sensorName, min, max, average;


        public SummaryViewHolder(View view) {
            super(view);
            sensorName = view.findViewById(R.id.txt_sensor_name);
            min = view.findViewById(R.id.txt_min);
            max = view.findViewById(R.id.txt_max);
            average = view.findViewById(R.id.txt_average);

        }
    }

}
