package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soleeklab.nilebot.GetSensorsReadingByPondIDQuery;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.utils.DateUtil;
import com.soleeklab.nilebot.utils.LocalHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParentSensorAdapter extends RecyclerView.Adapter<ParentSensorAdapter.MyViewHolder> {

    private Context context;

    private Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map;


    public ParentSensorAdapter(Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map, Context context) {
        this.map = map;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sensor_reads, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        Movie movie = moviesList.get(position);


        List<String> indexes = new ArrayList<String>(map.keySet());
        String key = indexes.get(position);

        holder.date.setText(DateUtil.formatDate(key, "yyyy-MM-dd h:mm:ss", "yyyy-MM-dd", LocalHelper.getLanguage()));
        holder.time.setText(DateUtil.formatDate(key, "yyyy-MM-dd h:mm:ss", "h:mm:ss aaa", LocalHelper.getLanguage()));


        ChildSensorAdapter childSensorAdapter = new ChildSensorAdapter(map.get(key), context);
        holder.rvReads.setAdapter(childSensorAdapter);
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, time;
        public RecyclerView rvReads;

        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.txt_sensor_date);
            time = view.findViewById(R.id.txt_sensor_time);
            rvReads = view.findViewById(R.id.rv_sensors_readings);

        }
    }


}
