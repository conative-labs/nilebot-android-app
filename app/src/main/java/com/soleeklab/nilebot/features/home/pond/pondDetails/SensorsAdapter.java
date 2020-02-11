package com.soleeklab.nilebot.features.home.pond.pondDetails;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.soleeklab.nilebot.GetSensorsByPondIDQuery;
import com.soleeklab.nilebot.ParentRecyclerAdapter;
import com.soleeklab.nilebot.ParentRecyclerViewHolder;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.utils.DateUtil;
import com.soleeklab.nilebot.utils.LocalHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SensorsAdapter extends ParentRecyclerAdapter<GetSensorsByPondIDQuery.Parameter> {

    Context context;
    SensorClicks sensorClicks;
    LocalRepo localRepo;


    public SensorsAdapter(Context context, List<GetSensorsByPondIDQuery.Parameter> data, SensorClicks sensorClicks) {
        super(data);
        this.context = context;
        this.sensorClicks = sensorClicks;
        localRepo = new LocalRepoImpl(context);
    }

    @NonNull
    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sensor, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.setOnItemClickListener(getItemClickListener());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentRecyclerViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final GetSensorsByPondIDQuery.Parameter sensor = getData().get(position);

//        if (LocalHelper.getLanguage().equals("ar"))

        viewHolder.txtSensorName.setText(localRepo.getTranslation(sensor.name()));

        if (sensor.sensor() != null)
            viewHolder.txtSensorDetails.setText(localRepo.getTranslation(sensor.sensor().bot().name()) + " > " + localRepo.getTranslation(sensor.sensor().nameID()));
        else
            viewHolder.txtSensorDetails.setText("-");

        viewHolder.seekBarScale.setEnabled(false);
        if (sensor.lastReading() != null) {
            viewHolder.txtSensorDate.setVisibility(View.VISIBLE);
            viewHolder.txtSensorTime.setVisibility(View.VISIBLE);
            viewHolder.txtSensorDate.setText(DateUtil.formatDate(sensor.lastReading().time(), "yyyy-MM-dd'T'HH:mm:ssX", "yyyy MMM dd", LocalHelper.getLanguage()));
            viewHolder.txtSensorTime.setText(DateUtil.formatDate(sensor.lastReading().time(), "yyyy-MM-dd'T'HH:mm:ssX", "hh:mm:ss a", LocalHelper.getLanguage()));


            viewHolder.txtSensorValue.setText(sensor.lastReading().value() + " " + localRepo.getTranslation(sensor.unit()));


            if (sensor.thresholdLow() != null && sensor.thresholdHigh() != null) {

                double minus = sensor.thresholdHigh().intValue() - sensor.thresholdLow().intValue();
                double percentage = (minus * 10) / 100;


                if (sensor.lastReading().value() >= sensor.thresholdLow() && sensor.lastReading().value() <= sensor.thresholdHigh())
                    viewHolder.txtSensorValue.setTextColor(context.getResources().getColor(R.color.green));
                else
                    viewHolder.txtSensorValue.setTextColor(context.getResources().getColor(R.color.red));


                viewHolder.seekBarScale.setMax((int) (sensor.thresholdHigh() + Math.round(percentage)));

                viewHolder.seekBarScale.setProgress((int) sensor.lastReading().value());

                viewHolder.seekBarScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        int min = (int) (sensor.thresholdLow() - Math.round(percentage));
                        if (progress < min) {
                            seekBar.setProgress(min);


                        }


                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                viewHolder.txtMax.setText((sensor.thresholdHigh()) + " " + sensor.unit());
                viewHolder.txtMin.setText((sensor.thresholdLow()) + " " + sensor.unit());
            } else {
                viewHolder.seekBarScale.setProgress(50);
            }
        } else {
            viewHolder.layFrame.setVisibility(View.GONE);
            viewHolder.txtSensorValue.setText(context.getResources().getString(R.string.no_data));
            viewHolder.txtSensorValue.setTextColor(context.getResources().getColor(R.color.colorPrimary));

//            hn3ml text y2ool no reading
        }

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorClicks.onEditClick(sensor);
            }
        });

        viewHolder.btnShowReads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorClicks.onShowReadsClick(sensor);
            }
        });


    }


    class ViewHolder extends ParentRecyclerViewHolder {


        @BindView(R.id.btn_show_reads)
        TextView btnShowReads;

        @BindView(R.id.txt_sensor_time)
        TextView txtSensorTime;
        @BindView(R.id.txt_sensor_date)
        TextView txtSensorDate;

        @BindView(R.id.txt_sensor_name)
        TextView txtSensorName;
        @BindView(R.id.txt_sensor_details)
        TextView txtSensorDetails;
        @BindView(R.id.btn_edit)
        ImageView btnEdit;
        @BindView(R.id.imgScale)
        ImageView imgScale;
        @BindView(R.id.seekBarScale)
        SeekBar seekBarScale;
        @BindView(R.id.lay_seek)
        ConstraintLayout laySeek;
        @BindView(R.id.txt_sensor_value)
        TextView txtSensorValue;
        @BindView(R.id.txt_min)
        TextView txtMin;
        @BindView(R.id.txt_max)
        TextView txtMax;
        @BindView(R.id.lay_frame)
        FrameLayout layFrame;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setClickableRootView(itemView);
        }
    }


    interface SensorClicks {

        void onEditClick(GetSensorsByPondIDQuery.Parameter sensor);

        void onShowReadsClick(GetSensorsByPondIDQuery.Parameter sensor);
    }
}
